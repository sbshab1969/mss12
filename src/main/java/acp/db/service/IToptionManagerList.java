package acp.db.service;

import acp.forms.dto.ToptionDto;

public interface IToptionManagerList extends IManagerList<ToptionDto> {
  public void createTable(Long src);
}