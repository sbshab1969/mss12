package acp.db.service.impl.hiber.all;

import org.hibernate.SessionFactory;

import acp.Main;
import acp.db.connect.IDbConnectHiber;

public class ManagerBaseHiber {
  protected IDbConnectHiber dbConnect = (IDbConnectHiber) Main.dbConnect;
  protected SessionFactory sessionFactory = dbConnect.getSessionFactory();
}
