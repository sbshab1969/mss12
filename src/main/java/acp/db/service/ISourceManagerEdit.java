package acp.db.service;

import java.util.List;

import acp.forms.dto.SourceDto;

public interface ISourceManagerEdit extends IManagerEdit<SourceDto> {
  public List<String[]> getSources();
}