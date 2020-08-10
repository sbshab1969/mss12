package acp.db.service.factory;

import java.util.ArrayList;

import acp.Main;
import acp.db.service.IToptionManagerEdit;
import acp.db.service.IToptionManagerList;
import acp.db.service.impl.hiber.all.ToptionManagerEditSql;
import acp.db.service.impl.hiber.all.ToptionManagerListSql;
import acp.db.service.impl.jdbc.ToptionManagerEditJdbc;
import acp.db.service.impl.jdbc.ToptionManagerListJdbc;

public class ToptionManagerFactory {

  public static IToptionManagerList getManagerList(String path, ArrayList<String> attrs) {
    if (Main.modeWorkDb == 0) {
      return new ToptionManagerListJdbc(path, attrs);
    } else {
      return new ToptionManagerListSql(path, attrs);
    }
  }

  public static IToptionManagerEdit getManagerEdit(String path, ArrayList<String> attrs) {
    if (Main.modeWorkDb == 0) {
      return new ToptionManagerEditJdbc(path, attrs);
    } else {
      return new ToptionManagerEditSql(path, attrs);
    }
  }

}
