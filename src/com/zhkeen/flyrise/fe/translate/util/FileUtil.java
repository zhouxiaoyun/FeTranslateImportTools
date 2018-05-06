package com.zhkeen.flyrise.fe.translate.util;

import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class FileUtil {

  private static final String CONFIG_PROPERTIES = "config.properties";

  public String readDefaultLanguage() throws IOException {
    File propertiesFile = new File(CONFIG_PROPERTIES);
    if (propertiesFile.exists()) {
      Properties properties = new Properties();
      properties.load(new FileInputStream(propertiesFile));
      String defaultLanguage = properties.getProperty("defaultLanguage");
      if (defaultLanguage != null && defaultLanguage.length() > 0) {
        return defaultLanguage;
      } else {
        throw new FileNotFoundException("多语言配置文件错误！");
      }
    } else {
      throw new FileNotFoundException("没有找到多语言的配置文件！");
    }
  }

  public Map<String, String> readSupportLanguage()
      throws IOException {
    File propertiesFile = new File(CONFIG_PROPERTIES);
    if (propertiesFile.exists()) {
      Properties properties = new Properties();
      properties.load(new FileInputStream(propertiesFile));
      String supportLanguages = properties.getProperty("supportLanguages");
      if (supportLanguages != null && supportLanguages.length() > 0) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String lang : supportLanguages.split(",")) {
          if (Constants.ALL_LANGUAGE_MAP.containsKey(lang)) {
            map.put(lang, Constants.ALL_LANGUAGE_MAP.get(lang));
          }
        }
        return map;
      } else {
        throw new FileNotFoundException("多语言配置文件错误！");
      }
    } else {
      throw new FileNotFoundException("没有找到多语言的配置文件！");
    }
  }

  public JdbcConnectionModel readJdbcConnectionModel()
      throws IOException {
    File propertiesFile = new File(CONFIG_PROPERTIES);
    if (propertiesFile.exists()) {
      Properties properties = new Properties();
      properties.load(new FileInputStream(propertiesFile));

      String driverName = properties.getProperty("mssql.jdbc.driver");
      String dbURL = properties.getProperty("mssql.jdbc.url");
      String userName = properties.getProperty("mssql.jdbc.user");
      String userPwd = properties.getProperty("mssql.jdbc.password");

      JdbcConnectionModel model = new JdbcConnectionModel();
      model.setDriverName(driverName);
      model.setJdbcUrl(dbURL);
      model.setJdbcUser(userName);
      model.setJdbcPassword(userPwd);

      return model;
    } else {
      throw new FileNotFoundException("没有找到数据库的配置文件！");
    }
  }

}
