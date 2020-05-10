package uk.co.urbanandco.keycloak.storage.user;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import uk.co.urbanandco.keycloak.sdk.model.User;

public class RestUserAdapter extends AbstractUserAdapter {

  private final User user;

  public RestUserAdapter(KeycloakSession session,
      RealmModel realm,
      ComponentModel storageProviderModel,
      User user) {
    super(session, realm, storageProviderModel);
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private KeycloakSession session;
    private RealmModel realm;
    private ComponentModel storageProviderModel;
    private User user;

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder session(KeycloakSession session) {
      this.session = session;
      return this;
    }

    public Builder realm(RealmModel realm) {
      this.realm = realm;
      return this;
    }

    public Builder storageProviderModel(ComponentModel storageProviderModel) {
      this.storageProviderModel = storageProviderModel;
      return this;
    }

    public RestUserAdapter build() {
      return new RestUserAdapter(session, realm, storageProviderModel, user);
    }
  }
}
