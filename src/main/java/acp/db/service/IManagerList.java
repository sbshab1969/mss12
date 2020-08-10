package acp.db.service;

import java.util.List;
import java.util.Map;

public interface IManagerList<T> {
  public String[] getHeaders();
  public Class<?>[] getTypes();
  public Long getSeqId();

  public void prepareQuery(Map<String,String> mapFilter);
  public List<T> queryAll();
  public List<T> fetchPage(int startPos, int cntRows);
  
  public long countRecords();

  public void openQueryAll();
  public void openQueryPage();
  public void closeQuery();
}