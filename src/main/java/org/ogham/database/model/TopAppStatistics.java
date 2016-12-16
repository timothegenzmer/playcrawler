package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.TopAppStatisticsDao;

@DatabaseTable(tableName = "top_app_statistics", daoClass = TopAppStatisticsDao.class)
public class TopAppStatistics extends SequentialModelParent {
  @DatabaseField(index = true, canBeNull = false)
  private String category;
  @DatabaseField(index = true, canBeNull = false)
  private String collection;
  @DatabaseField
  private int numberOfApps;
  @DatabaseField
  private int statuscode;
  @DatabaseField(index = true)
  private String locale;

  private TopAppStatistics() {
  }

  public TopAppStatistics(String category, String collection, int numberOfApps, int statuscode, String locale) {
    this.category = category;
    this.collection = collection;
    this.numberOfApps = numberOfApps;
    this.statuscode = statuscode;
    this.locale = locale;
  }

  public String getCategory() {
    return category;
  }

  public String getCollection() {
    return collection;
  }

  @Override
  public String toString() {
    return "TopAppStatistics{" +
        "category='" + category + '\'' +
        ", collection='" + collection + '\'' +
        ", numberOfApps=" + numberOfApps +
        ", statuscode=" + statuscode +
        ", locale='" + locale + '\'' +
        '}';
  }
}
