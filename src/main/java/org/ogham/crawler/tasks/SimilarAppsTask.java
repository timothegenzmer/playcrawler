package org.ogham.crawler.tasks;

import org.ogham.database.dao.SimilarAppsDao;
import org.ogham.database.dao.SimilarAppsStatisticsDao;
import org.ogham.database.model.SimilarApplication;
import org.ogham.database.model.SimilarApplicationStatistic;
import org.ogham.play.PlayClient;
import org.ogham.play.exceptions.PlayException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Timothe Genzmer 546765
 */
public class SimilarAppsTask extends CrawlerTask {

  private String appId;
  private SimilarAppsDao similarAppsDao;
  private SimilarAppsStatisticsDao similarAppsStatisticsDao;


  public SimilarAppsTask(PlayClient playClient,
                         SimilarAppsDao similarAppsDao,
                         SimilarAppsStatisticsDao similarAppsStatisticsDao,
                         String appId) {
    super(playClient);
    this.appId = appId;
    this.similarAppsDao = similarAppsDao;
    this.similarAppsStatisticsDao = similarAppsStatisticsDao;
  }

  @Override
  public void run() {
    try {
      int statuscode = 200;
      int similarAppsSize = 0;
      try {
        System.out.println("Download similar apps: " + appId);
        List<SimilarApplication> similarApps = playClient.getSimilarApplications(appId);
        similarAppsDao.createAll(similarApps);
        similarAppsSize = similarApps.size();
      } catch (PlayException e) {
        System.err.println(e.getMessage());
        statuscode = e.getStatuscode();
      }
      SimilarApplicationStatistic statistic = new SimilarApplicationStatistic(appId, similarAppsSize, statuscode);
      similarAppsStatisticsDao.create(statistic);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }
}
