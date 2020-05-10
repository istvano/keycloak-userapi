package uk.co.urbanandco.keycloak.service;

import java.util.UUID;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import uk.co.urbanandco.keycloak.sdk.api.UserApi;
import uk.co.urbanandco.keycloak.sdk.model.Credential;
import uk.co.urbanandco.keycloak.sdk.model.Success;

public class CredentialService {

  private final UserApi api;
  public CredentialService(UserApi api) {
    this.api = api;
  }

  public boolean isCredentialValid(final KeycloakSession session, final ComponentModel model, final RealmModel realm, UserModel user, CredentialInput credential) {
    try {

      Success result = api.validateUserCredential(UUID.fromString(user.getId()),
          new Credential().type(credential.getType())
              .credential(credential.getChallengeResponse()));
      return true;
    } catch(Exception e) {
      return false;
    }

  }
}
