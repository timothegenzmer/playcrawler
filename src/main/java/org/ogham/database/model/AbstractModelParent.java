package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author Timothe Genzmer 546765
 */
public abstract class AbstractModelParent {
  public static final String CREATED_AT = "createdAt";

  @DatabaseField(canBeNull = false, columnName = CREATED_AT)
  private long createdAt;
  @DatabaseField(canBeNull = false)
  private long updatedAt;

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "AbstractModelParent{" +
        "createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
