package uk.co.urbanandco.keycloak.service;

import static uk.co.urbanandco.keycloak.storage.user.RestUserAdapter.REMOTE_ID;

import java.util.UUID;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import uk.co.urbanandco.keycloak.sdk.api.UserApi;
import uk.co.urbanandco.keycloak.sdk.model.Credential;
import uk.co.urbanandco.keycloak.sdk.model.Success;

@JBossLog
public class CredentialService {

  private final UserApi api;
  public CredentialService(UserApi api) {
    this.api = api;
  }

  public boolean isCredentialValid(final KeycloakSession session, final ComponentModel model, final RealmModel realm, UserModel user, CredentialInput credential) {
    try {
      String id = user.getFirstAttribute(REMOTE_ID);
      log.debugv("Checking credential for {0}", id);
      Success result = api.validateUserCredential(UUID.fromString(id),
          new Credential().type(credential.getType())
              .credential(credential.getChallengeResponse()));
      return true;
    } catch(Exception e) {
      log.error("Credential is incorrect", e);
      return false;
    }

  }
}
