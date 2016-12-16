package org.ogham;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.ogham.database.model.*;
import org.ogham.play.PlayUrls;
import org.ogham.play.exceptions.PlayException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Timothe Genzmer 546765
 */
public class Main {

  public static void main(String[] args) {
    System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "WARNING");
    try {
      ConnectionSource connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:playcrawler.sqlite");

      setupDatabase(connectionSource);

      Crawler crawler = new Crawler(connectionSource);

      crawler.downloadTopLists(PlayUrls.DEFAULT_LOCALE);
      crawler.downloadTopListAppData();
      crawler.downloadSimilarApps();


    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (PlayException e) {
      e.printStackTrace();
    }
  }

  private static void setupDatabase(ConnectionSource connectionSource) throws SQLException {
    TableUtils.createTableIfNotExists(connectionSource, Application.class);
    TableUtils.createTableIfNotExists(connectionSource, Developer.class);
    TableUtils.createTableIfNotExists(connectionSource, SimilarApplication.class);
    TableUtils.createTableIfNotExists(connectionSource, SimilarApplicationStatistic.class);
    TableUtils.createTableIfNotExists(connectionSource, DeveloperApplication.class);
    TableUtils.createTableIfNotExists(connectionSource, TopApp.class);
    TableUtils.createTableIfNotExists(connectionSource, TopAppStatistics.class);

  }
}
