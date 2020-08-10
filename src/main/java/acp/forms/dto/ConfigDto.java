package acp.forms.dto;

import java.util.Date;

public class ConfigDto {
  private Long id;
  private String name;
  private String config;
  private Date dateBegin;
  private Date dateEnd;
  private String comment;
  private String owner;
  private Long sourceId;
  private String sourceName;

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

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
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

  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public String toString() {
    String str = id + "/" + name + "/" + dateBegin + "/" + dateEnd + "/"
        + comment + "/" + owner + "/"
        + sourceId + "/" + config;
    return str;
  }

}
