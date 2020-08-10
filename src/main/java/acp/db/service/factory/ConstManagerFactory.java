package acp.db.service.factory;

import acp.Main;
import acp.db.service.IConstManagerEdit;
import acp.db.service.IManagerList;
import acp.db.service.impl.hiber.crit.ConstManagerEditCrit;
import acp.db.service.impl.hiber.crit.ConstManagerListCrit;
import acp.db.service.impl.hiber.crit2.ConstManagerEditCrit2;
import acp.db.service.impl.hiber.crit2.ConstManagerListCrit2;
import acp.db.service.impl.hiber.hql.ConstManagerEditHql;
import acp.db.service.impl.hiber.hql.ConstManagerListHql;
import acp.db.service.impl.hiber.sql.ConstManagerEditSql;
import acp.db.service.impl.hiber.sql.ConstManagerListSql;
import acp.db.service.impl.jdbc.ConstManagerEditJdbc;
import acp.db.service.impl.jdbc.ConstManagerListJdbc;
import acp.forms.dto.ConstDto;

public class ConstManagerFactory {

  public static IManagerList<ConstDto> getManagerList() {
    switch (Main.modeWorkDb) {
    case 0:
      return new ConstManagerListJdbc();
    case 1:
      return new ConstManagerListHql();
    case 2:
      return new ConstManagerListSql();
    case 3:
      return new ConstManagerListCrit();
    case 4:
      return new ConstManagerListCrit2();
    }
    return null;
  }

  public static IConstManagerEdit getManagerEdit() {
    switch (Main.modeWorkDb) {
    case 0:
      return new ConstManagerEditJdbc();
    case 1:
      return new ConstManagerEditHql();
    case 2:
      return new ConstManagerEditSql();
    case 3:
      return new ConstManagerEditCrit();
    case 4:
      return new ConstManagerEditCrit2();
    }
    return null;
  }

}
