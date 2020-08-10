package acp.db.service.impl.hiber.sql;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.ConfigClass;
import acp.db.domain.FileLoadClass;
import acp.db.service.IFileLoadManagerEdit;
import acp.db.service.impl.hiber.all.ManagerBaseHiber;
import acp.forms.dto.FileLoadDto;
import acp.utils.DialogUtils;

public class FileLoadManagerEditSql extends ManagerBaseHiber implements IFileLoadManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerEditSql.class);

  private FileLoadDto createDto(FileLoadClass objClass) {
    FileLoadDto objDto = new FileLoadDto();
    // ----------------------------------
    objDto.setId(objClass.getId());    
    objDto.setName(objClass.getName());    
    objDto.setMd5(objClass.getMd5());    
    objDto.setDateCreate(objClass.getDateCreate());
    objDto.setDateWork(objClass.getDateWork());
    objDto.setOwner(objClass.getOwner());
    objDto.setConfigId(objClass.getConfigId());
    if (objClass.getConfig() != null) {
      objDto.setConfigName(objClass.getConfig().getName());
    }
    objDto.setRecAll(objClass.getRecAll());
    objDto.setRecErr(objClass.getRecErr());
    // ----------------------------------
    ArrayList<String> statList = new ArrayList<>();
    statList.add(String.valueOf(objClass.getRecAll()));
    statList.add(String.valueOf(objClass.getRecErr()));
    objDto.setStatList(statList);
    // ----------------------------------
    return objDto;
  }

  public FileLoadDto select(Long objId) {
    FileLoadDto objDto = null; 
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
//    sbQuery.append("select fl.*,cfg.*");  // -- Error
    sbQuery.append("select {fl.*},{cfg.*}");
    sbQuery.append("  from mss_files fl, mss_options cfg");
    sbQuery.append(" where mssf_msso_id=msso_id");
    sbQuery.append("   and mssf_id=:id");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ----------------------------------------
      SQLQuery query = session.createSQLQuery(strQuery)
          .addEntity("fl",FileLoadClass.class)
          .addEntity("cfg",ConfigClass.class)
      ;
      query.setLong("id", objId);
      logger.info("\nQuery string: " + query.getQueryString());
      // ----------------------------------------
      Object[] objArr = (Object[]) query.uniqueResult();
      // ------------
      FileLoadClass objClass = (FileLoadClass) objArr[0];
      ConfigClass cfgClass = (ConfigClass) objArr[1];
      objClass.setConfig(cfgClass);
      objDto = createDto(objClass);
      // ----------------------------------------
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objDto = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objDto = null;
    } finally {
      session.close();
    }  
    return objDto;
  }

}
