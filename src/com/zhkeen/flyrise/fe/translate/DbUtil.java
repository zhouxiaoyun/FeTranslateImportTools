package com.zhkeen.flyrise.fe.translate;

import com.zhkeen.flyrise.fe.translate.baidu.TransApi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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

  public void insert(String message) throws SQLException {
    String selectSql = String
        .format("SELECT ID FROM %s WHERE zh_CN = ?", TABLE_TRANSLATE);
    PreparedStatement pstmt = connection.prepareStatement(selectSql);
    pstmt.setString(1, message);
    ResultSet rs = pstmt.executeQuery();
    if (!rs.next()) {
      String insertSql = String
          .format("INSERT %s(ID,zh_CN,zh_TW,en) VALUES(?,?,?,?)", TABLE_TRANSLATE);
      PreparedStatement psInsert = connection.prepareStatement(insertSql);
      String enMessage = "";
      String twMessage = "";
      try {
        enMessage = transApi.getTransResult(message, "zh", "en");
        twMessage = ZHConverter.convert(message, ZHConverter.TRADITIONAL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      psInsert.setLong(1, new Date().getTime() * 1000 + new Random().nextInt(1000));
      psInsert.setString(2, message);
      psInsert.setString(3, twMessage);
      psInsert.setString(4, enMessage);
      psInsert.execute();
    }
  }

  public void close() throws SQLException {
    connection.close();
  }
}
