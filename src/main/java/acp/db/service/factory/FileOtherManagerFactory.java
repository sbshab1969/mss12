package acp.db.service.factory;

import acp.Main;
import acp.db.service.IManagerList;
import acp.db.service.impl.hiber.crit.FileOtherManagerListCrit;
import acp.db.service.impl.hiber.crit2.FileOtherManagerListCrit2;
import acp.db.service.impl.hiber.hql.FileOtherManagerListHql;
import acp.db.service.impl.hiber.sql.FileOtherManagerListSql;
import acp.db.service.impl.jdbc.FileOtherManagerListJdbc;
import acp.forms.dto.FileOtherDto;

public class FileOtherManagerFactory {

  public static IManagerList<FileOtherDto> getManagerList(Long file_id) {
    switch (Main.modeWorkDb) {
    case 0:
      return new FileOtherManagerListJdbc(file_id);
    case 1:
      return new FileOtherManagerListHql(file_id);
    case 2:
      return new FileOtherManagerListSql(file_id);
    case 3:
      return new FileOtherManagerListCrit(file_id);
    case 4:
      return new FileOtherManagerListCrit2(file_id);
    }
    return null;
  }

}
