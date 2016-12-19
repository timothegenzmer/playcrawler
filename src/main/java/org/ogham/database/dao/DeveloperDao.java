package org.ogham.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.Application;
import org.ogham.database.model.DeveloperApplication;
import org.ogham.database.model.DeveloperApplicationStatistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timothe Genzmer 546765
 */
public class DeveloperDao extends BatchedCrawlerDao<DeveloperApplication, Long> {

  public DeveloperDao(Class<DeveloperApplication> dataClass) throws SQLException {
    super(dataClass);
  }

  public DeveloperDao(ConnectionSource connectionSource, Class<DeveloperApplication> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public DeveloperDao(ConnectionSource connectionSource, DatabaseTableConfig<DeveloperApplication> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  public List<String> getDeveloperIdsWithoutApps() throws SQLException {
    Dao<Application, String> apps = DaoManager.createDao(connectionSource, Application.class);
    DeveloperStatsDao statsDao = DaoManager.createDao(connectionSource, DeveloperApplicationStatistics.class);

    QueryBuilder<DeveloperApplicationStatistics, Long> statsQuery = statsDao.queryBuilder();
    statsQuery.selectColumns(DeveloperApplicationStatistics.DEVELOPER_ID);


    QueryBuilder<Application, String> appsQuery = apps.queryBuilder();
    appsQuery.where().isNotNull(Application.DEVELOPER_ID).and().notIn(Application.DEVELOPER_ID, statsQuery);
    appsQuery.selectColumns(Application.DEVELOPER_ID);
    appsQuery.distinct();

    GenericRawResults<String[]> devIds = appsQuery.queryRaw();

    List<String> developerIds = new ArrayList<>();
    devIds.forEach(row -> developerIds.add(row[0]));
    return developerIds;
  }
}
