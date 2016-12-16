package org.ogham;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.ogham.database.dao.*;
import org.ogham.database.model.*;
import org.ogham.play.PlayClient;
import org.ogham.play.exceptions.PlayException;
import org.ogham.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Timothe Genzmer 546765
 */
public class Crawler {

  private List<String> categoriesToCrawl = new ArrayList<>();

  private ApplicationDao appDao;
  private CrawlerDao<Developer, String> developerDao;
  private TopAppsDao topAppDao;
  private TopAppStatisticsDao topAppStatisticsDao;

  private SimilarAppsDao similarAppsDao;
  private SimilarAppsStatisticsDao similarAppsStatisticsDao;

  private PlayClient client = new PlayClient();

  public Crawler(ConnectionSource connectionSource) throws SQLException, IOException, PlayException {
    appDao = DaoManager.createDao(connectionSource, Application.class);
    developerDao = DaoManager.createDao(connectionSource, Developer.class);
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
  public void downloadTopListAppData() throws SQLException, IOException {
    List<String> appIds = topAppDao.getTopAppsNotDownloaded();
    appIds.parallelStream().flatMap(appId -> {
      try {
        System.out.println("Download and store app: " + appId);
        loadAndStoreApp(appId);
      } catch (PlayException e) {
        System.err.println(e.getMessage());
      } catch (SQLException | IOException | IllegalArgumentException e) {
        return Stream.of(e);
      }
      return null;
    }).reduce((ex1, ex2) -> {
      ex1.addSuppressed(ex2);
      return ex1;
    }).ifPresent(ex -> {
      ex.printStackTrace();
    });
  }

  private void loadAndStoreApp(String appId) throws IOException, SQLException, PlayException {
    Application app = client.getApplication(appId);
    appDao.createIfNotExists(app);
  }

  /**
   * Downloads toplists for all Categories and all collections that have not been downloaded in the last day
   *
   * @param locale
   * @throws IOException
   * @throws SQLException
   */
  public void downloadTopLists(String locale) throws IOException, SQLException {
    List<Pair<String, String>> toDownload = topAppStatisticsDao.getCollectionsToDownload();

    toDownload.parallelStream().flatMap(pair -> {
      try {
        String category = pair.k;
        String collection = pair.v;
        System.out.println("Download category " + category + " Download collection " + collection);
        downloadAndStoreCollection(category, collection, locale);
        return null;
      } catch (SQLException | IOException e) {
        return Stream.of(e);
      }
    }).reduce((ex1, ex2) -> {
      ex1.addSuppressed(ex2);
      return ex1;
    }).ifPresent(ex -> {
      ex.printStackTrace();
    });
  }

  private void downloadAndStoreCollection(String category, String collection, String locale) throws SQLException, IOException {
    List<String> topPaidApps = null;
    int statusCode = 200;
    int numberOfDownloads = 0;
    try {
      topPaidApps = client.getCollection(collection, category, locale);
      storeTopApp(collection, category, topPaidApps, locale);
      numberOfDownloads = topPaidApps.size();
    } catch (PlayException e) {
      System.err.println(e.getMessage());
      statusCode = e.getStatuscode();
    }
    TopAppStatistics stats = new TopAppStatistics(category, collection, numberOfDownloads, statusCode, locale);
    topAppStatisticsDao.create(stats);
  }

  private void storeTopApp(String collection, String category, List<String> topFreeApps, String locale) throws SQLException {
    int i = 1;
    List<TopApp> topApps = new ArrayList<>();
    for (String app : topFreeApps) {
      topApps.add(new TopApp(category, app, i++, collection, locale));
    }
    topAppDao.createAll(topApps);
  }

  public void downloadSimilarApps() throws SQLException {
    List<String> appIds = similarAppsDao.getAppIdsWithoutSimilarApps();
    appIds.parallelStream().flatMap(appId -> {
      try {
        int statuscode = 200;
        int similarAppsSize = 0;
        try {
          System.out.println("Download similar apps: " + appId);
          List<SimilarApplication> similarApps = client.getSimilarApplications(appId);
          similarAppsDao.createAll(similarApps);
          similarAppsSize = similarApps.size();
        } catch (PlayException e) {
          System.err.println(e.getMessage());
          statuscode = e.getStatuscode();
        }
        SimilarApplicationStatistic statistic = new SimilarApplicationStatistic(appId, similarAppsSize, statuscode);
        similarAppsStatisticsDao.create(statistic);
        return null;
      } catch (SQLException | IOException e) {
        e.printStackTrace();
        return Stream.of(e);
      }
    }).reduce((ex1, ex2) -> {
      ex1.addSuppressed(ex2);
      return ex1;
    }).ifPresent(ex -> {
      throw new RuntimeException(ex);
    });
    similarAppsDao.flush();
    similarAppsStatisticsDao.flush();
  }

  public void downloadSimilarAppsWithoutApp() throws SQLException {
    List<String> appIds = similarAppsDao.getAppIdsWithoutApps();
    appIds.parallelStream().flatMap(appId -> {
      try {
        loadAndStoreApp(appId);
        return null;
      } catch (IOException | SQLException | PlayException e) {
        e.printStackTrace();
        return Stream.of(e);
      }
    }).reduce((ex1, ex2) -> {
      ex1.addSuppressed(ex2);
      return ex1;
    }).ifPresent(ex -> {
      throw new RuntimeException(ex);
    });
    appDao.flush();
  }
}
