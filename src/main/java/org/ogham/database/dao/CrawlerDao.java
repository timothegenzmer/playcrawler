package org.ogham.database.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.AbstractModelParent;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Timothe Genzmer 546765
 */
public class CrawlerDao<T extends AbstractModelParent, ID> extends BaseDaoImpl<T, ID> {

  public CrawlerDao(Class<T> dataClass) throws SQLException {
    super(dataClass);
  }

  public CrawlerDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public CrawlerDao(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  @Override
  public int create(T data) throws SQLException {
    data.setCreatedAt(System.currentTimeMillis());
    data.setUpdatedAt(System.currentTimeMillis());
    return super.create(data);
  }

  @Override
  public int update(T data) throws SQLException {
    data.setUpdatedAt(System.currentTimeMillis());
    return super.update(data);
  }

  @Override
  public int updateId(T data, ID newId) throws SQLException {
    data.setUpdatedAt(System.currentTimeMillis());
    return super.updateId(data, newId);
  }

  public void createAll(List<T> list) throws SQLException {
    callBatchTasks(() -> {
      for (T data : list) {
        create(data);
      }
      return null;
    });
  }
}
