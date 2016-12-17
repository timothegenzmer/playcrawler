package org.ogham.crawler.tasks;

import org.ogham.database.dao.TopAppStatisticsDao;
import org.ogham.database.dao.TopAppsDao;
import org.ogham.database.model.TopApp;
import org.ogham.database.model.TopAppStatistics;
import org.ogham.play.PlayClient;
import org.ogham.play.exceptions.PlayException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timothe Genzmer 546765
 */
public class CollectionTask extends CrawlerTask {

  private String collection;
  private String category;
  private String locale;

  private TopAppsDao topAppsDao;
  private TopAppStatisticsDao topAppStatisticsDao;

  public CollectionTask(PlayClient playClient,
                        TopAppsDao topAppsDao,
                        TopAppStatisticsDao topAppStatisticsDao,
                        String collection,
                        String category,
                        String locale) {
    super(playClient);

    this.collection = collection;
    this.category = category;
    this.locale = locale;

    this.topAppsDao = topAppsDao;
    this.topAppStatisticsDao = topAppStatisticsDao;
  }

  @Override
  public void run() {
    try {
      int statusCode = 200;
      int numberOfDownloads = 0;
      try {
        System.out.println("Download category " + category + " Download collection " + collection);
        List<String> topPaidApps = playClient.getCollection(collection, category, locale);
        storeTopApp(collection, category, topPaidApps, locale);
        numberOfDownloads = topPaidApps.size();
      } catch (PlayException e) {
        System.err.println(e.getMessage());
        statusCode = e.getStatuscode();
      }
      TopAppStatistics stats = new TopAppStatistics(category, collection, numberOfDownloads, statusCode, locale);
      topAppStatisticsDao.create(stats);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }

  private void storeTopApp(String collection, String category, List<String> topFreeApps, String locale) throws SQLException {
    int i = 1;
    List<TopApp> topApps = new ArrayList<>(topFreeApps.size());
    for (String app : topFreeApps) {
      topApps.add(new TopApp(category, app, i++, collection, locale));
    }
    topAppsDao.createAll(topApps);
  }
}
