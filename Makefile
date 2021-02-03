UNAME := $(shell uname | tr '[:upper:]' '[:lower:]')

ifeq ($(UNAME), darwin)
    OS := osx
else
	OS := linux
endif

PROTOC := docker-compose run --rm protobuf

GAME_DIR := servers/game

JAVA_DIR := ./servers/game
JAVA_DST_DIR := $(JAVA_DIR)/src/main/java
JAVA_SRC_DIR := $(JAVA_DIR)/src/main/protos
JAVA_TEST_DST_DIR := $(JAVA_DIR)/src/test/java
JAVA_TEST_SRC_DIR := $(JAVA_DIR)/src/test/protos

PHP_DIR := ./servers/webclient
PHP_DST_DIR := $(PHP_DIR)/protobuf

.PHONY: help
help: ## Outputs this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: build-proto
build-proto:
	@echo "Generating java protos" && $(PROTOC) -I=$(JAVA_SRC_DIR) --java_out=$(JAVA_DST_DIR) $(JAVA_SRC_DIR)/*.proto
	@echo "Generating test java protos" && $(PROTOC) -I=$(JAVA_TEST_SRC_DIR) --java_out=$(JAVA_TEST_DST_DIR) $(JAVA_TEST_SRC_DIR)/*.proto
	@echo "Generating php protos" && $(PROTOC) -I=$(JAVA_SRC_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_SRC_DIR)/*.proto

.PHONY: bp
bp: build-proto ## alias of build-proto

.PHONY: start
start: ## Runs the :servers:game run
	./gradlew :servers:game:run

.PHONY: test
test: build-proto ## Runs the :servers:game tests
	@bash $(GAME_DIR)/bin/test/setup.sh
	./gradlew :servers:game:test

.PHONY: build
build: ## Builds the :servers:game and :servers:client projects
	./gradlew :servers:game:build
	./gradlew :servers:web:build

.PHONY: server
server: ## Launches the game server
	./gradlew :servers:game:run

.PHONY: client
client: ## Launches the game client
	./gradlew :client:run

.PHONY: composer
composer: servers/webclient/vendor/autoload.php ## Launches a composer install

servers/webclient/vendor/autoload.php: servers/webclient/composer.lock
	composer install -d servers/webclient
	touch servers/webclient/vendor/autoload.php