include Makefile.cmds.mk

# @todo: replace this by variables
SUPPORTED_COMMANDS := migration-players-create migration-world-create migration-config-create seed-create
SUPPORTS_MAKE_ARGS := $(findstring $(firstword $(MAKECMDGOALS)), $(SUPPORTED_COMMANDS))
ifneq "$(SUPPORTS_MAKE_ARGS)" ""
  COMMAND_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  $(eval $(COMMAND_ARGS):;@:)
endif

##> Migrator
MIGRATOR = ${DOCKER_COMPOSE_RUN_CMD} migrator
PHINX = $(MIGRATOR) migrations/migrator/vendor/bin/phinx
PHINX_CONFIG_ARG = --configuration migrations/migrator
PHINX_WORLD_CONFIG_ARG = $(PHINX_CONFIG_ARG)/phinx-world.php
PHINX_PLAYERS_CONFIG_ARG = $(PHINX_CONFIG_ARG)/phinx-players.php
PHINX_CONFIG_CONFIG_ARG = $(PHINX_CONFIG_ARG)/phinx-config.php

MIGRATOR_SEED_NAME :=
MIGRATOR_DB :=
##< Migrator

.PHONY: help
help: ## Outputs this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: lint-fix
lint-fix:
	${GRADLE_CMD} ktlintFormat

.PHONY: build
build: ## Builds all the projects
	${GRADLE_CMD} build

.PHONY: game-server-dev
game-server-dev: ## Runs the game server using gradle
	${GRADLE_CMD} :servers:game:run

.PHONY: game-server-test
game-server-test: ## Runs the game server tests
	${GRADLE_CMD} :servers:game:test

game-server-build:
	${GRADLE_CMD} :servers:game:build

.PHONY: game-server
game-server: game-server-build start-dependencies ## Builds and run the game server jar with minimal configuration
	rm -rf servers/game/build/distributions/game-1.0
	cd servers/game/build/distributions; unzip game-1.0.zip
	cd servers/game/build/distributions/game-1.0; GAME_OPTS="-Dmysql_game.host=127.0.0.1 -Dmysql_game.tcp.3306=33060" ./bin/game

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
migration-status: start-dependencies
	$(PHINX) status --configuration migrator/phinx.php

.PHONY: install-ci
install-ci: servers/login/src/test/resources/private.pem

start-dependencies: .env ## Runs all the docker containers
	${DOCKER_COMPOSE_CMD} up -d

migrations/migrator/vendor/autoload.php: start-dependencies
	$(MIGRATOR) composer install -d migrations/migrator

servers/login/src/test/resources/private.pem:
	@bash servers/login/bin/test/setup.sh

servers/webclient/vendor/autoload.php: servers/webclient/composer.lock
	composer install -d servers/webclient
	touch servers/webclient/vendor/autoload.php

composer: servers/webclient/vendor/autoload.php

.env:
	cp .env.dev .env
