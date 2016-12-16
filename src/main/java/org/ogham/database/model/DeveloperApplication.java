package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.CrawlerDao;

/**
 * @author Timothe Genzmer 546765
 */
@DatabaseTable(tableName = "developer_application", daoClass = CrawlerDao.class)
public class DeveloperApplication extends SequentialModelParent {
  @DatabaseField(canBeNull = false, uniqueCombo = true)
  private String developerId;
  @DatabaseField(canBeNull = false, uniqueCombo = true)
  private String applicationId;

  public DeveloperApplication() {}

  public DeveloperApplication(String developerId, String applicationId) {
    this.developerId = developerId;
    this.applicationId = applicationId;
  }

  public String getDeveloperId() {
    return developerId;
  }

  public String getApplicationId() {
    return applicationId;
  }

  @Override
  public String toString() {
    return "DeveloperApplication{" +
        "developerId='" + developerId + '\'' +
        ", applicationId='" + applicationId + '\'' +
        '}';
  }
}
