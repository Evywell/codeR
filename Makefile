.PHONY: build-proto
build-proto:
	bash servers/game/bin/build-protos.sh

.PHONY: bp
bp: ## alias of build-proto
	@make build-proto

.PHONY: test
test: ## Runs the :servers:game tests
	./gradlew :servers:game:test
