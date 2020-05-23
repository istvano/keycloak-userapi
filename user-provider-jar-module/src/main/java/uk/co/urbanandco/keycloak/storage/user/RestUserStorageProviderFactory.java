package uk.co.urbanandco.keycloak.storage.user;

import static uk.co.urbanandco.keycloak.storage.user.RestApiConstants.CONNECTION_URL;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.Config.Scope;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.EnvironmentDependentProviderFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.storage.UserStorageProviderFactory;
import org.keycloak.storage.UserStorageProviderModel;
import uk.co.urbanandco.keycloak.sdk.api.UserApi;
import uk.co.urbanandco.keycloak.sdk.invoker.ApiClient;
import uk.co.urbanandco.keycloak.service.CredentialService;
import uk.co.urbanandco.keycloak.service.UserRepository;

@JBossLog
@AutoService(UserStorageProviderFactory.class)
public class RestUserStorageProviderFactory implements UserStorageProviderFactory<RestUserStorageProvider>,
    ServerInfoAwareProviderFactory, EnvironmentDependentProviderFactory {

  public static final String PROVIDER_NAME = "rest-user-provider";
  protected static final List<ProviderConfigProperty> CONFIG_META_DATA;

  static {
    CONFIG_META_DATA = ProviderConfigurationBuilder.create()
        .property().name(CONNECTION_URL)
        .type(ProviderConfigProperty.STRING_TYPE)
        .label("Rest API endpoint url")
        .defaultValue("http://127.0.0.1:4010")
        .helpText("Rest endpoint main url")
        .add()
        .build();
  }

  private static UserRepository createUserRepository(UserApi api) {
    return new UserRepository(api);
  }

  private static CredentialService createCredentialService(UserApi api) {
    return new CredentialService(api);
  }

  private static UserApi buildUserApiClient(ComponentModel componentModel) {
    RestApiConfig config = new RestApiConfig(componentModel.getConfig());
    return new ApiClient().setBasePath(config.getApiEndpointUrl()).buildClient(UserApi.class);
  }

  @Override
  public RestUserStorageProvider create(KeycloakSession keycloakSession,
      ComponentModel componentModel) {
    log.debug("Create new Rest Storage Provider");
    UserApi api = buildUserApiClient(componentModel);
    return new RestUserStorageProvider(keycloakSession, new UserStorageProviderModel(componentModel), createUserRepository(api), createCredentialService(api));
  }

  @Override
  public String getId() {
    return PROVIDER_NAME;
  }

  @Override
  public String getHelpText() {
    return "Rest API User Storage Provider";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return CONFIG_META_DATA;
  }

  @Override
  public void validateConfiguration(KeycloakSession session, RealmModel realm,
      ComponentModel config) throws ComponentValidationException {

    log.debug("Check if configuration is valid");
    if (Strings.isNullOrEmpty(config.getConfig().getFirst(CONNECTION_URL))) {
      throw new ComponentValidationException("Rest API url must be specified");
    }
  }

  /**
   * A Map of String can be generated here which we can show on the operational info page
   * @return
   */
  @Override
  public Map<String, String> getOperationalInfo() {
    Map<String, String> result = new LinkedHashMap<>();
    result.put("Info", "Rest API Service Provider");
    return result;
  }

  /**
   * This is a method of the EnvironmentDependentProviderFactory class.
   * It is used to signal if an OS or other project specific servce is available and enable / disable
   * the Provider based on this flag.
   * @return
   */
  @Override
  public boolean isSupported() {
    return true;
  }

  /**
   * Place any code here that should be created when the factory is created
   * see LDAPStorageProviderFactory
   * @param config
   */
  @Override
  public void init(Scope config) {
    //TODO
  }

  /**
   * Place any clean up code here
   * see LDAPStorageProviderFactory
   */

  @Override
  public void close() {

  }
}