package com.zhkeen.flyrise.fe.translate;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
    ProjectUtil projectUtil = new ProjectUtil();
    projectUtil.generateLanguage(args[0]);
  }
}
