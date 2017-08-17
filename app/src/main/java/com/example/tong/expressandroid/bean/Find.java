package com.example.tong.expressandroid.bean;

import java.io.Serializable;

public class Find implements Serializable {
  private int findid;
  private String findtitle;
  private String findpage;

  public int getFindid() {
    return findid;
  }

  public void setFindid(int findid) {
    this.findid = findid;
  }

  public String getFindtitle() {
    return findtitle;
  }

  public void setFindtitle(String findtitle) {
    this.findtitle = findtitle;
  }

  public String getFindpage() {
    return findpage;
  }

  public void setFindpage(String findpage) {
    this.findpage = findpage;
  }
}
