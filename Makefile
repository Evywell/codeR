UNAME := $(shell uname | tr '[:upper:]' '[:lower:]')

ifeq ($(UNAME), darwin)
 	OS = osx
else
	OS = linux
endif

# @todo: replace this by variables
SUPPORTED_COMMANDS := migration-players-create migration-world-create migration-config-create seed-create
SUPPORTS_MAKE_ARGS := $(findstring $(firstword $(MAKECMDGOALS)), $(SUPPORTED_COMMANDS))
ifneq "$(SUPPORTS_MAKE_ARGS)" ""
  COMMAND_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  $(eval $(COMMAND_ARGS):;@:)
endif

ifdef CI_SERVER
NO_INTERACTIVE_FLAGS = -T
else
NO_INTERACTIVE_FLAGS =
endif

DOCKER_COMPOSE = docker-compose
DCR = $(DOCKER_COMPOSE) run --rm $(NO_INTERACTIVE_FLAGS)
DCE = $(DOCKER_COMPOSE) exec

GRADLE = $(DCE) gradle
GRADLE_TASK = $(GRADLE) gradle

PROTOC = $(DCR) protobuf

##> Migrator
MIGRATOR = $(DCR) migrator
PHINX = $(MIGRATOR) migrations/migrator/vendor/bin/phinx
PHINX_CONFIG_ARG = --configuration migrations/migrator
PHINX_WORLD_CONFIG_ARG = $(PHINX_CONFIG_ARG)/phinx-world.php
PHINX_PLAYERS_CONFIG_ARG = $(PHINX_CONFIG_ARG)/phinx-players.php
PHINX_CONFIG_CONFIG_ARG = $(PHINX_CONFIG_ARG)/phinx-config.php

MIGRATOR_SEED_NAME :=
MIGRATOR_DB :=
##< Migrator

CLI_DIR = cli
CORE_DIR = core
GAME_DIR = servers/game
LOGIN_DIR = servers/login
ORCHESTRATOR_DIR = orchestrator
CLIENT_DIR = client

JAVA_GAME_DIR = ./$(GAME_DIR)
JAVA_GAME_DST_DIR = $(JAVA_GAME_DIR)/src/main/java
JAVA_GAME_PROTOS_DIR = $(JAVA_GAME_DIR)/src/main/protos
JAVA_GAME_TEST_DST_DIR = $(JAVA_GAME_DIR)/src/test/java
JAVA_GAME_TEST_SRC_DIR = $(JAVA_GAME_DIR)/src/test/protos

JAVA_LOGIN_DIR = ./$(LOGIN_DIR)
JAVA_LOGIN_PROTOS_DIR = $(JAVA_LOGIN_DIR)/src/main/protos
JAVA_LOGIN_DST_DIR = $(JAVA_LOGIN_DIR)/src/main/java

JAVA_ORCHESTRATOR_DIR = ./$(ORCHESTRATOR_DIR)
JAVA_ORCHESTRATOR_SHARED_PROTOS_DIR = $(JAVA_ORCHESTRATOR_DIR)/shared/src/main/protos
JAVA_ORCHESTRATOR_SHARED_DST_DIR = $(JAVA_ORCHESTRATOR_DIR)/shared/src/main/java

JAVA_CORE_DIR = ./$(CORE_DIR)
JAVA_CORE_PROTOS_DIR = $(JAVA_CORE_DIR)/src/main/protos
JAVA_CORE_DST_DIR = $(JAVA_CORE_DIR)/src/main/java

JAVA_CLIENT_DIR = ./$(CLIENT_DIR)
JAVA_CLIENT_PROTOS_DIR = $(JAVA_CLIENT_DIR)/src/main/protos
JAVA_CLIENT_DST_DIR = $(JAVA_CLIENT_DIR)/src/main/java

PHP_DIR = ./servers/webclient
PHP_DST_DIR = $(PHP_DIR)/protobuf

##> Debug
DEBUGGER_SOCKET_PORT = 5005
DEBUGGER_BUILD_GAME_JAR_PATH = build/libs/game-1.0.jar
DEBUGGER_BUILD_LOGIN_JAR_PATH = build/libs/login-1.0.jar
DEBUGGER_BUILD_CLI_JAR_PATH = build/libs/cli-1.0.jar
##< Debug

.PHONY: lint-fix
lint-fix: up
	$(GRADLE_TASK) ktlintFormat

.PHONY: install-ci
install-ci: servers/login/src/test/resources/private.pem ## Install the CI environment
	@$(MAKE) -i CI_SERVER=true build-proto migrate seed

.PHONY: help
help: ## Outputs this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: build-proto
build-proto: ## Builds protos for java and php
	@$(MAKE) -i build-core-proto
	@$(MAKE) -i build-game-proto
	@$(MAKE) -i build-login-proto
	@$(MAKE) -i build-orchestrator-proto

.PHONY: build-java-proto
build-java-proto:
	$(PROTOC) -I=./$(ARGS)/src/main/protos --java_out=./$(ARGS)/src/main/java ./$(ARGS)/src/main/protos/*.proto

.PHONY: build-test-proto
build-test-proto:
	$(PROTOC) -I=./$(ARGS)/src/test/protos --java_out=./$(ARGS)/src/test/java ./$(ARGS)/src/test/protos/*.proto

.PHONY: grpc
grpc:
	$(PROTOC) --plugin=protoc-gen-grpc-java=/usr/bin/protoc-gen-grpc-java -I=orchestrator/api/src/main/protos/rpc --java_out=orchestrator/api/src/main/java --grpc-java_out=orchestrator/api/src/main/java orchestrator/api/src/main/protos/rpc/*.proto

.PHONY: build-core-proto
build-core-proto:
	@echo "Generating java protos" && $(MAKE) build-java-proto ARGS="core"

.PHONY: build-client-proto
build-client-proto:
	@echo "Generating java protos" && $(MAKE) build-java-proto ARGS="client"

.PHONY: build-game-proto
build-game-proto: ## Builds game protos for java and php
	@echo "Generating game java protos" && $(MAKE) build-java-proto ARGS="servers/game"
	@echo "Generating test java protos" && $(MAKE) build-test-proto ARGS="servers/game"
	@echo "Generating php protos" && $(PROTOC) -I=$(JAVA_GAME_PROTOS_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_GAME_PROTOS_DIR)/*.proto

.PHONY: build-login-proto
build-login-proto: ## Builds login protos for java and php
	@echo "Generating login java protos" && $(MAKE) build-java-proto ARGS="servers/login"
	@echo "Generating php protos" && $(PROTOC) -I=$(JAVA_LOGIN_PROTOS_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_LOGIN_PROTOS_DIR)/*.proto

.PHONY: build-orchestrator-proto
build-orchestrator-proto: ## Builds orchestrator protos for java
	@echo "Generating shared protos" && $(PROTOC) -I=$(JAVA_ORCHESTRATOR_SHARED_PROTOS_DIR) --java_out=$(JAVA_ORCHESTRATOR_SHARED_DST_DIR) $(JAVA_ORCHESTRATOR_SHARED_PROTOS_DIR)/*.proto
	@echo "Generating shared protos" && $(PROTOC) -I=./servers/orchestrator/src/main/protos --java_out=./servers/orchestrator/src/main/java ./servers/orchestrator/src/main/protos/*.proto

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
	@$(MAKE) task-test

.PHONY: task-test
task-test:
	$(GRADLE_TASK) :servers:game:test

.PHONY: login-test
login-test:
	$(GRADLE_TASK) :servers:login:test
	@$(MAKE) login-cucumber

.PHONY: login-test
login-cucumber:
	@$(MAKE) -i seed
	$(GRADLE_TASK) :servers:login:cucumber

.PHONY: setup-tests
setup-tests: servers/game/src/test/resources/private.pem

.PHONY: build
build: up ## Builds all the projects
	$(GRADLE_TASK) build

.PHONY: build-game-debug
build-game-debug: ## Runs the build of the game project then start the debugger socket
	$(GRADLE_TASK) :servers:game:run -Ddebug_mode="true" -Ddebug_port="5006"

.PHONY: build-login
build-login:
	$(GRADLE_TASK) :servers:login:build

.PHONY: build-login-debug
build-login-debug: ## Runs the build of the login project then start the debugger socket
	$(GRADLE_TASK) :servers:login:run -Ddebug_mode="true"

.PHONY: build-cli
build-cli: up
	$(GRADLE_TASK) :cli:build

build-cli-debug:  ## Runs the build of the cli project then start the debugger socket
	$(GRADLE_TASK) :cli:run -Ddebug_mode="true" -Ddebug_port="5007"

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
	$(PHINX) migrate $(PHINX_CONFIG_CONFIG_ARG) -e development

.PHONY: seed
seed: migrations/migrator/vendor/autoload.php
	$(PHINX) seed:run $(PHINX_WORLD_CONFIG_ARG) -e development
	$(PHINX) seed:run $(PHINX_PLAYERS_CONFIG_ARG) -e development
	$(PHINX) seed:run $(PHINX_CONFIG_CONFIG_ARG) -e development

.PHONY: migration-players-create
migration-players-create: migrations/migrator/vendor/autoload.php
	$(PHINX) create $(COMMAND_ARGS) $(PHINX_PLAYERS_CONFIG_ARG)

.PHONY: migration-config-create
migration-config-create: migrations/migrator/vendor/autoload.php
	$(PHINX) create $(COMMAND_ARGS) $(PHINX_CONFIG_CONFIG_ARG)

.PHONY: migration-world-create
migration-world-create: migrations/migrator/vendor/autoload.php
	$(PHINX) create $(COMMAND_ARGS) $(PHINX_WORLD_CONFIG_ARG)

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

servers/login/src/test/resources/private.pem:
	@bash $(LOGIN_DIR)/bin/test/setup.sh

servers/webclient/vendor/autoload.php: servers/webclient/composer.lock
	composer install -d servers/webclient
	touch servers/webclient/vendor/autoload.php

.env:
	cp .env.dev .env
