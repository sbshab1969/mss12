package acp.db.domain;

import java.sql.Timestamp;

public class FileLoadClass {
  private Long id;
  private String name;
  private String md5;
  private Timestamp dateCreate;
  private Timestamp dateWork;
  private String owner;
  private String statisticR;
  private String statisticW;
  private Long configId;
  private int recAll;
  private int recErr;
  private int secAll;
  private int secErr;
  private ConfigClass config;

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

  public String getStatisticR() {
    return statisticR;
  }

  public void setStatisticR(String statisticR) {
    this.statisticR = statisticR;
  }

  public String getStatisticW() {
    return statisticW;
  }

  public void setStatisticW(String statisticW) {
    this.statisticW = statisticW;
  }

  public Long getConfigId() {
    return configId;
  }

  public void setConfigId(Long configId) {
    this.configId = configId;
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

  public int getSecAll() {
    return secAll;
  }

  public void setSecAll(int secAll) {
    this.secAll = secAll;
  }

  public int getSecErr() {
    return secErr;
  }

  public void setSecErr(int secErr) {
    this.secErr = secErr;
  }

  public ConfigClass getConfig() {
    return config;
  }

  public void setConfig(ConfigClass config) {
    this.config = config;
  }

}
