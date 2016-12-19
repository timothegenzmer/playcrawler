package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.SimilarAppsDao;

/**
 * @author Timothe Genzmer 546765
 */
@DatabaseTable(tableName = "similar_application", daoClass = SimilarAppsDao.class)
public class SimilarApplication extends SequentialModelParent {

  public static final String APPLICATION_ID = "applicationId";
  public static final String SIMILAR_APP_ID = "similarAppId";

  @DatabaseField(canBeNull = false, uniqueCombo = true, index = true, columnName = APPLICATION_ID)
  private String applicationId;
  @DatabaseField(canBeNull = false, uniqueCombo = true, index = true, columnName = SIMILAR_APP_ID)
  private String similarAppId;

  @SuppressWarnings("unused")
  public SimilarApplication() {
  }

  public SimilarApplication(String applicationId, String similarAppId) {
    this.applicationId = applicationId;
    this.similarAppId = similarAppId;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public String getSimilarAppId() {
    return similarAppId;
  }

  @Override
  public String toString() {
    return "SimilarApplication{" +
        "applicationId='" + applicationId + '\'' +
        ", similarAppId='" + similarAppId + '\'' +
        '}';
  }
}
