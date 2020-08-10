package acp.db.service.factory;

import acp.Main;
import acp.db.service.IManagerList;
import acp.db.service.ISourceManagerEdit;
import acp.db.service.impl.hiber.crit.SourceManagerEditCrit;
import acp.db.service.impl.hiber.crit.SourceManagerListCrit;
import acp.db.service.impl.hiber.crit2.SourceManagerEditCrit2;
import acp.db.service.impl.hiber.crit2.SourceManagerListCrit2;
import acp.db.service.impl.hiber.hql.SourceManagerEditHql;
import acp.db.service.impl.hiber.hql.SourceManagerListHql;
import acp.db.service.impl.hiber.sql.SourceManagerEditSql;
import acp.db.service.impl.hiber.sql.SourceManagerListSql;
import acp.db.service.impl.jdbc.SourceManagerEditJdbc;
import acp.db.service.impl.jdbc.SourceManagerListJdbc;
import acp.forms.dto.SourceDto;

public class SourceManagerFactory {

  public static IManagerList<SourceDto> getManagerList() {
    switch (Main.modeWorkDb) {
    case 0:
      return new SourceManagerListJdbc();
    case 1:
      return new SourceManagerListHql();
    case 2:
      return new SourceManagerListSql();
    case 3:
      return new SourceManagerListCrit();
    case 4:
      return new SourceManagerListCrit2();
    }
    return null;
  }

  public static ISourceManagerEdit getManagerEdit() {
    switch (Main.modeWorkDb) {
    case 0:
      return new SourceManagerEditJdbc();
    case 1:
      return new SourceManagerEditHql();
    case 2:
      return new SourceManagerEditSql();
    case 3:
      return new SourceManagerEditCrit();
    case 4:
      return new SourceManagerEditCrit2();
    }
    return null;
  }

}
