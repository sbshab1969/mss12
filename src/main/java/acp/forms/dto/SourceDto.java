package acp.forms.dto;

public class SourceDto {
  private Long id;
  private String name;
//  private Timestamp dateCreate;
//  private Timestamp dateModify;
  private String owner;

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

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

}
