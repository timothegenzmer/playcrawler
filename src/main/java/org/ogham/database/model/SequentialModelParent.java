package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author Timothe Genzmer 546765
 */
/*default*/abstract class SequentialModelParent extends AbstractModelParent {
  @DatabaseField(generatedId = true)
  private long id;

}
