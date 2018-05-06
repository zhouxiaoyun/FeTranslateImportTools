package com.zhkeen.flyrise.fe.translate.model;

import java.util.Map;

public class ConfigurationModel {

  private String driverName;
  private String jdbcUrl;
  private String jdbcUserName;
  private String jdbcPassword;

  private String appId;
  private String secretKey;

  private String defaultLanguage;
  private Map<String, String> supportLanguageMap;

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  public String getJdbcUserName() {
    return jdbcUserName;
  }

  public void setJdbcUserName(String jdbcUserName) {
    this.jdbcUserName = jdbcUserName;
  }

  public String getJdbcPassword() {
    return jdbcPassword;
  }

  public void setJdbcPassword(String jdbcPassword) {
    this.jdbcPassword = jdbcPassword;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  public void setDefaultLanguage(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  public Map<String, String> getSupportLanguageMap() {
    return supportLanguageMap;
  }

  public void setSupportLanguageMap(Map<String, String> supportLanguageMap) {
    this.supportLanguageMap = supportLanguageMap;
  }
}
