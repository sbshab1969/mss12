package acp.db.service.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.service.IFileLoadManagerEdit;
import acp.forms.dto.FileLoadDto;
import acp.utils.*;

public class FileLoadManagerEditJdbc extends ManagerBaseJdbc implements IFileLoadManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerEditJdbc.class);

  private FileLoadDto createDto(ResultSet rsq) throws SQLException {
    FileLoadDto objDto = new FileLoadDto();
    // ----------------------------------
    Long rsqId = rsq.getLong("mssf_id");
    String rsqName = rsq.getString("mssf_name");
    String rsqMd5 = rsq.getString("mssf_md5");
    Timestamp rsqDateCreate = rsq.getTimestamp("mssf_dt_create");
    Timestamp rsqDateWork = rsq.getTimestamp("mssf_dt_work");
    String rsqOwner = rsq.getString("mssf_owner");
    Long rsqConfigId = rsq.getLong("mssf_msso_id");
    String rsqConfigName = rsq.getString("msso_name");
    String rsqRecAll = rsq.getString("records_all");
    String rsqRecErr = rsq.getString("records_err");
    // ---------------------
    objDto.setId(rsqId);
    objDto.setName(rsqName);
    objDto.setMd5(rsqMd5);
    objDto.setDateCreate(rsqDateCreate);
    objDto.setDateWork(rsqDateWork);
    objDto.setOwner(rsqOwner);
    objDto.setConfigId(rsqConfigId);
    objDto.setConfigName(rsqConfigName);
    // -----
    ArrayList<String> statList = new ArrayList<>();
    statList.add(rsqRecAll);
    statList.add(rsqRecErr);
    objDto.setStatList(statList);
    // ---------------------
    return objDto;
  }
  
  @Override
  public FileLoadDto select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
//    sbQuery.append("select * from mss_files,mss_options"); // Error 
    sbQuery.append("select mssf_id,mssf_name,mssf_md5,mssf_owner");
    sbQuery.append(",mssf_dt_create,mssf_dt_work");
    sbQuery.append(",mssf_msso_id,msso_name");
    sbQuery.append(",mssf_rec_all records_all, mssf_rec_er records_err");
//    sbQuery.append(",extract(mssf_statistic,'statistic/records/all/text()').getStringVal() records_all");
//    sbQuery.append(",extract(mssf_statistic,'statistic/records/error/text()').getStringVal() records_err");
    sbQuery.append("  from mss_files,mss_options");
    sbQuery.append(" where mssf_msso_id=msso_id");
    sbQuery.append("   and mssf_id=?");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    FileLoadDto filesObj = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(strQuery);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        filesObj = createDto(rsq);
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e, logger);
      filesObj = null;
    } catch (Exception e) {
      DialogUtils.errorPrint(e, logger);
      filesObj = null;
    }
    // ------------------------------------------------------
    return filesObj;
  }

}
