package acp.db.service;

import java.util.Map;

import acp.forms.dto.VarDto;

public interface IVarManagerEdit extends IManagerEdit<VarDto> {
  void fillVars(Map<String, String> varMap);
}