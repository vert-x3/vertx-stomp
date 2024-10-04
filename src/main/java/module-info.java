

module io.vertx.stomp {

  requires static io.vertx.core.logging;
  requires static io.vertx.docgen;
  requires static io.vertx.codegen.api;
  requires static io.vertx.codegen.json;

  requires io.vertx.auth.common;
  requires io.vertx.core;
  requires io.vertx.eventbusbridge;

  exports io.vertx.ext.stomp;


}
