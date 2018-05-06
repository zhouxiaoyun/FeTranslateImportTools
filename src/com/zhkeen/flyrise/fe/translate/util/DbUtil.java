package com.zhkeen.flyrise.fe.translate.util;

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
  private static final String APP_ID = "20180503000153011";
  private static final String SECURITY_KEY = "_MRqRxWs1i75bfvXg4kU";
  private TransApi transApi;
  private Connection connection;

  public DbUtil() {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      connection = DriverManager
          .getConnection("jdbc:sqlserver://localhost;database=FE_BASE5;SelectMerthod=cursor", "sa",
              "123456");
      transApi = new TransApi(APP_ID, SECURITY_KEY);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private Connection connect() throws ClassNotFoundException, SQLException {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    Connection connection = DriverManager
        .getConnection("jdbc:sqlserver://localhost;database=FE_BASE5;SelectMerthod=cursor", "sa",
            "123456");
    return connection;
  }

  public void insert(String message) throws SQLException, UnsupportedEncodingException {
    String selectSql = String
        .format("SELECT ID FROM %s WHERE zh = ?", TABLE_TRANSLATE);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    if (!rs.next()) {
      TransApi transApi = new TransApi(APP_ID, SECURITY_KEY);
      String insertSql =
          "INSERT INTO " + TABLE_TRANSLATE + "(ID, " + getSearchField(Constants.ALL_LANGUAGE_MAP)
              + ") VALUES(";
      int j = Constants.ALL_LANGUAGE_MAP.size();
      for (int i = 0; i < j; i++) {
        insertSql = insertSql + "?,";
      }
      insertSql = insertSql + "?)";
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      psInsert.setLong(1, new Date().getTime() * 1000 + new Random().nextInt(1000));
      int m = 2;
      for (String lang : Constants.ALL_LANGUAGE_MAP.keySet()) {
        psInsert.setString(m, transApi.getTransResult(message,"zh", lang));
        m++;
      }
      psInsert.execute();
    }
  }

  public void close() throws SQLException {
    connection.close();
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
