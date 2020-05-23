package uk.co.urbanandco.keycloak.storage.user;

import static uk.co.urbanandco.keycloak.storage.user.RestApiConstants.CONNECTION_URL;

import org.keycloak.common.util.MultivaluedHashMap;

public class RestApiConfig {
  private final MultivaluedHashMap<String, String> config;

  public RestApiConfig(MultivaluedHashMap<String, String> config) {
    this.config = config;
  }

  public String getApiEndpointUrl() {
    return config.getFirst(CONNECTION_URL);
  }
}
