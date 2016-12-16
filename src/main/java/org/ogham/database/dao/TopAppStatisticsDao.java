package org.ogham.database.dao;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.TopAppStatistics;
import org.ogham.play.PlayUrls;
import org.ogham.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopAppStatisticsDao extends CrawlerDao<TopAppStatistics, Long> {

  public static final long MINUTE = 60 * 1000;
  public static final long HOUR = 60 * MINUTE;
  public static final long DAY = 24 * HOUR;

  public TopAppStatisticsDao(Class<TopAppStatistics> dataClass) throws SQLException {
    super(dataClass);
  }

  public TopAppStatisticsDao(ConnectionSource connectionSource, Class<TopAppStatistics> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public TopAppStatisticsDao(ConnectionSource connectionSource, DatabaseTableConfig<TopAppStatistics> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  public List<Pair<String, String>> getCollectionsToDownload() throws SQLException {
    long threshold = System.currentTimeMillis() - DAY;
    QueryBuilder<TopAppStatistics, Long> query = queryBuilder();
    List<TopAppStatistics> collectionsNotToQuery = query.where().ge(TopAppStatistics.CREATED_AT, threshold).query();

    List<Pair<String, String>> toDownload = crossJoin();
    for (TopAppStatistics stat : collectionsNotToQuery) {
      toDownload.remove(new Pair<>(stat.getCategory(), stat.getCollection()));
    }
    return toDownload;
  }

  private List<Pair<String, String>> crossJoin() {
    String[] categories = PlayUrls.CATEGORIES;
    String[] collections = PlayUrls.COLLECTIONS;
    List<Pair<String, String>> toDownload = new ArrayList<>();
    for (String ca : categories) {
      for (String co : collections) {
        toDownload.add(new Pair<>(ca, co));
      }
    }
    return toDownload;
  }
}
