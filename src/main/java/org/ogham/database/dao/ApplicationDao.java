package org.ogham.database.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.Application;

import java.sql.SQLException;

/**
 * @author Timothe Genzmer 546765
 */
public class ApplicationDao extends BatchedCrawlerDao<Application, String> {
  public ApplicationDao(Class<Application> dataClass) throws SQLException {
    super(dataClass);
  }

  public ApplicationDao(ConnectionSource connectionSource, Class<Application> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public ApplicationDao(ConnectionSource connectionSource, DatabaseTableConfig<Application> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

}
