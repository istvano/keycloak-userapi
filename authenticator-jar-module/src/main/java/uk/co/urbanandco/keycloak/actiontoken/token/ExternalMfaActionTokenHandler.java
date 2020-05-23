package uk.co.urbanandco.keycloak.actiontoken.token;

import static org.keycloak.services.resources.LoginActionsService.AUTHENTICATE_PATH;

import javax.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.TokenVerifier.Predicate;
import org.keycloak.authentication.AuthenticationProcessor;
import org.keycloak.authentication.actiontoken.AbstractActionTokenHander;
import org.keycloak.authentication.actiontoken.ActionTokenContext;
import org.keycloak.events.Errors;
import org.keycloak.events.EventType;
import org.keycloak.services.messages.Messages;

@JBossLog
public class ExternalMfaActionTokenHandler extends
    AbstractActionTokenHander<ExternalMfaActionToken> {

  public static final String INITIATED_BY_ACTION_TOKEN_EXT_APP = "INITIATED_BY_ACTION_TOKEN_MFA";

  public ExternalMfaActionTokenHandler() {
    super(
        ExternalMfaActionToken.TOKEN_TYPE,
        ExternalMfaActionToken.class,
        Messages.INVALID_REQUEST,
        EventType.EXECUTE_ACTION_TOKEN,
        Errors.INVALID_REQUEST
    );
  }

  @Override
  public Response handleToken(ExternalMfaActionToken token,
      ActionTokenContext<ExternalMfaActionToken> tokenContext) {
    tokenContext.getAuthenticationSession().setAuthNote(INITIATED_BY_ACTION_TOKEN_EXT_APP, "true");
    return tokenContext.processFlow(true, AUTHENTICATE_PATH, tokenContext.getRealm().getDirectGrantFlow(), null, new AuthenticationProcessor());
  }

  @Override
  public Predicate<? super ExternalMfaActionToken>[] getVerifiers(
      ActionTokenContext<ExternalMfaActionToken> tokenContext) {
    return new Predicate[0];
  }

}
