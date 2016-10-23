package com.github.tnerevival.core.db.flat;

import java.io.Serializable;
import java.util.HashMap;

public class Section implements Serializable {

  private static final long serialVersionUID = 1L;

  HashMap<String, Article> articles = new HashMap<String, Article>();

  Integer version;
  String name;

  public Section(String name) {
    this.name = name;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addArticle(String name, Article article) {
    articles.put(name, article);
  }

  public HashMap<String, Article> getArticle() {
    return articles;
  }

  public Article getArticle(String name) {
    return articles.get(name);
  }
}