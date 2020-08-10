package acp.forms.dto;

import java.util.Date;

import java.util.ArrayList;

public class ToptionDto {
  private Long id;
  private ArrayList<String> pArray;
  private Date dateBegin;
  private Date dateEnd;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrayList<String> getPArray() {
    return pArray;
  }

  public void setArrayP(ArrayList<String> pArray) {
    this.pArray = pArray;
  }

  public Date getDateBegin() {
    return dateBegin;
  }

  public void setDateBegin(Date dateBegin) {
    this.dateBegin = dateBegin;
  }

  public Date getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(Date dateEnd) {
    this.dateEnd = dateEnd;
  }

}
