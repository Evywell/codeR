# Agent Guidelines for codeR

## Build/Test Commands
- Build all: `./gradlew build` or `make build`
- Build specific module: `./gradlew :servers:game:build`
- Run all tests: `./gradlew test`
- Run single test: `./gradlew :servers:game:test --tests "TestClassName.testMethodName"`
- Lint fix: `make lint-fix` (runs ktlintFormat)
- Run server: `./gradlew :servers:game:run` or `make game-server-dev`
- Database migrations: `make migrate` (requires Docker dependencies)

## Code Style (Kotlin)
- **Follow Kotlin best practices**: Adhere to official Kotlin coding conventions and idioms as documented in the [Kotlin style guide](https://kotlinlang.org/docs/coding-conventions.html).
- **Naming**: PascalCase for classes, camelCase for functions/variables. Interfaces use `Interface` suffix (e.g., `OpcodeFunctionInterface`). Custom exceptions extend `RuntimeException` with descriptive names (e.g., `CharacterNotFoundException`).
- **Imports**: Group by domain, import individually (no wildcards), standard library last.
- **Types**: Explicit types for public function parameters and return types. Use type inference for local variables when obvious. Expression functions use `=` syntax.
- **Error handling**: Prefer exceptions over Result types. Document thrown exceptions with `@throws` in KDoc. Use try-catch for Java interop (SQL, etc.). Nullable returns for optional failures.
- **Documentation**: Minimal KDoc - use `@throws` tags and self-documenting names over extensive comments.
- **Constructors**: Use primary constructor with `private val` for encapsulation. Trailing commas in multi-line parameter lists.
- **Testing**: JUnit Jupiter framework. Test helper classes in `test/*/tools/` packages.

## Architecture
- Multi-module Gradle project: `:core`, `:servers:game`, `:gateway`, `:utilities`, `:world:service`
- Three databases: config (architecture), players (dynamic data), world (game content)
- Proto-based communication (gRPC)
