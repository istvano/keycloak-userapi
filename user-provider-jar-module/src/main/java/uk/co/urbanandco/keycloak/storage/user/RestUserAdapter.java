package uk.co.urbanandco.keycloak.storage.user;

import java.util.Objects;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.InMemoryUserAdapter;
import uk.co.urbanandco.keycloak.sdk.model.User;

public class RestUserAdapter extends InMemoryUserAdapter {

  public static final String REMOTE_ID = "remoteId";
  private final User user;

  public RestUserAdapter(KeycloakSession session,
      RealmModel realm,
      ComponentModel storageProviderModel,
      User user) {

    super(session, realm, new StorageId(storageProviderModel.getId(), user.getUsername()).getId());

    setSingleAttribute(REMOTE_ID, user.getId().toString());
    setFirstName(user.getFirstName());
    setLastName(user.getLastName());
    setEmail(user.getEmail());
    setEnabled(true);
    setEmailVerified(true);
    setUsername(user.getUsername());
    addDefaults();

    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    RestUserAdapter that = (RestUserAdapter) o;
    return Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), user);
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
