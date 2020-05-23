package uk.co.urbanandco.keycloak.actiontoken.authenticator;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

@JBossLog
public class ExternalMfaAuthenticator implements Authenticator {

  public static final String DEFAULT_MFA_ID = "ext-mfa";

  @Override
  public void authenticate(AuthenticationFlowContext authenticationFlowContext) {

  }

  @Override
  public void action(AuthenticationFlowContext authenticationFlowContext) {

  }

  @Override
  public boolean requiresUser() {
    return true;
  }

  @Override
  public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel,
      UserModel userModel) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel,
      UserModel userModel) {
    //Nothing to do
  }

  @Override
  public void close() {

  }
}
