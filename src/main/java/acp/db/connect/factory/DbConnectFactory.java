package acp.db.connect.factory;

import acp.Main;
import acp.db.connect.IDbConnect;
import acp.db.connect.impl.DbConnectHiber;
import acp.db.connect.impl.DbConnectJdbc;

public class DbConnectFactory {

  public static IDbConnect getDbConnect() {
    if (Main.modeWorkDb == 0) {
      return new DbConnectJdbc();
    } else {
      return new DbConnectHiber();
    }
  }

}
