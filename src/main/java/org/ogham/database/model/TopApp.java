package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.TopAppsDao;

@DatabaseTable(tableName = "top_app", daoClass = TopAppsDao.class)
public class TopApp extends SequentialModelParent {

  public static final String APP_ID = "appId";

  @DatabaseField(index = true, canBeNull = false)
  private String category;
  @DatabaseField(index = true, canBeNull = false, columnName = APP_ID)
  private String appId;
  @DatabaseField
  private int position;
  @DatabaseField(index = true, canBeNull = false)
  private String collection;
  @DatabaseField(index = true)
  private String locale;


  @SuppressWarnings("unused")
  private TopApp() {
  }

  public TopApp(String category, String appId, int position, String collection, String locale) {
    this.category = category;
    this.appId = appId;
    this.position = position;
    this.collection = collection;
    this.locale = locale;
  }

  public String getAppId() {
    return appId;
  }

  @Override
  public String toString() {
    return "TopApp{" +
        "category='" + category + '\'' +
        ", appId='" + appId + '\'' +
        ", position=" + position +
        ", collection='" + collection + '\'' +
        ", locale='" + locale + '\'' +
        '}';
  }
}
