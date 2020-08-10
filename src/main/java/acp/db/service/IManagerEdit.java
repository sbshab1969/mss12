package acp.db.service;

public interface IManagerEdit<T> extends IManagerView<T> {
  public Long insert(T objDto);
  public boolean update(T objDto);
  public boolean delete(Long objId);
}