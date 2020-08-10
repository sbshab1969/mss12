package acp.db.service;

public interface IManagerView<T> {
  public T select(Long objId);
}