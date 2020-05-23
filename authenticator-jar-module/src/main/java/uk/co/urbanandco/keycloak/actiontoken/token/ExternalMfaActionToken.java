package uk.co.urbanandco.keycloak.actiontoken.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.keycloak.authentication.actiontoken.DefaultActionToken;

public class ExternalMfaActionToken extends DefaultActionToken {
  public static final String TOKEN_TYPE = "external-mfa";

  private static final String JSON_FIELD_APP_ID = "app-id";

  @JsonProperty(value = JSON_FIELD_APP_ID)
  private String applicationId;

  public ExternalMfaActionToken(String userId, int absoluteExpirationInSecs, String authenticationSessionId, String applicationId) {
    super(userId, TOKEN_TYPE, absoluteExpirationInSecs, null, authenticationSessionId);
    this.applicationId = applicationId;
  }

  private ExternalMfaActionToken() {
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

}
