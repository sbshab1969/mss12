package acp.db.service.factory;

import acp.db.service.IVarManagerEdit;
import acp.db.service.impl.hiber.crit.VarManagerEditCrit;
import acp.db.service.impl.hiber.crit.VarManagerListCrit;
import acp.db.service.impl.hiber.crit2.VarManagerEditCrit2;
import acp.db.service.impl.hiber.crit2.VarManagerListCrit2;
import acp.db.service.impl.hiber.hql.VarManagerEditHql;
import acp.db.service.impl.hiber.hql.VarManagerListHql;
import acp.db.service.impl.hiber.sql.VarManagerEditSql;
import acp.db.service.impl.hiber.sql.VarManagerListSql;
import acp.db.service.impl.jdbc.VarManagerEditJdbc;
import acp.db.service.impl.jdbc.VarManagerListJdbc;
import acp.Main;
import acp.db.service.IManagerList;
import acp.forms.dto.VarDto;

public class VarManagerFactory {

  public static IManagerList<VarDto> getManagerList() {
    switch (Main.modeWorkDb) {
    case 0:
      return new VarManagerListJdbc();
    case 1:
      return new VarManagerListHql();
    case 2:
      return new VarManagerListSql();
    case 3:
      return new VarManagerListCrit();
    case 4:
      return new VarManagerListCrit2();
    }
    return null;
  }

  public static IVarManagerEdit getManagerEdit() {
    switch (Main.modeWorkDb) {
    case 0:
      return new VarManagerEditJdbc();
    case 1:
      return new VarManagerEditHql();
    case 2:
      return new VarManagerEditSql();
    case 3:
      return new VarManagerEditCrit();
    case 4:
      return new VarManagerEditCrit2();
    }
    return null;
  }

}
