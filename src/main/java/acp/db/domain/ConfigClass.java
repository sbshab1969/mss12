package acp.db.domain;

import java.sql.Timestamp;
import java.util.Date;

public class ConfigClass {
  private Long id;
  private String name;
  private String configR;  // read only
  private String configW;  // write
  private Date dateBegin;
  private Date dateEnd;
  private String comment;
  private Timestamp dateCreate;
  private Timestamp dateModify;
  private String owner;
  private Long sourceId;
  private SourceClass source;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getConfigR() {
    return configR;
  }

  public void setConfigR(String configR) {
    this.configR = configR;
  }

  public String getConfigW() {
    return configW;
  }

  public void setConfigW(String configW) {
    this.configW = configW;
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

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Timestamp getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(Timestamp dateCreate) {
    this.dateCreate = dateCreate;
  }

  public Timestamp getDateModify() {
    return dateModify;
  }

  public void setDateModify(Timestamp dateModify) {
    this.dateModify = dateModify;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Long getSourceId() {
    return sourceId;
  }

  public void setSourceId(Long sourceId) {
    this.sourceId = sourceId;
  }

  public SourceClass getSource() {
    return source;
  }

  public void setSource(SourceClass source) {
    this.source = source;
  }

  public String toString() {
    String str = id + "/" + name + "/" + dateBegin + "/" + dateEnd + "/"
        + comment + "/" + dateCreate + "/" + dateModify + "/" + owner + "/"
        + sourceId + "/" + configR + "/" + configW;
    return str;
  }

}
