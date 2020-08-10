package acp.db.service.impl.hiber.all;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.utils.*;

public class ManagerUtilSql extends ManagerBaseHiber {
  private static Logger logger = LoggerFactory.getLogger(ManagerUtilSql.class);

  public Long getValueL(String strQuery) {
    Long val = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQuery);
      BigDecimal idBigDec = (BigDecimal) query.uniqueResult();
      val = idBigDec.longValue();
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      val = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      val = null;
    } finally {
      session.close();
    }
    return val;
  }

  public String getValueV(String strQuery) {
    String val = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQuery);
      val = (String) query.uniqueResult();
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      val = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      val = null;
    } finally {
      session.close();
    }
    return val;
  }

  public Timestamp getValueT(String strQuery) {
    Timestamp val = null;
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    try {
      SQLQuery query = session.createSQLQuery(strQuery);
      val = (Timestamp) query.uniqueResult();
      tx.commit();
    } catch (HibernateException e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      val = null;
    } catch (Exception e) {
      tx.rollback();
      DialogUtils.errorPrint(e,logger);
      val = null;
    } finally {
      session.close();
    }
    return val;
  }

  public String getUser() {
    String usr = getValueV("select user from dual");
    return usr;
  }

  public Timestamp getSysdate() {
    Timestamp tst = getValueT("select sysdate from dual");
    return tst;
  }

}
