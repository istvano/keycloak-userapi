package uk.co.urbanandco.keycloak.actiontoken.authenticator;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.Time;
import org.keycloak.events.Errors;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;
import org.keycloak.sessions.AuthenticationSessionModel;
import uk.co.urbanandco.keycloak.actiontoken.token.ExternalMfaActionToken;
import uk.co.urbanandco.keycloak.representations.idm.OAuth2TokenErrorRepresentation;

@JBossLog
public class ExternalMfaAuthenticator implements Authenticator {

  public static final String DEFAULT_MFA_ID = "ext-mfa";

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    if (!configuredFor(context.getSession(), context.getRealm(), context.getUser())) {
      if (context.getExecution().isConditional()) {
        context.attempted();
      } else if (context.getExecution().isRequired()) {
        context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
        Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(),
            "invalid_grant",
            "Invalid user credentials");
        context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
      }
    } else {
      Response challengeResponse = mfaErrorResponse(Response.Status.FORBIDDEN.getStatusCode(),
          "mfa_required",
          "External Multi-factor authentication required",
          generateMfaToken(context));
      context.failure(AuthenticationFlowError.CREDENTIAL_SETUP_REQUIRED, challengeResponse);
    }
  }

  public Response errorResponse(int status, String error, String errorDescription) {
    OAuth2ErrorRepresentation errorRep = new OAuth2ErrorRepresentation(error, errorDescription);
    return Response.status(status).entity(errorRep).type(MediaType.APPLICATION_JSON_TYPE).build();
  }

  public Response mfaErrorResponse(int status, String error, String errorDescription, String token) {
    OAuth2TokenErrorRepresentation errorRep = new OAuth2TokenErrorRepresentation(error, errorDescription, token);
    return Response.status(status).entity(errorRep).type(MediaType.APPLICATION_JSON_TYPE).build();
  }

  public String generateMfaToken(AuthenticationFlowContext context) {

    int validityInSecs = context.getRealm().getActionTokenGeneratedByUserLifespan();
    int absoluteExpirationInSecs = Time.currentTime() + validityInSecs;
    final AuthenticationSessionModel authSession = context.getAuthenticationSession();
    final String clientId = authSession.getClient().getClientId();

    return new ExternalMfaActionToken(
        context.getUser().getId(),
        absoluteExpirationInSecs,
        clientId,
        getApplicationId(context)
    ).serialize(
        context.getSession(),
        context.getRealm(),
        context.getUriInfo()
    );

  }

  public String getApplicationId(AuthenticationFlowContext context) {
    return context.getAuthenticatorConfig() != null ? context.getAuthenticatorConfig().getConfig().get(ExternalMfaAuthenticatorFactory.CONFIG_APPLICATION_ID) : DEFAULT_MFA_ID;
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
