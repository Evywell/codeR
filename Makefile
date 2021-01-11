UNAME := $(shell uname | sed -e 's/\(.*\)/\L\1/')
PROTOC_LIB_NAME := protoc-3.14.0-$(UNAME)-x86_64
PROTOC_ARCHIVE_NAME := $(PROTOC_LIB_NAME).zip

.PHONY: help
help: ## Outputs this help message
	@grep -E '(^[a-zA-Z0-9_-]+:.*?##.*$$)|(^##)' ${MAKEFILE_LIST} | awk 'BEGIN {FS = ":.*?## "}{printf "${GREEN}%-23s${RESET} %s\n", $$1, $$2}' | sed -e 's/\[32m##/[33m/'

.PHONY: build-proto
build-proto:
	bash servers/game/bin/build-protos.sh

.PHONY: bp
bp: ## alias of build-proto
	@make build-proto

.PHONY: test
test: ## Runs the :servers:game tests
	./gradlew :servers:game:test

.PHONY: install-protobuf
install-protobuf: ## Download the 3.14.0 version of protoc and install it in libs folder
	wget -P tmp https://github.com/protocolbuffers/protobuf/releases/download/v3.14.0/$(PROTOC_ARCHIVE_NAME)
	unzip tmp/$(PROTOC_ARCHIVE_NAME) -d tmp/$(PROTOC_LIB_NAME)
	mkdir -p servers/game/libs/
	mv tmp/$(PROTOC_LIB_NAME) servers/game/libs/$(PROTOC_LIB_NAME)
	rm tmp/$(PROTOC_ARCHIVE_NAME)
