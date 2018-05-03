package com.zhkeen.flyrise.fe.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormUtil {

  private DbUtil dbUtil;
  private Connection connection;

  public FormUtil() {
    dbUtil = new DbUtil();
  }

  public void generateLanguage(String path)
      throws IOException, SQLException, ClassNotFoundException {
    File baseDir = new File(path);
    if (baseDir.exists() && baseDir.isDirectory()) {
      loopDir(baseDir);
    }
  }

  private void loopDir(File dir) throws IOException, SQLException {
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        loopDir(file);
      } else if (file.getName().endsWith(".java") || file.getName().endsWith(".js") || file.getName()
          .endsWith(".html") || file.getName().endsWith(".jsp")) {
        findCn(file);
      }
    }
  }

  public  void findCn(File file) throws IOException, SQLException {
    if (file.isFile()) {
      Pattern pattern = Pattern.compile("(\"[\u4e00-\u9fa5]{1,}\")|(\'[\u4e00-\u9fa5]{1,}\')");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = reader.readLine()) != null) {
        Matcher m = pattern.matcher(line);
        if (m.find()) {
          dbUtil.insert(m.group().substring(1, m.group().length() - 1));
        }
      }
    }
  }
}
