package uk.co.urbanandco.keycloak.storage.user;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class RestUserStorageProvider implements UserStorageProvider,
    UserLookupProvider, UserQueryProvider, CredentialInputUpdater, CredentialInputValidator {

  @Override
  public boolean supportsCredentialType(String s) {
    return false;
  }

  @Override
  public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String s) {
    return false;
  }

  @Override
  public boolean isValid(RealmModel realmModel, UserModel userModel,
      CredentialInput credentialInput) {
    return false;
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
    return null;
  }

  @Override
  public void close() {

  }

  @Override
  public UserModel getUserById(String s, RealmModel realmModel) {
    return null;
  }

  @Override
  public UserModel getUserByUsername(String s, RealmModel realmModel) {
    return null;
  }

  @Override
  public UserModel getUserByEmail(String s, RealmModel realmModel) {
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