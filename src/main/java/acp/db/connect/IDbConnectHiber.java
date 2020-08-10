package acp.db.connect;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface IDbConnectHiber extends IDbConnect {

  SessionFactory getSessionFactory();
  Connection openConnection(SessionFactory sFactory);
  Connection openConnection(Session sess);
  Connection openConnectionDoWork(Session sess);

}