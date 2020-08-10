package acp.db.service.impl.hiber.sql;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.SourceClass;
import acp.db.service.ISourceManagerEdit;
import acp.db.service.impl.hiber.all.ManagerBaseHiber;
import acp.db.service.impl.hiber.all.ManagerUtilSql;
import acp.forms.dto.SourceDto;
import acp.utils.DialogUtils;
import acp.utils.QueryUtils;

public class SourceManagerEditSql extends ManagerBaseHiber implements ISourceManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(SourceManagerEditSql.class);

  private ManagerUtilSql mngUtil = new ManagerUtilSql();

  private SourceDto createDto(SourceClass objClass) {
    SourceDto objDto = new SourceDto();
    // ----------------------------------
    objDto.setId(objClass.getId());    
    objDto.setName(objClass.getName());    
//    objDto.setOwner(objClass.getOwner());    
    // ----------------------------------
    return objDto;
  }
  
  private void fillClassByDto(SourceClass objClass, SourceDto objDto) {
    // ----------------------------------
    objClass.setName(objDto.getName());
    if (objClass.getId() == null) {
      objClass.setDateCreate(mngUtil.getSysdate());    // !!!
    }
    objClass.setDateModify(mngUtil.getSysdate());    // !!!
    objClass.setOwner(mngUtil.getUser());    // !!!
    // ----------------------------------
  }

  @Override
  public SourceDto select(Long objId) {
    SourceDto objDto = null; 
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ----------------------------------------
      SourceClass objClass = session.get(SourceClass.class, objId);
      objDto= createDto(objClass);
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

  @Override
  public Long insert(SourceDto objDto) {
    Long objId = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ---------------------------------
      SourceClass objClass = new SourceClass();
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
  public boolean update(SourceDto objDto) {
    boolean res = false;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // --------------------
      SourceClass objClass = session.get(SourceClass.class, objDto.getId());
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
  public boolean delete(Long objId) {
    boolean res = false;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ---------------------------------------------------
      session.delete(session.load(SourceClass.class, objId));
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

  private List<?> getListObject(String strQuery) {
    List<?> objList =  null;
    // --------------------------------------------
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQuery);
      objList = query.list();
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
    } finally {
      session.close();
    }
    // -------------------------------------------
    return objList;
  }

  private List<String[]> getListStringByQuery(String strQuery) {
    List<?> objList =  getListObject(strQuery);
    List<String[]> arrayString = QueryUtils.getListString(objList);
    return arrayString;
  }

  @Override
  public List<String[]> getSources() {
    String strQuery = "select msss_id, msss_name from mss_source order by msss_name";
    List<String[]> arrayString = getListStringByQuery(strQuery);
    return arrayString;
  }

}
