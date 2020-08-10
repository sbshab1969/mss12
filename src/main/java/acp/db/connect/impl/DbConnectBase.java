package acp.db.connect.impl;

import java.util.Properties;

import acp.db.connect.IDbConnect;

public abstract class DbConnectBase implements IDbConnect {
  protected String dbPath;
  protected String dbExt;
  protected String dbDefaultName;

  protected String dbKeyIndex;
  protected String dbKeyName;
  protected String dbKeyFullName;

  protected String dbKeyUser;
  protected String dbKeyPassword;
  protected String dbKeyConnString;
  protected String dbKeyDriver;

  protected Properties dbProp;

  @Override
  public String getDbPath() {
    return dbPath;
  }

  @Override
  public String getDbExt() {
    return dbExt;
  }

  @Override
  public String getDbKeyIndex() {
    return dbKeyIndex;
  }

  @Override
  public String getDbKeyName() {
    return dbKeyName;
  }

  @Override
  public String getDbKeyFullName() {
    return dbKeyFullName;
  }

  @Override
  public String getDbKeyUser() {
    return dbKeyUser;
  }

  @Override
  public String getDbKeyPassword() {
    return dbKeyPassword;
  }

  @Override
  public String getDbKeyConnString() {
    return dbKeyConnString;
  }

  @Override
  public String getDbKeyDriver() {
    return dbKeyDriver;
  }

  @Override
  public Properties getDbProp() {
    return dbProp;
  }

  @Override
  public void setDbProp(Properties props) {
    dbProp = props;
  }

}
