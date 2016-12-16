package org.ogham.database.dao;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.Application;
import org.ogham.database.model.TopApp;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TopAppsDao extends CrawlerDao<TopApp, Long> {
  public TopAppsDao(Class<TopApp> dataClass) throws SQLException {
    super(dataClass);
  }

  public TopAppsDao(ConnectionSource connectionSource, Class<TopApp> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public TopAppsDao(ConnectionSource connectionSource, DatabaseTableConfig<TopApp> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  public List<String> getTopAppsNotDownloaded() throws SQLException {
    CrawlerDao<Application, String> appDao = DaoManager.createDao(connectionSource, Application.class);
    QueryBuilder<Application, String> query = appDao.queryBuilder();
    query.selectColumns(Application.APP_ID);

    List<TopApp> topApps = queryBuilder().where().notIn(TopApp.APP_ID, query).query();

    return topApps.stream().map(TopApp::getAppId).distinct().collect(Collectors.toList());
  }
}
