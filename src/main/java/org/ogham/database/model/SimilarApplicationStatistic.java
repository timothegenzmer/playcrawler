package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.SimilarAppsStatisticsDao;

@DatabaseTable(tableName = "similar_application_stat", daoClass = SimilarAppsStatisticsDao.class)
public class SimilarApplicationStatistic extends SequentialModelParent {
  @DatabaseField
  private String appId;
  @DatabaseField
  private int numberOfApps;
  @DatabaseField
  private int statusCode;

  public SimilarApplicationStatistic() {
  }

  public SimilarApplicationStatistic(String appId, int numberOfApps, int statusCode) {
    this.appId = appId;
    this.numberOfApps = numberOfApps;
    this.statusCode = statusCode;
  }

  @Override
  public String toString() {
    return "SimilarApplicationStatistic{" +
        "appId='" + appId + '\'' +
        ", numberOfApps=" + numberOfApps +
        ", statuscode=" + statusCode +
        '}';
  }
}
