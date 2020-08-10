package acp.db.connect.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConnectJdbc extends DbConnectBase {
  private static Logger logger = LoggerFactory.getLogger(DbConnectJdbc.class);

  private static Connection dbConn;

  public DbConnectJdbc() {
    dbPath = "/config/jdbc";
    dbExt = "xml";
    dbDefaultName = "oracle.xml";

    dbKeyIndex = "index";
    dbKeyName  = "name";
    dbKeyFullName  = "fullName";
    
    dbKeyUser = "user";
    dbKeyPassword = "password";
    dbKeyConnString = "connectionString";
    dbKeyDriver = "driver";
  }

  @Override
  public Connection getConnection() {
    return dbConn;
  }

  @Override
  public boolean testConnect() {
    if (dbConn != null) {
      return true;
    } else {
      return false;
    }  
  }

  @Override
  public boolean connect() {
    logger.info("connecting ...");
    try {
      String user = dbProp.getProperty(dbKeyUser);
      String passwd = dbProp.getProperty(dbKeyPassword);
      String connString = dbProp.getProperty(dbKeyConnString);
      String driver = dbProp.getProperty(dbKeyDriver);
      // ---------------------------------
      Class.forName(driver).newInstance();
      dbConn = DriverManager.getConnection(connString, user, passwd);
      // ---------------------------------
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      return false;
    }
    System.out.println("connect: " + dbConn);
    return true;
  }

  @Override
  public boolean connectDefault() {
    String dbIndex = null;
    String dbName = null;
    // ----------------------
    String[] list = getFileList();
    if (list.length == 0) {
      return false;
    }
    List<String> arrList = Arrays.asList(list);
    int ind = arrList.indexOf(dbDefaultName);
    if (ind >= 0) {
      dbIndex = String.valueOf(ind);
      dbName = dbDefaultName;
    } else {
      // return false;
      logger.info("Конфигурация " + dbDefaultName + " не найдена. Выбирается первая из списка.");
      dbIndex = "0";
      dbName = list[0];
    }
    // ----------------------
    Properties props = new Properties();
    props.setProperty(dbKeyIndex, dbIndex);
    props.setProperty(dbKeyName, dbName);
    // ----------------------
    readCfg(props);  // user, password
    setDbProp(props);
    // ----------------------
    boolean res = connect();
    // ----------------------
    return res;
  }

  @Override
  public boolean reconnect() { 
    disconnect();
    boolean res = connect();
    return res;
  }

  @Override
  public void disconnect() {
    if (dbConn == null) {
      return;
    }
    System.out.println("disconnect: " + dbConn);
    try {
      dbConn.close();
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    dbConn = null;
  }

  @Override
  public void readCfg(Properties props) {
    // ---------------------------
    String fullFileName = dbPath;
    if (dbPath != "") {
      fullFileName += "/";
    }
    fullFileName += props.getProperty(dbKeyName);
    props.setProperty(dbKeyFullName, fullFileName);
    // System.out.println("Файл конфигурации: " + fullFileName);
    // ---------------------------
    Properties fileProps = new Properties();
    try {
      // InputStream fis = new FileInputStream(fullFileName);
      InputStream fis = getClass().getResourceAsStream(fullFileName);
      fileProps.loadFromXML(fis);
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    // ---------------------------
    props.setProperty(dbKeyUser, fileProps.getProperty(dbKeyUser));
    props.setProperty(dbKeyPassword, fileProps.getProperty(dbKeyPassword));
    props.setProperty(dbKeyConnString, fileProps.getProperty(dbKeyConnString));
    props.setProperty(dbKeyDriver, fileProps.getProperty(dbKeyDriver));
    // ---------------------------
    printProps(props);
  }

  private void printProps(Properties props) {
    System.out.println("");

    System.out.println("Configuration file = " + props.getProperty(dbKeyFullName));
    System.out.println(dbKeyConnString + " = " + props.getProperty(dbKeyConnString));
    System.out.println(dbKeyDriver + " = " + props.getProperty(dbKeyDriver));
    System.out.println(dbKeyUser + " = " + props.getProperty(dbKeyUser));
    // System.out.println(dbKeyPassword + " = " + props.getProperty(dbKeyPassword));
    System.out.println(dbKeyPassword + " = " + "****");

    System.out.println("");
  }

  @Override
  public String[] getFileList() {
    String[] list = {};
    URL url = getClass().getResource(dbPath);
    if (url == null) {
      return list;
    }
    String urlProtocol = url.getProtocol();
    System.out.println("getFileList:");
    System.out.println("url: " + url);
    System.out.println("urlProtocol: " + urlProtocol);
    
    if (urlProtocol.equalsIgnoreCase("jar")) {
      list = fileListJar(url,dbExt);
    } else {
      list = fileListFile(url,dbExt);
    }
    // System.out.println(Arrays.deepToString(list));
    Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
    return list;
  }

  public String[] fileListFile(URL url, String ext) {
    URI uri = null;
    try {
      uri = url.toURI();
    } catch (URISyntaxException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    // ------------
    String[] list = {};
    if (uri != null) {
      File path = new File(uri);
      // list = path.list();
      list = path.list(filter(".*\\." + ext));
    }
    // ------------
    return list;
  }

  private FilenameFilter filter(final String regex) {
    return new FilenameFilter() {
      private Pattern pattern = Pattern.compile(regex);

      public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
      }
    };
  }

  public String[] fileListJar(URL url, String ext) {
    String[] list = {};
    List<String> arrList = new ArrayList<>();
    JarURLConnection conn;
    try {
      conn = (JarURLConnection) url.openConnection();
      Enumeration<JarEntry> en = conn.getJarFile().entries();
      String mainEntryName = conn.getEntryName();
      while (en.hasMoreElements()) {
        JarEntry entry = en.nextElement();
        String entryName = entry.getName();
        if (entryName.startsWith(mainEntryName)) {
          if (!entry.isDirectory()) {
            String filter = "." + ext;
            if (entryName.endsWith(filter)) {
              int pos = entryName.lastIndexOf("/");
              String entryName2 = entryName.substring(pos+1, entryName.length());
              arrList.add(entryName2);
            }  
          }
        }
      }
      list = new String[arrList.size()];
      arrList.toArray(list);
    } catch (IOException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return list;
  }

}
