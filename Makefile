UNAME := $(shell uname | tr '[:upper:]' '[:lower:]')

ifeq ($(UNAME), darwin)
    OS := osx
else
	OS := linux
endif

SUPPORTED_COMMANDS := migration-create
SUPPORTS_MAKE_ARGS := $(findstring $(firstword $(MAKECMDGOALS)), $(SUPPORTED_COMMANDS))
ifneq "$(SUPPORTS_MAKE_ARGS)" ""
  COMMAND_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  $(eval $(COMMAND_ARGS):;@:)
endif

DOCKER_COMPOSE := docker-compose
DCR := $(DOCKER_COMPOSE) run --rm
DCE := $(DOCKER_COMPOSE) exec

GRADLE := $(DCE) gradle
GRADLE_TASK := $(GRADLE) gradle

PROTOC := $(DCR) protobuf
MIGRATOR := $(DCR) migrator
PHINX := $(MIGRATOR) migrations/migrator/vendor/bin/phinx
PHINX_CONFIG_ARG := --configuration migrations/migrator/phinx.php

GAME_DIR := servers/game

JAVA_DIR := ./servers/game
JAVA_DST_DIR := $(JAVA_DIR)/src/main/java
JAVA_SRC_DIR := $(JAVA_DIR)/src/main/protos
JAVA_TEST_DST_DIR := $(JAVA_DIR)/src/test/java
JAVA_TEST_SRC_DIR := $(JAVA_DIR)/src/test/protos

PHP_DIR := ./servers/webclient
PHP_DST_DIR := $(PHP_DIR)/protobuf

##> Debug
DEBUGGER_SOCKET_PORT := 5005
DEBUGGER_BUILD_JAR_PATH := build/libs/game-1.0.jar
##< Debug

.PHONY: help
help: ## Outputs this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: build-proto
build-proto: ## Builds protos for java and php
	@echo "Generating java protos" && $(PROTOC) -I=$(JAVA_SRC_DIR) --java_out=$(JAVA_DST_DIR) $(JAVA_SRC_DIR)/*.proto
	@echo "Generating test java protos" && $(PROTOC) -I=$(JAVA_TEST_SRC_DIR) --java_out=$(JAVA_TEST_DST_DIR) $(JAVA_TEST_SRC_DIR)/*.proto
	@echo "Generating php protos" && $(PROTOC) -I=$(JAVA_SRC_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_SRC_DIR)/*.proto

.PHONY: bp
bp: build-proto ## alias of build-proto

.PHONY: start
start: up ## Runs the :servers:game run
	./gradlew :servers:game:run

.PHONY: up
up: .env ## Runs all the docker containers
	$(DOCKER_COMPOSE) up -d

.PHONY: test
test: build-proto ## Runs the :servers:game tests
	@bash $(GAME_DIR)/bin/test/setup.sh
	$(GRADLE_TASK) :servers:game:test

.PHONY: build
build: up ## Builds the :servers:game and :servers:client projects
	$(GRADLE_TASK) :servers:game:build
	$(GRADLE_TASK) :servers:web:build

.PHONY: build-debug
build-debug: build ## Runs the build then start the debugger socket
	$(GRADLE) java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:$(DEBUGGER_SOCKET_PORT) $(GAME_DIR)/$(DEBUGGER_BUILD_JAR_PATH)

.PHONY: server
server: ## Launches the game server
	$(GRADLE_TASK) :servers:game:run

.PHONY: client
client: ## Launches the game client
	$(GRADLE_TASK) :client:run

.PHONY: composer
composer: servers/webclient/vendor/autoload.php ## Launches a composer install

.PHONY: migrate
migrate: migrations/migrator/vendor/autoload.php
	$(PHINX) migrate $(PHINX_CONFIG_ARG) -e development

.PHONY: migration-create
migration-create: migrations/migrator/vendor/autoload.php
	$(PHINX) create $(COMMAND_ARGS) $(PHINX_CONFIG_ARG)

.PHONY: migration-status
migration-status: up
	$(PHINX) status --configuration migrator/phinx.php

migrations/migrator/vendor/autoload.php: up
	$(MIGRATOR) composer install -d migrations/migrator

servers/webclient/vendor/autoload.php: servers/webclient/composer.lock
	composer install -d servers/webclient
	touch servers/webclient/vendor/autoload.php

.env:
	cp .env.dev .env
