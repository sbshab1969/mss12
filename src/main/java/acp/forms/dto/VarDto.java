package acp.forms.dto;

import java.util.Date;

public class VarDto {
  private Long id;
  private String name;
  private String type;
//  private Integer len;
  private Double valuen;
  private String valuev;
  private Date valued;
//  private Timestamp dateModify;
//  private String owner;

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getValuen() {
    return valuen;
  }

  public void setValuen(Double valuen) {
    this.valuen = valuen;
  }

  public String getValuev() {
    return valuev;
  }

  public void setValuev(String valuev) {
    this.valuev = valuev;
  }

  public Date getValued() {
    return valued;
  }

  public void setValued(Date valued) {
    this.valued = valued;
  }

  public String toString() {
    String str = id + "/" + name + "/" + type + "/" + valuen + "/" + valuev
        + "/" + valued + "/";
    return str;
  }

}
