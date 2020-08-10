package acp.forms.dto;

import java.sql.Timestamp;

import java.util.ArrayList;

public class FileLoadDto {
  private Long id;
  private String name;
  private String md5;
  private Timestamp dateCreate;
  private Timestamp dateWork;
  private String owner;
  private Long configId;
  private String configName;
  private int recAll;
  private int recErr;
  private ArrayList<String> statList;

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

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public Timestamp getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(Timestamp dateCreate) {
    this.dateCreate = dateCreate;
  }

  public Timestamp getDateWork() {
    return dateWork;
  }

  public void setDateWork(Timestamp dateWork) {
    this.dateWork = dateWork;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Long getConfigId() {
    return configId;
  }

  public void setConfigId(Long configId) {
    this.configId = configId;
  }

  public String getConfigName() {
    return configName;
  }

  public void setConfigName(String configName) {
    this.configName = configName;
  }

  public int getRecAll() {
    return recAll;
  }

  public void setRecAll(int recAll) {
    this.recAll = recAll;
  }

  public int getRecErr() {
    return recErr;
  }

  public void setRecErr(int recErr) {
    this.recErr = recErr;
  }

  public ArrayList<String> getStatList() {
    return statList;
  }

  public void setStatList(ArrayList<String> statList) {
    this.statList = statList;
  }

}
