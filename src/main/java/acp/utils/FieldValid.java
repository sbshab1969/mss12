package acp.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FieldValid {
  private static final String BUNDLE_NAME = "fieldvalid";
//  private static final ResourceBundle resourceBundle = loadBundle();
  private static ResourceBundle resourceBundle = loadBundle();

  private static ResourceBundle loadBundle() {
    return ResourceBundle.getBundle(BUNDLE_NAME);
  }

  public static void reloadBundle() {
    resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
  }

  public static String getString(String key) {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      return null;
    }
  }
}
