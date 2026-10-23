---
name: writing-tests
description: >
  Testing patterns for Vert.x STOMP: test frameworks, async testing with
  vertx-unit, test locations, Docker broker containers, and how to run tests.
---

# Writing Tests

Vert.x STOMP uses JUnit 4 with vertx-unit for async coordination, AssertJ for assertions, Awaitility for blocking waits, and Testcontainers for broker infrastructure in integration tests. Tests are mandatory for contributions.

## Test Framework

- **JUnit 4** — Standard test framework (`@RunWith`, `@Before`, `@After`, `@Test`)
- **VertxUnitRunner** — JUnit 4 runner for async tests (`io.vertx.ext.unit.junit.VertxUnitRunner`)
- **TestContext** — Async test utility injected by the runner into `@Before`, `@After`, and `@Test` methods
- **Async** — Token obtained from `TestContext` to signal async completion
- **Awaitility** — Blocking wait utility used to bridge async operations in setup/teardown when `TestContext` is not available
- **AssertJ** — Fluent assertions (`assertThat(...)`)
- **Testcontainers** — Docker-based broker containers for integration tests

## Test Annotations and Extensions

### Standard Component Test Setup

```java
@RunWith(VertxUnitRunner.class)
public class MyTest {

  private Vertx vertx;
  private StompServer server;
  private List<StompClient> clients = new ArrayList<>();

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    server = StompServer.create(vertx).handler(StompServerHandler.create(vertx));
    server.listen().onComplete(context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    clients.forEach(StompClient::close);
    clients.clear();
    server.close().onComplete(context.asyncAssertSuccess(v ->
      vertx.close().onComplete(context.asyncAssertSuccess())
    ));
  }
}
```

`context.asyncAssertSuccess()` returns a handler that fails the test if the future fails, and signals completion when it succeeds. This is the preferred way to await async operations in `@Before` and `@After`.

## Async Test Patterns

### Pattern 1: `context.asyncAssertSuccess()` — assert and complete

Use when an async operation must succeed and you have nothing more to verify.

```java
@Test
public void testConnect(TestContext context) {
  StompClient client = StompClient.create(vertx);
  clients.add(client);
  client.connect().onComplete(context.asyncAssertSuccess());
}
```

### Pattern 2: `context.async()` — manual completion

Use when you need to run code after the future completes and then call `async.complete()` yourself.

```java
@Test
public void testMessageDelivery(TestContext context) {
  Async async = context.async();
  AtomicReference<Frame> received = new AtomicReference<>();

  StompClient subscriber = StompClient.create(vertx);
  clients.add(subscriber);
  subscriber.connect().onComplete(context.asyncAssertSuccess(conn ->
    conn.subscribe("/queue/test", frame -> {
      received.set(frame);
      async.complete();
    }).onComplete(context.asyncAssertSuccess(ignored -> {
      StompClient sender = StompClient.create(vertx);
      clients.add(sender);
      sender.connect().onComplete(context.asyncAssertSuccess(senderConn ->
        senderConn.send("/queue/test", Buffer.buffer("hello"))
          .onComplete(context.asyncAssertSuccess())
      ));
    }))
  ));
}
```

### Pattern 3: Chaining with `compose()`

Chain multiple async operations using `Future.compose()`.

```java
@Test
public void testSubscribeAndSend(TestContext context) {
  Async async = context.async();
  StompClient client = StompClient.create(vertx);
  clients.add(client);

  client.connect()
    .compose(conn -> conn.subscribe("/queue/test", frame -> async.complete()))
    .compose(ignored -> {
      StompClient sender = StompClient.create(vertx);
      clients.add(sender);
      return sender.connect();
    })
    .compose(senderConn -> senderConn.send("/queue/test", Buffer.buffer("hello")))
    .onComplete(context.asyncAssertSuccess());
}
```

### Pattern 4: Awaitility for polling assertions

When you need to wait for side effects to propagate (e.g., checking that a list was populated by an async callback), use Awaitility rather than sleeping.

```java
@Test
public void testFrameReceived(TestContext context) {
  List<Frame> received = new CopyOnWriteArrayList<>();

  StompClient client = StompClient.create(vertx);
  clients.add(client);
  client.connect().onComplete(context.asyncAssertSuccess(conn ->
    conn.subscribe("/queue/test", received::add)
      .onComplete(context.asyncAssertSuccess())
  ));

  // ... trigger send elsewhere ...

  await().atMost(10, TimeUnit.SECONDS).until(() -> received.size() == 1);
  assertThat(received.get(0).getBodyAsString()).isEqualTo("hello");
}
```

### Pattern 5: `context.asyncAssertFailure()` — expect failure

When the operation is expected to fail.

```java
@Test
public void testInvalidFrame(TestContext context) {
  StompClient client = StompClient.create(vertx, new StompClientOptions().setHost("invalid-host"));
  clients.add(client);
  client.connect().onComplete(context.asyncAssertFailure());
}
```

## Test Location

- **Unit tests** (no broker): `src/test/java/io/vertx/stomp/tests/` — frame parsing, options, headers
- **Component tests** (embedded server, no Docker): `src/test/java/io/vertx/stomp/tests/impl/` — server/client interactions, ACK/NACK, subscriptions, transactions, receipts
- **Multi-instance tests**: `src/test/java/io/vertx/stomp/tests/verticles/` — verticle-based multi-node scenarios
- **Integration tests** (require Docker): `src/test/java/io/vertx/stomp/tests/integration/` — classes named `*IT`, test against real brokers

## Integration Tests with Testcontainers

Integration tests use a JUnit 4 `@ClassRule` to start a Docker container once per test class.

```java
public class MyBrokerIT extends AbstractClientIT {

  @ClassRule
  public static final GenericContainer<?> container =
    new GenericContainer<>("my-broker-image:tag")
      .withExposedPorts(61613);

  @Override
  public StompClientOptions getOptions() {
    return new StompClientOptions()
      .setHost(container.getHost())
      .setPort(container.getMappedPort(61613));
  }

  @Override
  public StompClientOptions getOptionsWithSSL() {
    return null; // return null to skip SSL tests
  }
}
```

### Available broker containers

| Test class | Broker | Image |
|---|---|---|
| `ActivemqDockerIT` | Apache ActiveMQ | `rmohr/activemq:5.15.6-alpine` |
| `ArtemisHornetQDockerIT` | ActiveMQ Artemis (HornetQ protocol) | `vromero/activemq-artemis:2.6.3-alpine` |
| `ArtemisStompDockerIT` | ActiveMQ Artemis (STOMP protocol) | `vromero/activemq-artemis:2.6.3-alpine` |
| `RabbitMQDockerIT` | RabbitMQ | `rabbitmq:latest` |

## Running Tests

```bash
mvn test                                # Run unit and component tests (no Docker)
mvn verify                              # Run all tests including integration (requires Docker)
mvn test -Dtest=MyTest                  # Run specific test class
mvn test -Dtest=MyTest#testMethod       # Run specific test method
```

## Assertions

Use AssertJ fluent assertions:

```java
import static org.assertj.core.api.Assertions.assertThat;

assertThat(frame.getCommand()).isEqualTo(Command.MESSAGE);
assertThat(frame.getHeader("destination")).isEqualTo("/queue/test");
assertThat(received).hasSize(1);
```

## Test Requirements

- **All new features must include tests**
- **Tests must clean up resources** — close clients in `@After`, close server and Vertx last
- **Prefer `context.asyncAssertSuccess()`** over `AsyncLock` for async coordination in setup/teardown
- **Integration tests need Docker** — ensure Docker is running before executing `mvn verify`

## Common Pitfalls

1. **Forgetting to complete async tests** — Always call `async.complete()` or use `context.asyncAssertSuccess()` / `context.asyncAssertFailure()`
2. **Closing resources out of order** — Close clients before closing the server; close the server before closing Vertx
3. **Race conditions in callback assertions** — Use `Awaitility.await()` to poll, not `Thread.sleep()`
4. **Mixing thread-unsafe collections with async callbacks** — Use `CopyOnWriteArrayList` or `AtomicReference` when collecting results from callbacks
