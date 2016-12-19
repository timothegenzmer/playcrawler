package org.ogham.crawler.tasks;

import org.ogham.database.dao.DeveloperDao;
import org.ogham.database.dao.DeveloperStatsDao;
import org.ogham.database.model.DeveloperApplication;
import org.ogham.database.model.DeveloperApplicationStatistics;
import org.ogham.play.PlayClient;
import org.ogham.play.exceptions.PlayException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Timothe Genzmer 546765
 */
public class DeveloperTask extends CrawlerTask {

  private String developerId;
  private DeveloperDao developerDao;
  private DeveloperStatsDao developerStatsDao;


  public DeveloperTask(PlayClient playClient,
                       DeveloperDao developerDao,
                       DeveloperStatsDao developerStatsDao,
                       String developerId) {
    super(playClient);
    this.developerId = developerId;
    this.developerDao = developerDao;
    this.developerStatsDao = developerStatsDao;
  }

  @Override
  public void run() {
    try {
      int statuscode = 200;
      int developerAppsSize = 0;
      try {
        System.out.println("Download developer apps: " + developerId);
        List<DeveloperApplication> developerApps = playClient.getDevelopperApplications(developerId);
        developerDao.createAll(developerApps);
        developerAppsSize = developerApps.size();
      } catch (PlayException e) {
        System.err.println(e.getMessage());
        statuscode = e.getStatuscode();
      }
      DeveloperApplicationStatistics statistic = new DeveloperApplicationStatistics(developerId, developerAppsSize, statuscode);
      developerStatsDao.create(statistic);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }
}
