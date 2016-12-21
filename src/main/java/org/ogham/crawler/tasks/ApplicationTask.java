package org.ogham.crawler.tasks;

import org.ogham.database.dao.ApplicationDao;
import org.ogham.database.model.Application;
import org.ogham.play.PlayClient;
import org.ogham.play.exceptions.PlayException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Timothe Genzmer 546765
 */
public class ApplicationTask extends CrawlerTask {

  private String appId;

  private ApplicationDao appDao;

  public ApplicationTask(PlayClient playClient, ApplicationDao appDao, String appId) {
    super(playClient);
    this.appDao = appDao;
    this.appId = appId;
  }


  @Override
  public void run() {
    try {
      //System.out.println("Download and store app: " + appId);
      Application app = playClient.getApplication(appId);
      appDao.create(app);
    } catch (PlayException e) {
      System.err.println(e.getMessage());
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }
}
