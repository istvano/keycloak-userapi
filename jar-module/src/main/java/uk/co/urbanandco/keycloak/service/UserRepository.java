package uk.co.urbanandco.keycloak.service;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import uk.co.urbanandco.keycloak.sdk.api.UserApi;
import uk.co.urbanandco.keycloak.sdk.model.User;
import uk.co.urbanandco.keycloak.sdk.model.UserNameLookup;
import uk.co.urbanandco.keycloak.storage.user.RestUserAdapter;

public class UserRepository {

  private final UserApi api;

  public UserRepository(UserApi api) {
    this.api = api;
  }

  public UserModel find(final KeycloakSession session, final ComponentModel model, final RealmModel realm, final String userName) {

    User user = api.getUserByUsername(new UserNameLookup().username(userName));
    return RestUserAdapter.builder()
        .session(session)
        .storageProviderModel(model)
        .realm(realm)
        .user(user)
        .build();
  }
}
