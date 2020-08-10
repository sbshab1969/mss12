package acp.db.connect;

import java.sql.Connection;
import java.util.Properties;

public interface IDbConnect {

  public String getDbPath();
  public String getDbExt();

  public String getDbKeyIndex();
  public String getDbKeyName();
  public String getDbKeyFullName();

  public String getDbKeyUser();
  public String getDbKeyPassword();
  public String getDbKeyConnString();
  public String getDbKeyDriver();

  public Properties getDbProp();
  public void setDbProp(Properties props);
  
  public void readCfg(Properties props);
  public String[] getFileList();

  public Connection getConnection();

  public boolean connect();
  public boolean connectDefault();
  public void disconnect();
  public boolean reconnect();
  public boolean testConnect();

}