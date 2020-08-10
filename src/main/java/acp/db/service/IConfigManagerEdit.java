package acp.db.service;

import acp.forms.dto.ConfigDto;

public interface IConfigManagerEdit extends IManagerEdit<ConfigDto> {
  public String getCfgName(Long objId);
  public String getCfgStr(Long objId);
  boolean updateCfgStr(Long objId, String txtConf);
  boolean copy(Long objId);
}