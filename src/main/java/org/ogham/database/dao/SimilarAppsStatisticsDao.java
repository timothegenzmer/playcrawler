package org.ogham.database.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.SimilarApplicationStatistic;

import java.sql.SQLException;

/**
 * @author Timothe Genzmer 546765
 */
public class SimilarAppsStatisticsDao extends BatchedCrawlerDao<SimilarApplicationStatistic, Long> {
  public SimilarAppsStatisticsDao(Class<SimilarApplicationStatistic> dataClass) throws SQLException {
    super(dataClass);
  }

  public SimilarAppsStatisticsDao(ConnectionSource connectionSource, Class<SimilarApplicationStatistic> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public SimilarAppsStatisticsDao(ConnectionSource connectionSource, DatabaseTableConfig<SimilarApplicationStatistic> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }


}
