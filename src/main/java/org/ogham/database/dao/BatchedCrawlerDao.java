package org.ogham.database.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import org.ogham.database.model.AbstractModelParent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchedCrawlerDao<T extends AbstractModelParent, ID> extends CrawlerDao<T, ID> {

  private static final int BATCH_SIZE = 1000;

  private List<T> buffer = new ArrayList<>(BATCH_SIZE);


  public BatchedCrawlerDao(Class<T> dataClass) throws SQLException {
    super(dataClass);
  }

  public BatchedCrawlerDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public BatchedCrawlerDao(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  @Override
  public synchronized int create(T data) throws SQLException {
    buffer.add(data);
    flushOnLimit();
    return 1;
  }

  @Override
  public synchronized void createAll(List<T> list) throws SQLException {
    buffer.addAll(list);
    flushOnLimit();
  }

  public synchronized void flush() throws SQLException {
    callBatchTasks(() -> {
      for (T data : buffer) {
        super.create(data);
      }
      return null;
    });
    buffer.clear();
  }

  private void flushOnLimit() throws SQLException {
    if (buffer.size() >= BATCH_SIZE) {
      flush();
    }
  }
}
