package org.ogham.database.dao;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.Application;
import org.ogham.util.Decoder;

import java.sql.SQLException;
import java.util.List;

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



  public void decodeDevId() throws SQLException {
    long numberOfRows = 100000;
    long count = countOf();
    for (long i = 0; i < count; i += numberOfRows) {
      QueryBuilder<Application, String> query = queryBuilder();
      query.limit(numberOfRows);
      query.offset(i);
      query.where().isNotNull(Application.DEVELOPER_ID);
      List<Application> apps = query.query();

      apps.forEach(app -> app.setDeveloperId(Decoder.decode(app.getDeveloperId())));

      callBatchTasks(() -> {
        for (Application data : apps) {
          update(data);
        }
        return null;
      });
    }
  }

}
