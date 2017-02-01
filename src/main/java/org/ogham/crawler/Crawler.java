package org.ogham.crawler;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.ogham.crawler.tasks.ApplicationTask;
import org.ogham.crawler.tasks.CollectionTask;
import org.ogham.crawler.tasks.DeveloperTask;
import org.ogham.crawler.tasks.SimilarAppsTask;
import org.ogham.database.dao.*;
import org.ogham.database.model.*;
import org.ogham.play.PlayClient;
import org.ogham.play.exceptions.PlayException;
import org.ogham.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Timothe Genzmer 546765
 */
public class Crawler {

  private static final int NUMBER_OF_THREADS = 200;

  private List<String> categoriesToCrawl = new ArrayList<>();

  private ApplicationDao appDao;

  private DeveloperDao developerDao;
  private DeveloperStatsDao developerStatsDao;

  private TopAppsDao topAppDao;
  private TopAppStatisticsDao topAppStatisticsDao;

  private SimilarAppsDao similarAppsDao;
  private SimilarAppsStatisticsDao similarAppsStatisticsDao;

  private ExecutorService executor;

  private PlayClient client = new PlayClient(NUMBER_OF_THREADS);

  public Crawler(ConnectionSource connectionSource) throws SQLException, IOException, PlayException {
    appDao = DaoManager.createDao(connectionSource, Application.class);

    developerDao = DaoManager.createDao(connectionSource, DeveloperApplication.class);
    developerStatsDao = DaoManager.createDao(connectionSource, DeveloperApplicationStatistics.class);

    similarAppsDao = DaoManager.createDao(connectionSource, SimilarApplication.class);
    similarAppsStatisticsDao = DaoManager.createDao(connectionSource, SimilarApplicationStatistic.class);

    topAppDao = DaoManager.createDao(connectionSource, TopApp.class);
    topAppStatisticsDao = DaoManager.createDao(connectionSource, TopAppStatistics.class);

    categoriesToCrawl = client.getCategories();
    System.out.println("Categories: " + categoriesToCrawl);
    if (categoriesToCrawl.isEmpty()) {
      throw new IllegalStateException("No Categories found");
    }
  }


  /**
   * Downloads all app data which was found in the top lists that is not downloaded yet
   *
   * @throws SQLException
   * @throws IOException
   */
  public void downloadTopListAppData() throws SQLException {
    List<String> appIds = topAppDao.getTopAppsNotDownloaded();
    downloadsApps(appIds);
  }

  /**
   * Downloads toplists for all Categories and all collections that have not been downloaded in the last day
   *
   * @param locale
   * @throws IOException
   * @throws SQLException
   */
  public void downloadTopLists(String locale) throws SQLException {
    System.out.println("Download Top Lists");
    List<Pair<String, String>> toDownload = topAppStatisticsDao.getCollectionsToDownload();

    initExecutor();
    toDownload.forEach(s -> executor.execute(getCollectionTask(s.k, s.v, locale)));

    waitForExecutor();
    topAppDao.flush();
    topAppStatisticsDao.flush();
  }

  private CollectionTask getCollectionTask(String category, String collection, String locale) {
    return new CollectionTask(client, topAppDao, topAppStatisticsDao, collection, category, locale);
  }

  public void downloadSimilarApps() throws SQLException {
    System.out.println("Download Similar Apps");
    List<String> appIds = similarAppsDao.getAppIdsWithoutSimilarApps();

    initExecutor();
    appIds.forEach(s -> executor.execute(getSimilarAppsTask(s)));

    waitForExecutor();

    similarAppsDao.flush();
    similarAppsStatisticsDao.flush();
  }

  private SimilarAppsTask getSimilarAppsTask(String appId) {
    return new SimilarAppsTask(client, similarAppsDao, similarAppsStatisticsDao, appId);
  }

  public void downloadSimilarAppsWithoutApp() throws SQLException {
    System.out.println("Download SimilarApps Without App");
    List<String> appIds = similarAppsDao.getAppIdsWithoutApps();
    downloadsApps(appIds);
  }

  public void downloadDeveloperApps() throws SQLException {
    System.out.println("Download Developer Apps");
    List<String> developerIds = developerDao.getDeveloperIdsWithoutApps();
    initExecutor();
    developerIds.forEach(s -> {
      executor.execute(getDeveloperTask(s));
    });

    waitForExecutor();

    developerDao.flush();
    developerStatsDao.flush();
  }

  private DeveloperTask getDeveloperTask(String developerId) {
    return new DeveloperTask(client, developerDao, developerStatsDao, developerId);
  }

  public void getDeveloperAppsNotDownloaded() throws SQLException {
    System.out.println("Download Developer Apps Without App");
    List<String> appIds = developerDao.getAppIdsWithoutApp();
    downloadsApps(appIds);
  }


  private void downloadsApps(List<String> appIds) throws SQLException {
    initExecutor();
    appIds.forEach(s -> executor.execute(new ApplicationTask(client, appDao, s)));

    waitForExecutor();
    appDao.flush();
  }

  private void initExecutor() {
    this.executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  }

  private void waitForExecutor() {
    System.out.println("Wait for Executor");
    try {
      executor.shutdown();
      executor.awaitTermination(1, TimeUnit.DAYS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Executor finished");
  }
}
