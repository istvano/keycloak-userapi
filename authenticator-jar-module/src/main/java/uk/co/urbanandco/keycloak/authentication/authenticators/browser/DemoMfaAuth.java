package uk.co.urbanandco.keycloak.authentication.authenticators.browser;

import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import javax.ws.rs.core.Response;

public class DemoMfaAuth implements Authenticator {

  protected Response challenge(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
    LoginFormsProvider forms = context.form();
    return forms.createForm("demo-mfa.ftl");
  }

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    MultivaluedMap<String, String> formData = new MultivaluedMapImpl<>();
    Response challengeResponse = challenge(context, formData);
    context.challenge(challengeResponse);
    //send an API call to your mfa manager -> ? error -> show error to the user
  }

  protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
    //validate otp code
    return true;
  }

  @Override
  public void action(AuthenticationFlowContext context) {
    MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
    if (formData.containsKey("cancel")) {
      context.cancelLogin();
      return;
    }
    if (!validateForm(context, formData)) {
      return;
    }
    context.success();
  }

  @Override
  public boolean requiresUser() {
    return false;
  }

  @Override
  public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel,
      UserModel userModel) {
    return false;
  }

  @Override
  public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel,
      UserModel userModel) {

  }

  @Override
  public void close() {

  }
}
