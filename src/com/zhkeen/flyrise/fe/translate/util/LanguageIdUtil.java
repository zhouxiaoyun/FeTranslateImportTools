package com.zhkeen.flyrise.fe.translate.util;

import java.util.Date;
import java.util.Random;

public class LanguageIdUtil {

  public static String generateId() {
    return String.valueOf(new Date().getTime() * 1000 + new Random().nextInt(1000));
  }
}
