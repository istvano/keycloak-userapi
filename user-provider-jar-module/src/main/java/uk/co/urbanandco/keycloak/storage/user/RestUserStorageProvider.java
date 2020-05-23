package uk.co.urbanandco.keycloak.storage.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.UserStorageProviderModel;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import lombok.extern.jbosslog.JBossLog;
import uk.co.urbanandco.keycloak.service.CredentialService;
import uk.co.urbanandco.keycloak.service.UserRepository;

@JBossLog
public class RestUserStorageProvider implements UserStorageProvider,
    UserLookupProvider, UserQueryProvider, CredentialInputUpdater, CredentialInputValidator {

  protected static final Set<String> supportedCredentialTypes = new HashSet<>();

  private final UserStorageProviderModel config;
  private final KeycloakSession session;
  private final UserRepository repository;
  private final CredentialService credentialService;

  static {
    supportedCredentialTypes.add(PasswordCredentialModel.TYPE);
  }

  public RestUserStorageProvider(KeycloakSession session, UserStorageProviderModel config, UserRepository repository, CredentialService credentialService){
    this.session = session;
    this.config = config;
    this.repository = repository;
    this.credentialService = credentialService;
  }


  @Override
  public boolean supportsCredentialType(String credentialType) {
    return supportedCredentialTypes.contains(credentialType);
  }

  @Override
  public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
    return !supportsCredentialType(credentialType);
  }

  @Override
  public boolean isValid(RealmModel realmModel, UserModel userModel,
      CredentialInput credentialInput) {
    if (userModel == null) {
      log.warn("Unable to check the credentials of a non existent user");
      return false;
    }
    if (!supportsCredentialType(credentialInput.getType())) {
      log.warn("Invalid credential type");
      return false;
    }

    return credentialService.isCredentialValid(session, config, realmModel, userModel, credentialInput);
  }

  @Override
  public boolean updateCredential(RealmModel realmModel, UserModel userModel,
      CredentialInput credentialInput) {
    return false;
  }

  @Override
  public void disableCredentialType(RealmModel realmModel, UserModel userModel, String s) {

  }

  @Override
  public Set<String> getDisableableCredentialTypes(RealmModel realmModel, UserModel userModel) {
    return (Set<String>) Collections.EMPTY_SET;
  }

  @Override
  public void close() {

  }

  @Override
  public UserModel getUserById(String userId, RealmModel realmModel) {
    StorageId id = new StorageId(userId);
    String userName = id.getExternalId();
    return getUserByUsername(userName, realmModel);
  }

  @Override
  public UserModel getUserByUsername(String userName, RealmModel realmModel) {
    return repository.find(session, config, realmModel, userName);
  }

  @Override
  public UserModel getUserByEmail(String email, RealmModel realmModel) {
    return null;
  }

  @Override
  public int getUsersCount(RealmModel realmModel) {
    return 0;
  }

  @Override
  public List<UserModel> getUsers(RealmModel realmModel) {
    return null;
  }

  @Override
  public List<UserModel> getUsers(RealmModel realmModel, int i, int i1) {
    return null;
  }

  @Override
  public List<UserModel> searchForUser(String s, RealmModel realmModel) {
    return null;
  }

  @Override
  public List<UserModel> searchForUser(String s, RealmModel realmModel, int i, int i1) {
    return null;
  }

  @Override
  public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel) {
    return null;
  }

  @Override
  public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel, int i,
      int i1) {
    return null;
  }

  @Override
  public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel, int i,
      int i1) {
    return null;
  }

  @Override
  public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel) {
    return null;
  }

  @Override
  public List<UserModel> searchForUserByUserAttribute(String s, String s1, RealmModel realmModel) {
    return null;
  }
}