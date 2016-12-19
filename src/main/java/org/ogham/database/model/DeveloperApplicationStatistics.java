package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.DeveloperStatsDao;

import java.util.Objects;

/**
 * @author Timothe Genzmer 546765
 */
@DatabaseTable(tableName = "developer_application_stats", daoClass = DeveloperStatsDao.class)
public class DeveloperApplicationStatistics extends SequentialModelParent {

  public static final String DEVELOPER_ID = "developerId";

  @DatabaseField(canBeNull = false, columnName = DEVELOPER_ID)
  private String developerId;
  @DatabaseField
  private int numberOfApps;
  @DatabaseField
  private int statusCode;

  private DeveloperApplicationStatistics() {
  }

  public DeveloperApplicationStatistics(String developerId, int numberOfApps, int statusCode) {
    Objects.requireNonNull(developerId);

    this.developerId = developerId;
    this.numberOfApps = numberOfApps;
    this.statusCode = statusCode;
  }
}
