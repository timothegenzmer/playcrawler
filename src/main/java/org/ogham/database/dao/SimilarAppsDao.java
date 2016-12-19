package org.ogham.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.Application;
import org.ogham.database.model.SimilarApplication;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Timothe Genzmer 546765
 */
public class SimilarAppsDao extends BatchedCrawlerDao<SimilarApplication, Long> {

  public SimilarAppsDao(Class<SimilarApplication> dataClass) throws SQLException {
    super(dataClass);
  }

  public SimilarAppsDao(ConnectionSource connectionSource, Class<SimilarApplication> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public SimilarAppsDao(ConnectionSource connectionSource, DatabaseTableConfig<SimilarApplication> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  public List<String> getAppIdsWithoutSimilarApps() throws SQLException {
    Dao<Application, String> apps = DaoManager.createDao(connectionSource, Application.class);

    QueryBuilder<Application, String> query = apps.queryBuilder();
    query.where().notIn(Application.APP_ID, queryBuilder().selectColumns(SimilarApplication.APPLICATION_ID).distinct());

    List<Application> appList = query.query();
    return appList.stream().map(Application::getId).collect(Collectors.toList());
  }

  public List<String> getAppIdsWithoutApps() throws SQLException {
    Dao<Application, String> apps = DaoManager.createDao(connectionSource, Application.class);

    QueryBuilder<SimilarApplication, Long> query = queryBuilder();
    query.where().notIn(SimilarApplication.SIMILAR_APP_ID, apps.queryBuilder().selectColumns(Application.APP_ID));

    List<SimilarApplication> appList = query.query();
    return appList.stream().map(SimilarApplication::getSimilarAppId).distinct().collect(Collectors.toList());
  }
}
