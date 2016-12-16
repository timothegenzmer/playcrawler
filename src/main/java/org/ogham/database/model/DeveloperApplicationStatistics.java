package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Timothe Genzmer 546765
 */
@DatabaseTable()
public class DeveloperApplicationStatistics extends SequentialModelParent {
  @DatabaseField(canBeNull = false)
  private String developerId;
  @DatabaseField
  private int numberOfApps;
  @DatabaseField
  private int statusCode;

  private DeveloperApplicationStatistics() {
  }

  public DeveloperApplicationStatistics(String developerId, int numberOfApps, int statusCode) {
    this.developerId = developerId;
    this.numberOfApps = numberOfApps;
    this.statusCode = statusCode;
  }
}
