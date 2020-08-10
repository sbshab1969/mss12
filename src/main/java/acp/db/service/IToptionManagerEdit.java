package acp.db.service;

import java.util.ArrayList;

import acp.forms.dto.ToptionDto;

public interface IToptionManagerEdit extends IManagerView<ToptionDto> {
  public String getPath();
  public ArrayList<String> getAttrs();
  public String getAttrPrefix();
  
  public boolean update(ToptionDto objOld, ToptionDto objNew);
}