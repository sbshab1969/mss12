package acp.db.service.impl.hiber.hql;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.VarClass;
import acp.db.service.IVarManagerEdit;
import acp.db.service.impl.hiber.all.ManagerBaseHiber;
import acp.db.service.impl.hiber.all.ManagerUtilSql;
import acp.forms.dto.VarDto;
import acp.utils.*;

public class VarManagerEditHql extends ManagerBaseHiber implements IVarManagerEdit {
  private static Logger logger = LoggerFactory.getLogger(VarManagerEditHql.class);

  private ManagerUtilSql mngUtil = new ManagerUtilSql();

  private VarDto createDto(VarClass objClass) {
    VarDto objDto = new VarDto();
    // ----------------------------------
    objDto.setId(objClass.getId());    
    objDto.setName(objClass.getName());    
    objDto.setType(objClass.getType());    
    objDto.setValuen(objClass.getValuen());    
    objDto.setValuev(objClass.getValuev());    
    objDto.setValued(objClass.getValued());    
    // ----------------------------------
    return objDto;
  }
  
  private void fillClassByDto(VarClass objClass, VarDto objDto) {
    // ----------------------------------
    objClass.setName(objDto.getName().toUpperCase());
    objClass.setType(objDto.getType());
    objClass.setLen(120);  // !!!
    objClass.setValuen(objDto.getValuen());
    objClass.setValuev(objDto.getValuev());
    objClass.setValued(objDto.getValued());
    objClass.setDateModify(mngUtil.getSysdate());    // !!!
    objClass.setOwner(mngUtil.getUser());    // !!!
    // ----------------------------------
  }

  @Override
  public VarDto select(Long objId) {
    VarDto objDto = null; 
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // ----------------------------------------
      VarClass objClass = session.get(VarClass.class, objId);
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
  public Long insert(VarDto objDto) {
    Long objId = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // --------------------
      VarClass objClass = new VarClass();
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
  public boolean update(VarDto objDto) {
    boolean res = false;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      // --------------------
      VarClass objClass = session.get(VarClass.class, objDto.getId());
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
      session.delete(session.load(VarClass.class, objId));
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
  @SuppressWarnings("unchecked")
  public void fillVars(Map<String, String> varMap) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("from VarClass");
    sbQuery.append(" where upper(name) like 'CERT%'");
    sbQuery.append(" or upper(name) = 'VERSION_MSS' order by name");
    String strQuery = sbQuery.toString();
    // ------------------------------------------------------
    List<VarClass> objList = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      Query query = session.createQuery(strQuery);
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
    // ---------------------------------------------
    for (VarClass vcls : objList) {
      String rsqName = vcls.getName().toUpperCase();
      String valuev = null;
      Date valued = null;
      if (rsqName.startsWith("CERT")) {
        valuev = vcls.getValuev();
        varMap.put(rsqName, valuev);
      } else if (rsqName.equals("VERSION_MSS")) {
        valuev = vcls.getValuev();
        valued = vcls.getValued();
        varMap.put("VERSION", valuev);
        varMap.put("VERSION_DATE", DateUtils.date2Str(valued));
      }
    }
    // ---------------------------------------------
  }
  
}
