.PHONY: build-proto
build-proto:
	bash servers/game/bin/build-protos.sh

.PHONY: bp
bp: ## alias of build-proto
	@make build-proto
