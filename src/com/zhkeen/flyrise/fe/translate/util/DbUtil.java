package com.zhkeen.flyrise.fe.translate.util;

import com.zhkeen.flyrise.fe.translate.model.ConfigurationModel;
import com.zhkeen.flyrise.fe.translate.util.baidu.TransApi;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class DbUtil {

  private static final String TABLE_TRANSLATE = "TRANSLATE";
  private TransApi transApi;
  private Connection connection;
  private String defaultLanguage;
  private Map<String, String> supportLanguage;

  public DbUtil(ConfigurationModel configurationModel)
      throws SQLException, ClassNotFoundException {
    this.transApi = new TransApi(configurationModel.getAppId(), configurationModel.getSecretKey());
    this.connection = getConnection(configurationModel);
    this.defaultLanguage = configurationModel.getDefaultLanguage();
    this.supportLanguage = configurationModel.getSupportLanguageMap();
  }

  private Connection getConnection(ConfigurationModel model)
      throws SQLException, ClassNotFoundException {
    Class.forName(model.getDriverName());
    Connection connection = DriverManager
        .getConnection(model.getJdbcUrl(), model.getJdbcUserName(), model.getJdbcPassword());
    return connection;
  }

  public long insert(String message)
      throws SQLException, UnsupportedEncodingException {
    String selectSql = String
        .format("SELECT ID FROM %s WHERE %s = ?", TABLE_TRANSLATE, defaultLanguage);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    if (!rs.next()) {
      long id = LanguageIdUtil.generateId();
      String insertSql =
          "INSERT INTO " + TABLE_TRANSLATE + "(ID, " + getSearchField(supportLanguage)
              + ") VALUES(";
      int j = supportLanguage.size();
      for (int i = 0; i < j; i++) {
        insertSql = insertSql + "?,";
      }
      insertSql = insertSql + "?)";
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      psInsert.setLong(1, new Date().getTime() * 1000 + new Random().nextInt(1000));
      int m = 2;
      for (String lang : supportLanguage.keySet()) {
        psInsert.setString(m, transApi.getTransResult(message, "zh", lang));
        m++;
      }
      psInsert.execute();
      return id;
    } else {
      return rs.getLong("ID");
    }
  }

  private String getSearchField(Map<String, String> supportLanguageMap) {
    String field = "";
    for (String lang : supportLanguageMap.keySet()) {
      field = field + lang + ",";
    }
    field = field.substring(0, field.length() - 1);
    return field;
  }
}
