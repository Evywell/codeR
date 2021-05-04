UNAME := $(shell uname | tr '[:upper:]' '[:lower:]')

ifeq ($(UNAME), darwin)
    OS := osx
else
	OS := linux
endif

SUPPORTED_COMMANDS := migration-create seed-create
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

##> Migrator
MIGRATOR := $(DCR) migrator
PHINX := $(MIGRATOR) migrations/migrator/vendor/bin/phinx
PHINX_CONFIG_ARG := --configuration migrations/migrator
PHINX_WORLD_CONFIG_ARG := $(PHINX_CONFIG_ARG)/phinx-world.php
PHINX_PLAYERS_CONFIG_ARG := $(PHINX_CONFIG_ARG)/phinx-players.php

MIGRATOR_SEED_NAME=
MIGRATOR_DB=
##< Migrator

GAME_DIR := servers/game
LOGIN_DIR := servers/login

JAVA_GAME_DIR := ./$(GAME_DIR)
JAVA_GAME_DST_DIR := $(JAVA_GAME_DIR)/src/main/java
JAVA_GAME_PROTOS_DIR := $(JAVA_GAME_DIR)/src/main/protos
JAVA_GAME_TEST_DST_DIR := $(JAVA_GAME_DIR)/src/test/java
JAVA_GAME_TEST_SRC_DIR := $(JAVA_GAME_DIR)/src/test/protos

JAVA_LOGIN_DIR := ./$(LOGIN_DIR)
JAVA_LOGIN_PROTOS_DIR := $(JAVA_LOGIN_DIR)/src/main/protos
JAVA_LOGIN_DST_DIR := $(JAVA_LOGIN_DIR)/src/main/java

PHP_DIR := ./servers/webclient
PHP_DST_DIR := $(PHP_DIR)/protobuf

##> Debug
DEBUGGER_SOCKET_PORT := 5005
DEBUGGER_BUILD_GAME_JAR_PATH := build/libs/game-1.0.jar
DEBUGGER_BUILD_LOGIN_JAR_PATH := build/libs/login-1.0.jar
##< Debug

.PHONY: help
help: ## Outputs this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: build-proto
build-proto: ## Builds protos for java and php
	@make build-game-proto
	@make build-login-proto

.PHONY: build-game-proto
build-game-proto: ## Builds game protos for java and php
	@echo "Generating game java protos" && $(PROTOC) -I=$(JAVA_GAME_PROTOS_DIR) --java_out=$(JAVA_GAME_DST_DIR) $(JAVA_GAME_PROTOS_DIR)/*.proto
	@echo "Generating test java protos" && $(PROTOC) -I=$(JAVA_GAME_TEST_SRC_DIR) --java_out=$(JAVA_GAME_TEST_DST_DIR) $(JAVA_GAME_TEST_SRC_DIR)/*.proto
	@echo "Generating php protos" && $(PROTOC) -I=$(JAVA_GAME_PROTOS_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_GAME_PROTOS_DIR)/*.proto

.PHONY: build-login-proto
build-login-proto: ## Builds login protos for java and php
	@echo "Generating login java protos" && $(PROTOC) -I=$(JAVA_LOGIN_PROTOS_DIR) --java_out=$(JAVA_LOGIN_DST_DIR) $(JAVA_LOGIN_PROTOS_DIR)/*.proto
	@echo "Generating php protos" && $(PROTOC) -I=$(JAVA_LOGIN_PROTOS_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_LOGIN_PROTOS_DIR)/*.proto

.PHONY: bp
bp: build-proto ## alias of build-proto

.PHONY: run
run: up ## Runs the :servers:game run
	$(GRADLE_TASK) :servers:game:run

.PHONY: up
up: .env ## Runs all the docker containers
	$(DOCKER_COMPOSE) up -d

.PHONY: test
test: build-proto setup-tests ## Runs the :servers:game tests
	@bash $(GAME_DIR)/bin/test/setup.sh
	@make task-test

.PHONY: task-test
task-test:
	$(GRADLE_TASK) :servers:game:test

.PHONY: setup-tests
setup-tests: servers/game/src/test/resources/private.pem

.PHONY: build
build: up ## Builds the :servers:game and :servers:client projects
	$(GRADLE_TASK) :servers:game:build
	$(GRADLE_TASK) :servers:web:build

.PHONY: build-debug
build-debug: build ## Runs the build then start the debugger socket
	$(GRADLE) java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:$(DEBUGGER_SOCKET_PORT) $(GAME_DIR)/$(DEBUGGER_BUILD_GAME_JAR_PATH)

.PHONY: build-login-debug
build-login-debug: build-login
	$(GRADLE) java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:$(DEBUGGER_SOCKET_PORT) $(LOGIN_DIR)/$(DEBUGGER_BUILD_LOGIN_JAR_PATH)

.PHONY: build-login
build-login:
	$(GRADLE_TASK) :servers:login:build

.PHONY: server
server: ## Launches the game server
	$(GRADLE_TASK) :servers:game:run

.PHONY: login
login: ## Launches the login server
	$(GRADLE_TASK) :servers:login:run

.PHONY: client
client: ## Launches the game client
	$(GRADLE_TASK) :client:run

.PHONY: composer
composer: servers/webclient/vendor/autoload.php ## Launches a composer install

.PHONY: migrate
migrate: migrations/migrator/vendor/autoload.php
	$(PHINX) migrate $(PHINX_WORLD_CONFIG_ARG) -e development
	$(PHINX) migrate $(PHINX_PLAYERS_CONFIG_ARG) -e development

.PHONY: seed
seed: migrations/migrator/vendor/autoload.php
	$(PHINX) seed:run $(PHINX_WORLD_CONFIG_ARG) -e development
	$(PHINX) seed:run $(PHINX_PLAYERS_CONFIG_ARG) -e development

.PHONY: migration-create
migration-create: migrations/migrator/vendor/autoload.php
	$(PHINX) create $(COMMAND_ARGS) $(PHINX_CONFIG_ARG)
	$(PHINX) create $(COMMAND_ARGS) $(PHINX_CONFIG_ARG)

.PHONY: seed-create
seed-create: migrations/migrator/vendor/autoload.php
	$(PHINX) seed:create $(MIGRATOR_SEED_NAME) $(PHINX_CONFIG_ARG)/phinx-$(MIGRATOR_DB).php

.PHONY: migration-status
migration-status: up
	$(PHINX) status --configuration migrator/phinx.php

migrations/migrator/vendor/autoload.php: up
	$(MIGRATOR) composer install -d migrations/migrator

servers/game/src/test/resources/private.pem:
	@bash $(GAME_DIR)/bin/test/setup.sh

servers/webclient/vendor/autoload.php: servers/webclient/composer.lock
	composer install -d servers/webclient
	touch servers/webclient/vendor/autoload.php

.env:
	cp .env.dev .env
