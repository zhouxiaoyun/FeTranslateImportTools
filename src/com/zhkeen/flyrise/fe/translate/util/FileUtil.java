package com.zhkeen.flyrise.fe.translate.util;

import com.zhkeen.flyrise.fe.translate.model.ConfigurationModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class FileUtil {

  private static final String CONFIG_PROPERTIES = "config.properties";

  public static ConfigurationModel readConfigurationModel()
      throws IOException, ConfigurationException {
    File propertiesFile = new File(CONFIG_PROPERTIES);
    if (propertiesFile.exists()) {
      Properties properties = new Properties();
      properties.load(new FileInputStream(propertiesFile));

      String driverName = properties.getProperty("mssql.jdbc.driver");
      String jdbcUrl = properties.getProperty("mssql.jdbc.url");
      String jdbcUserName = properties.getProperty("mssql.jdbc.user");
      String jdbcPassword = properties.getProperty("mssql.jdbc.password");

      String appId = properties.getProperty("baidu.appid");
      String secretKey = properties.getProperty("baidu.secretkey");

      String defaultLanguage = properties.getProperty("defaultLanguage");
      String supportLanguages = properties.getProperty("supportLanguages");

      if (StringUtils.isNotEmpty(driverName) && StringUtils.isNotEmpty(jdbcUrl) && StringUtils
          .isNotEmpty(jdbcUserName) && StringUtils.isNotEmpty(jdbcPassword) && StringUtils.isNotEmpty(appId)
          && StringUtils.isNotEmpty(secretKey) && StringUtils.isNotEmpty(defaultLanguage)
          && StringUtils.isNotEmpty(supportLanguages)) {

        ConfigurationModel configurationModel = new ConfigurationModel();

        configurationModel.setDriverName(driverName);
        configurationModel.setJdbcUrl(jdbcUrl);
        configurationModel.setJdbcUserName(jdbcUserName);
        configurationModel.setJdbcPassword(jdbcPassword);

        configurationModel.setAppId(appId);
        configurationModel.setSecretKey(secretKey);

        configurationModel.setDefaultLanguage(defaultLanguage);

        Map<String, String> map = new LinkedHashMap<>();
        for (String lang : supportLanguages.split(",")) {
          if (Constants.ALL_LANGUAGE_MAP.containsKey(lang)) {
            map.put(lang, Constants.ALL_LANGUAGE_MAP.get(lang));
          }
        }
        configurationModel.setSupportLanguageMap(map);

        return configurationModel;

      } else {
        throw new ConfigurationException("配置文件读取异常！");
      }
    } else {
      throw new FileNotFoundException("没有找到多语言的配置文件！");
    }
  }
}
