package acp.db.service.factory;

import acp.Main;
import acp.db.service.IFileLoadManagerEdit;
import acp.db.service.IManagerList;
import acp.db.service.impl.hiber.crit.FileLoadManagerEditCrit;
import acp.db.service.impl.hiber.crit.FileLoadManagerListCrit;
import acp.db.service.impl.hiber.crit2.FileLoadManagerEditCrit2;
import acp.db.service.impl.hiber.crit2.FileLoadManagerListCrit2;
import acp.db.service.impl.hiber.hql.FileLoadManagerEditHql;
import acp.db.service.impl.hiber.hql.FileLoadManagerListHql;
import acp.db.service.impl.hiber.sql.FileLoadManagerEditSql;
import acp.db.service.impl.hiber.sql.FileLoadManagerListSql;
import acp.db.service.impl.jdbc.FileLoadManagerEditJdbc;
import acp.db.service.impl.jdbc.FileLoadManagerListJdbc;
import acp.forms.dto.FileLoadDto;

public class FileLoadManagerFactory {

  public static IManagerList<FileLoadDto> getManagerList() {
    switch (Main.modeWorkDb) {
    case 0:
      return new FileLoadManagerListJdbc();
    case 1:
      return new FileLoadManagerListHql();
    case 2:
      return new FileLoadManagerListSql();
    case 3:
      return new FileLoadManagerListCrit();
    case 4:
      return new FileLoadManagerListCrit2();
    }
    return null;
  }

  public static IFileLoadManagerEdit getManagerEdit() {
    switch (Main.modeWorkDb) {
    case 0:
      return new FileLoadManagerEditJdbc();
    case 1:
      return new FileLoadManagerEditHql();
    case 2:
      return new FileLoadManagerEditSql();
    case 3:
      return new FileLoadManagerEditCrit();
    case 4:
      return new FileLoadManagerEditCrit2();
    }
    return null;
  }

}
