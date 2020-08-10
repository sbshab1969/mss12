package acp.forms.dto;

import java.sql.Timestamp;

public class FileOtherDto {
  private Long id;
  private Timestamp dateEvent;
  private String descr;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDateEvent() {
    return dateEvent;
  }

  public void setDateEvent(Timestamp dateEvent) {
    this.dateEvent = dateEvent;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

}
