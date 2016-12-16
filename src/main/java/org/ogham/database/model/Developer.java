package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.CrawlerDao;

/**
 * @author Timothe Genzmer 546765
 */
@DatabaseTable(tableName = "developers", daoClass = CrawlerDao.class)
public class Developer extends AbstractModelParent {
  @DatabaseField(id = true)
  private String developerId;
  @DatabaseField
  private String developerName;

  public Developer() {
    // ORMLite needs a no-args constructor
  }

  public Developer(String developerId, String developerName) {
    this.developerId = developerId;
    this.developerName = developerName;
  }

  public String getDeveloperId() {
    return developerId;
  }

  public String getDeveloperName() {
    return developerName;
  }
}
