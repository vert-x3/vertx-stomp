package io.vertx.ext.stomp;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static io.vertx.ext.stomp.StompOptions.DEFAULT_STOMP_PORT;
import static io.vertx.ext.stomp.StompOptions.DEFAULT_SUPPORTED_VERSIONS;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Check the behavior of {@link StompClientOptions}.
 *
 */
public class StompClientOptionsTest {

  @Test
  public void testDefaultConstructor(){
    StompClientOptions options = new StompClientOptions();
    assertFalse(options.isReuseAddress());
    assertEquals(options.getAcceptedVersions().indexOf(DEFAULT_SUPPORTED_VERSIONS.get(0)),
                  (options.getAcceptedVersions().size()-1));
    assertEquals( DEFAULT_STOMP_PORT, options.getPort() );
  }

  @Test
  public void testReusedAddressInConstructFromJsonObject(){
    String jsonWithoutReuseAddress = "{\"host\": \"127.0.0.1\"}";
    StompClientOptions options0  = new StompClientOptions(new JsonObject(jsonWithoutReuseAddress));
    assertFalse(options0.isReuseAddress());

    String jsonWithValidReuseAddress = "{\"reuseAddress\": true}";
    StompClientOptions options1  = new StompClientOptions(new JsonObject(jsonWithValidReuseAddress));
    assertTrue(options1.isReuseAddress());

    String jsonWithInvalidReuseAddress = "{\"reuseAddress\": \"none\"}";
    StompClientOptions options2  = new StompClientOptions(new JsonObject(jsonWithInvalidReuseAddress));
    assertFalse(options2.isReuseAddress() );
  }

  @Test
  public void testAcceptedVersionsInConstructFromJsonObject(){
    String jsonWithoutAcceptedVersions = "{\"host\": \"127.0.0.1\"}";
    StompClientOptions options0  = new StompClientOptions(new JsonObject(jsonWithoutAcceptedVersions));
    assertEquals(options0.getAcceptedVersions().
      indexOf(DEFAULT_SUPPORTED_VERSIONS.get(0)), (options0.getAcceptedVersions().size()-1));

    String jsonWithValidReuseAddress = "{\"acceptedVersions\": [\"1.2\"]}";
    StompClientOptions options1  = new StompClientOptions(new JsonObject(jsonWithValidReuseAddress));
    assertEquals( 1,options1.getAcceptedVersions().size());
    assertTrue(options1.getAcceptedVersions().contains("1.2"));
  }

}
