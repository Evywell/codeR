UNAME := $(shell uname | tr '[:upper:]' '[:lower:]')

ifeq ($(UNAME), darwin)
    OS := osx
else
	OS := linux
endif

GAME_DIR := servers/game

PROTOC_LIB_NAME := protoc-3.14.0-$(OS)-x86_64
PROTOC_ARCHIVE_NAME := $(PROTOC_LIB_NAME).zip
PROTOC_LIB_PATH := $(GAME_DIR)/libs/$(PROTOC_LIB_NAME)
PROTOC_LIB_BINARY := $(PROTOC_LIB_PATH)/bin/protoc

JAVA_DIR := ./servers/game
JAVA_DST_DIR := $(JAVA_DIR)/src/main/java
JAVA_SRC_DIR := $(JAVA_DIR)/src/main/protos
JAVA_TEST_DST_DIR := $(JAVA_DIR)/src/test/java
JAVA_TEST_SRC_DIR := $(JAVA_DIR)/src/test/protos

PHP_DIR := ./servers/webclient
PHP_DST_DIR := $(PHP_DIR)/protobuf

.PHONY: help
help: ## Outputs this help message
	@grep -E '(^[a-zA-Z0-9_-]+:.*?##.*$$)|(^##)' ${MAKEFILE_LIST} | awk 'BEGIN {FS = ":.*?## "}{printf "${GREEN}%-23s${RESET} %s\n", $$1, $$2}' | sed -e 's/\[32m##/[33m/'

.PHONY: build-proto
build-proto:
	$(PROTOC_LIB_BINARY) -I=$(JAVA_SRC_DIR) --java_out=$(JAVA_DST_DIR) $(JAVA_SRC_DIR)/*.proto
	$(PROTOC_LIB_BINARY) -I=$(JAVA_TEST_SRC_DIR) --java_out=$(JAVA_TEST_DST_DIR) $(JAVA_TEST_SRC_DIR)/*.proto
	@$(PROTOC_LIB_BINARY) -I=$(JAVA_SRC_DIR) --php_out=$(PHP_DST_DIR) $(JAVA_SRC_DIR)/*.proto

.PHONY: bp
bp: ## alias of build-proto
	@make build-proto

.PHONY: start
start: ## Runs the :servers:game run
	./gradlew :servers:game:run

.PHONY: test
test: ## Runs the :servers:game tests
	@bash $(GAME_DIR)/bin/test/setup.sh
	@make bp
	./gradlew :servers:game:test

.PHONY: build
build: ## Builds the :servers:game and :servers:client projects
	./gradlew :servers:game:build
	./gradlew :servers:client:build

.PHONY: server
server: ## Launches the game server
	./gradlew :servers:game:run

.PHONY: client
client: ## Launches the game client
	./gradlew :servers:client:run

.PHONY: server
server: ## Launches the game server
	./gradlew :servers:game:run

.PHONY: install-protobuf
install-protobuf: ## Download the 3.14.0 version of protoc and install it in libs folder
	curl https://github.com/protocolbuffers/protobuf/releases/download/v3.14.0/$(PROTOC_ARCHIVE_NAME) -Lo tmp/$(PROTOC_ARCHIVE_NAME)
	unzip tmp/$(PROTOC_ARCHIVE_NAME) -d tmp/$(PROTOC_LIB_NAME)
	mkdir -p $(PROTOC_LIB_PATH) && rm -rf $(PROTOC_LIB_PATH)
	mv tmp/$(PROTOC_LIB_NAME) $(PROTOC_LIB_PATH)
	rm tmp/$(PROTOC_ARCHIVE_NAME)
