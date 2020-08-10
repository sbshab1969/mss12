package acp.db.domain;

import java.sql.Timestamp;

public class FileOtherClass {
  private Long id;
  private Timestamp dateEvent;
  private String descr;
  private Long refId;
  private Long confId;
  private Long constId;

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

  public Long getRefId() {
    return refId;
  }

  public void setRefId(Long refId) {
    this.refId = refId;
  }

  public Long getConfId() {
    return confId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public Long getConstId() {
    return constId;
  }

  public void setConstId(Long constId) {
    this.constId = constId;
  }

}
