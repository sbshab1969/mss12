package acp.db.service.factory;

import acp.Main;
import acp.db.service.IConfigManagerEdit;
import acp.db.service.IManagerList;
import acp.db.service.impl.hiber.crit.ConfigManagerEditCrit;
import acp.db.service.impl.hiber.crit.ConfigManagerListCrit;
import acp.db.service.impl.hiber.crit2.ConfigManagerEditCrit2;
import acp.db.service.impl.hiber.crit2.ConfigManagerListCrit2;
import acp.db.service.impl.hiber.hql.ConfigManagerEditHql;
import acp.db.service.impl.hiber.hql.ConfigManagerListHql;
import acp.db.service.impl.hiber.sql.ConfigManagerEditSql;
import acp.db.service.impl.hiber.sql.ConfigManagerListSql;
import acp.db.service.impl.jdbc.ConfigManagerEditJdbc;
import acp.db.service.impl.jdbc.ConfigManagerListJdbc;
import acp.forms.dto.ConfigDto;

public class ConfigManagerFactory {

  public static IManagerList<ConfigDto> getManagerList() {
    switch (Main.modeWorkDb) {
    case 0:
      return new ConfigManagerListJdbc();
    case 1:
      return new ConfigManagerListHql();
    case 2:
      return new ConfigManagerListSql();
    case 3:
      return new ConfigManagerListCrit();
    case 4:
      return new ConfigManagerListCrit2();
    }
    return null;
  }

  public static IConfigManagerEdit getManagerEdit() {
    switch (Main.modeWorkDb) {
    case 0:
      return new ConfigManagerEditJdbc();
    case 1:
      return new ConfigManagerEditHql();
    case 2:
      return new ConfigManagerEditSql();
    case 3:
      return new ConfigManagerEditCrit();
    case 4:
      return new ConfigManagerEditCrit2();
    }
    return null;
  }

}
