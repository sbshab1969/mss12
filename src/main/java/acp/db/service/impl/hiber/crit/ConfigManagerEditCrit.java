package acp.db.service.impl.hiber.crit;

import java.sql.Timestamp;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.ConfigClass;
import acp.db.service.IConfigManagerEdit;
import acp.db.service.impl.hiber.all.ManagerBaseHiber;
import acp.db.service.impl.hiber.all.ManagerUtilSql;
import acp.forms.dto.ConfigDto;
import acp.utils.*;

public class ConfigManagerEditCrit extends ManagerBaseHiber implements IConfigManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(ConfigManagerEditCrit.class);

  private ManagerUtilSql mngUtil = new ManagerUtilSql();
  
  private ConfigDto createDto(ConfigClass objClass) {
    ConfigDto objDto = new ConfigDto();
    // ----------------------------------
    objDto.setId(objClass.getId());    
    objDto.setName(objClass.getName());    
    // objDto.setConfig(objClass.getConfigR());  // R !!!!
    objDto.setDateBegin(objClass.getDateBegin());    
    objDto.setDateEnd(objClass.getDateEnd());    
    objDto.setComment(objClass.getComment());    
    // objDto.setOwner(objClass.getOwner());    
    objDto.setSourceId(objClass.getSourceId());    
    // objDto.setSourceName(null);    
    // ----------------------------------
    return objDto;
  }

  private void fillClassByDto(ConfigClass objClass, ConfigDto objDto) {
    String emptyXml = "<?xml version=\"1.0\"?><config><sverka.ats/></config>";
    Timestamp sysdt = mngUtil.getSysdate();
    String usr = mngUtil.getUser();
    // ----------------------------------
    objClass.setName(objDto.getName());    
    if (objClass.getId() == null) {
      objClass.setConfigW(emptyXml);
    } else {
      objClass.setConfigW(objClass.getConfigR());   // !!! R -> W
    }
    objClass.setDateBegin(objDto.getDateBegin());    
    objClass.setDateEnd(objDto.getDateEnd());    
    objClass.setComment(objDto.getComment());    
    if (objClass.getId() == null) {
      objClass.setDateCreate(sysdt);
    }
    objClass.setDateModify(sysdt);
    objClass.setOwner(usr);
    objClass.setSourceId(objDto.getSourceId());    
    // ----------------------------------
  }
  
  @Override
  public ConfigDto select(Long objId) {
    ConfigDto objDto = null; 
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ----------------------------------------
      ConfigClass objClass = session.get(ConfigClass.class, objId);
      objDto = createDto(objClass);
      // ----------------------------------------
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objDto = null;
    } finally {
      session.close();
    }  
    return objDto;
  }

  @Override
  public String getCfgName(Long objId) {
    // ------------------------------------------------------
    String configName = "";
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // --------------------
      Criteria query = session.createCriteria(ConfigClass.class)
          .setProjection(Projections.projectionList().add(Property.forName("name")))
          .add(Restrictions.idEq(objId));
      // --------------------
      configName = (String) query.uniqueResult();
      // --------------------
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      configName = "";
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      configName = "";
    }
    // ------------------------------------------------------
    return configName;
  }

  @Override
  public String getCfgStr(Long objId) {
    // ------------------------------------------------------
    String configStr = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // --------------------
      Criteria query = session.createCriteria(ConfigClass.class)
          .setProjection(Projections.projectionList().add(Property.forName("configR")))
          .add(Restrictions.idEq(objId));
      // --------------------
      configStr = (String) query.uniqueResult();
      // --------------------
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      configStr = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      configStr = null;
    }
    // ------------------------------------------------------
    return configStr;
  }
 
  @Override
  public Long insert(ConfigDto objDto) {
    Long objId = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ---------------------------------
      ConfigClass objClass = new ConfigClass();
      fillClassByDto(objClass, objDto);        
      objId = (Long) session.save(objClass);
      // ---------------------------------
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objId = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      objId = null;
    } finally {
      session.close();
    }  
    return objId;
  }

  @Override
  public boolean update(ConfigDto objDto) {
    boolean res = false;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // --------------------
      ConfigClass objClass = session.get(ConfigClass.class, objDto.getId());
      fillClassByDto(objClass, objDto);        
      session.update(objClass);
      // --------------------
      tx.commit();
      res = true;
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } finally {
      session.close();
    }  
    return res;
  }

  @Override
  public boolean updateCfgStr(Long objId, String txtConf) {
    boolean res = false;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_options");
    //sbQuery.append(" set msso_config=:conf"); // OK
    sbQuery.append("   set msso_config=XMLType(:conf)");  // OK
    sbQuery.append(", msso_dt_modify=sysdate");
    sbQuery.append(", msso_owner=user");
    sbQuery.append(" where msso_id=:id");
    String strQuery = sbQuery.toString();
    // -----------------------------------------
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQuery);
      query.setLong("id", objId);
      query.setString("conf", txtConf);
      logger.info("\nQuery string: " + query.getQueryString());
      // --------------------
      query.executeUpdate();
      // --------------------
      tx.commit();
      res = true;
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } finally {
      session.close();
    }  
    // -----------------------------------------------------
    return res;
  }

  @Override
  public boolean delete(Long objId) {
    boolean res = false;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ---------------------------------------------------
      session.delete(session.load(ConfigClass.class, objId));
      // ---------------------------------------------------
      tx.commit();
      res = true;
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } finally {
      session.close();
    }  
    return res;
  }
 
  @Override
  public boolean copy(Long objId) {
    boolean res = false;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_options");
    sbQuery.append(" (select msso_seq.nextval, msso_name || '_copy'");
    sbQuery.append(", msso_config");
    sbQuery.append(", msso_dt_begin, msso_dt_end, msso_comment");
    sbQuery.append(", sysdate, sysdate, user, msso_msss_id");
    sbQuery.append(" from mss_options where msso_id=:id)");
    String strQuery = sbQuery.toString();
    // -----------------------------------------------------
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQuery);
      query.setLong("id", objId);
      logger.info("\nQuery string: " + query.getQueryString());
      // --------------------
      query.executeUpdate();
      // --------------------
      tx.commit();
      res = true;
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      res = false;
    } finally {
      session.close();
    }  
    // -----------------------------------------------------
    return res;
  }

}
