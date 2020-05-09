package uk.co.urbanandco.keycloak.storage.user;

import com.google.auto.service.AutoService;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

@JBossLog
@AutoService(UserStorageProviderFactory.class)
public class RestUserStorageProviderFactory implements UserStorageProviderFactory<RestUserStorageProvider> {

  @Override
  public RestUserStorageProvider create(KeycloakSession keycloakSession,
      ComponentModel componentModel) {
    return new RestUserStorageProvider();
  }

  @Override
  public String getId() {
    return "rest-user-provider";
  }
}