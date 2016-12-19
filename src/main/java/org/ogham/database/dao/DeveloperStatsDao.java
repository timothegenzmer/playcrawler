package org.ogham.database.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.DeveloperApplicationStatistics;

import java.sql.SQLException;

/**
 * @author Timothe Genzmer 546765
 */
public class DeveloperStatsDao extends BatchedCrawlerDao<DeveloperApplicationStatistics, Long> {
  public DeveloperStatsDao(Class<DeveloperApplicationStatistics> dataClass) throws SQLException {
    super(dataClass);
  }

  public DeveloperStatsDao(ConnectionSource connectionSource, Class<DeveloperApplicationStatistics> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public DeveloperStatsDao(ConnectionSource connectionSource, DatabaseTableConfig<DeveloperApplicationStatistics> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }
}
