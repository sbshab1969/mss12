package acp.db.service.impl.jdbc;

import java.sql.Connection;

import acp.Main;

public class ManagerBaseJdbc {
  protected Connection dbConn = Main.dbConnect.getConnection();
}
