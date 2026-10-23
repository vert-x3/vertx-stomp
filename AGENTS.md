# Agent Guidelines - vertx-stomp

Instructions for AI coding agents working in this repository.
Checkout relevant `.agents/skills/` to accomplish specific tasks.

## Build & Verify Commands

```bash
mvn test-compile         # compile code and tests
mvn test                 # run unit and component tests (no Docker required)
mvn verify               # run all tests including integration tests (requires Docker)
mvn spotless:check       # verify formatting
mvn spotless:apply       # auto-fix formatting
```

## Project Structure

This is a **single-module** Maven project (`io.vertx:vertx-stomp`).

### Directory Layout

- `src/main/java/io/vertx/ext/stomp/` — Public API interfaces and classes (`StompServer`, `StompClient`, `StompServerHandler`, `StompClientConnection`, `Frame`, `Frames`, `Destination`, `Command`, …)
- `src/main/java/io/vertx/ext/stomp/impl/` — Internal implementations (not exported)
- `src/main/java/io/vertx/ext/stomp/utils/` — Utility classes (`Headers`, `Server`)
- `src/main/generated/` — Auto-generated `*Converter.java` classes produced by Vert.x codegen (do not edit manually)
- `src/main/asciidoc/` — Documentation source
- `src/test/java/io/vertx/stomp/tests/` — Unit and component tests (no Docker required)
- `src/test/java/io/vertx/stomp/tests/impl/` — Component tests exercising server/client interactions
- `src/test/java/io/vertx/stomp/tests/verticles/` — Multi-instance verticle tests
- `src/test/java/io/vertx/stomp/tests/integration/` — Integration tests against real brokers via Docker (`*IT.java`)
- `module-info.java` — Defines module exports and dependencies

### Code Generation

Options classes (`StompClientOptions`, `StompServerOptions`, `BridgeOptions`, `FrameConverter`) are processed by Vert.x codegen, which generates `*Converter.java` classes in `src/main/generated/`.
The module is declared with `@ModuleGen` in `package-info.java`.
Do not edit generated files manually; modify the source class or its annotations instead.

## General Coding Rules

These rules apply when **writing or modifying code**. Code review is the checkpoint where compliance is verified.

### Logging

In production code, use the Vert.x internal logger, never SLF4J, Log4j, or `java.util.logging` directly.

```java
import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
```

Vert.x internal logging API doesn't support parameter placeholders.
Check the active level before debug or trace logging.

```java
if (logger.isDebugEnabled()) {
  logger.debug("Processing frame: " + frame);
}
```

### Async Patterns

Use Vert.x `Future<T>` and `Promise<T>` throughout. Do not use raw callbacks or `CompletableFuture` in production code.

### API Design

- Public contracts are interfaces in `io.vertx.ext.stomp`
- Implementations go in the `impl/` subpackage
- Annotate public API interfaces and methods with `@VertxGen` for code-generation support
- Options classes use `@DataObject` with generated JSON converters
- Expose construction via static factory methods, not constructors

### Module Boundaries

`module-info.java` governs exports.
Internal packages are not exported; do not widen exports without discussion.

### Copyright Header

New Java files must include the dual-license header matching existing files:

```java
/*
 *  Copyright (c) 2011-2025 The original author or authors
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
```

## Testing Guidelines

For comprehensive testing patterns and examples, see `.agents/skills/writing-tests/SKILL.md`.

### Test Framework

- Use **JUnit 4** for all tests
- Async tests use **vertx-unit** (`VertxUnitRunner`, `TestContext`, `Async`)
- Blocking coordination in `@Before`/`@After` uses **Awaitility**
- Integration tests require **Docker** for broker containers via **Testcontainers**

### Test Patterns

```java
@RunWith(VertxUnitRunner.class)
public class MyTest {

  private Vertx vertx;
  private StompServer server;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    server = StompServer.create(vertx).handler(StompServerHandler.create(vertx));
    server.listen().onComplete(context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    server.close().onComplete(context.asyncAssertSuccess(v ->
      vertx.close().onComplete(context.asyncAssertSuccess())
    ));
  }

  @Test
  public void testSomething(TestContext context) {
    Async async = context.async();
    // ... exercise code ...
    // complete when done:
    async.complete();
  }
}
```

### Test Location

- **Unit tests** (no broker required): `src/test/java/io/vertx/stomp/tests/` and `src/test/java/io/vertx/stomp/tests/impl/`
- **Multi-instance tests**: `src/test/java/io/vertx/stomp/tests/verticles/`
- **Integration tests** (require Docker): `src/test/java/io/vertx/stomp/tests/integration/` — classes named `*IT`

### Running Tests

```bash
mvn test                                # Run unit and component tests
mvn verify                              # Run all tests including integration (requires Docker)
mvn test -Dtest=MyTest                  # Run specific test class
mvn test -Dtest=MyTest#testMethod       # Run specific test method
```

### Test Requirements

- All new features must include tests
- Tests must clean up resources (connections, clients, server, Vertx instance)
- Use `@After` (or `@AfterClass`) to close clients, server, and Vertx

## Development Workflow

### Incremental Development

When making changes:
1. Compile frequently: `mvn compile`
2. Run affected tests: `mvn test -Dtest=TestClass`
3. Run full build before PR: `mvn clean verify`

### Build Optimization

```bash
# Skip tests during development
mvn compile -DskipTests

# Compile tests without running them
mvn test-compile
```

## Specialized Skills

When performing specific tasks, read the relevant skill file for detailed guidance:

- **Writing tests** - Read `.agents/skills/writing-tests/SKILL.md` when creating or modifying tests

## Contribution Process

- All commits must be signed off: `git commit -s` (DCO)
- Commit messages should end with: `Assisted-by: [Provider] [Model-Family] ([Version/ID])` (replace placeholders)
- Contributors must have signed the [Eclipse Contributor Agreement (ECA)](https://www.eclipse.org/legal/ECA.php)

See [CONTRIBUTING.md](CONTRIBUTING.md) for the full contribution workflow.

## Code Review Guidelines

### Verify

- General coding rules above are followed
- Test coverage is present; async tests use `VertxUnitRunner` and `TestContext`
- No breaking changes to public interfaces without prior discussion

### Do Not Comment On

- Patterns already used consistently throughout the codebase
