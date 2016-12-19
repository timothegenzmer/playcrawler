package org.ogham.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.ogham.database.dao.ApplicationDao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Timothe Genzmer 546765
 */
@DatabaseTable(tableName = "applications", daoClass = ApplicationDao.class)
public class Application extends AbstractModelParent {

  public static final String APP_ID = "id";
  public static final String DEVELOPER_ID = "developerId";


  private static final String ID_GROUP = "([a-zA-Z0-9_+,:~;'!?*/@$%().-]+?)";

  private static final Pattern patternCategory = Pattern.compile("<a class=\"document-subtitle category\" href=\"/store/apps/category/(\\w+?)\">");
  private static final Pattern patternDeveloper = Pattern.compile("<a class=\"document-subtitle primary\" href=\"/store/apps/developer\\?id=" + ID_GROUP + "\">");
  private static final Pattern patternDev = Pattern.compile("<a class=\"document-subtitle primary\" href=\"/store/apps/dev\\?id=" + ID_GROUP + "\">");
  private static final Pattern patternName = Pattern.compile("class=\"document-title\" itemprop=\"name\"> <div.*?>(.*?)</div>");
  private static final Pattern patternStar = Pattern.compile("<div class=\"rating-bar-container (five|four|three|two|one)\"> <span class=\"bar-label\"> <span class=\"star-tiny star-full\"></span>[1-5] </span> <span class=\"bar\" style=\"width:[0-9]{1,3}%\"></span> <span class=\"bar-number\".*?>([0-9.,]+)</span> </div>");
  private static final Pattern patternInApp = Pattern.compile("<div class=\"inapp-msg\">(.*?)</div>");

  private static final String ITEM_PROP_SOFTWARE_VERSION = "softwareVersion";
  private static final String ITEM_PROP_DATE_PUBLISHED = "datePublished";
  private static final String ITEM_PROP_NUM_DOWNLOADS = "numDownloads";
  private static final String ITEM_PROP_OPERATING_SYSTEM = "operatingSystems";
  private static final String ITEM_PROP_CONTENT_RATING = "contentRating";

  @DatabaseField(id = true, columnName = APP_ID)
  private String id;
  @DatabaseField
  private String category;
  @DatabaseField
  private String name;
  @DatabaseField(index = true, columnName = DEVELOPER_ID)
  private String developerId;
  @DatabaseField(index = true)
  private String devId;

  @DatabaseField
  private int oneStarRatings;
  @DatabaseField
  private int twoStarRatings;
  @DatabaseField
  private int threeStarRatings;
  @DatabaseField
  private int fourStarRatings;
  @DatabaseField
  private int fiveStarRatings;

  @DatabaseField
  private String version;
  @DatabaseField
  private String datePublished;
  @DatabaseField
  private String numDownloads;
  @DatabaseField
  private String operatingSystems;
  @DatabaseField
  private String contentRating;
  @DatabaseField
  private String inAppMsg;

  public Application() {
  }

  public static Application fromHTML(String id, String html) {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Invalid ID: " + id);
    }
    if (html == null || html.isEmpty()) {
      throw new IllegalArgumentException("Invalid HTML: " + id);
    }

    Application a = new Application();

    a.id = id;

    Matcher m = patternName.matcher(html);
    if (m.find()) {
      a.name = m.group(1);
    } else {
      System.out.println(html);
      throw new IllegalArgumentException("Could not find app's name for app: " + id);
    }

    m = patternCategory.matcher(html);
    if (m.find()) {
      a.category = m.group(1);
    } else {
      System.out.println(html);
      throw new IllegalArgumentException("Could not find app's category for app: " + id);
    }

    m = patternDeveloper.matcher(html);
    if (m.find()) {
      a.developerId = m.group(1);
    } else {
      m = patternDev.matcher(html);
      if (m.find()) {
        a.devId = m.group(1);
      } else {
        System.out.println(html);
        throw new IllegalArgumentException("Could not find app's developer for app: " + id);
      }
    }

    m = patternStar.matcher(html);

    if (m.find()) {
      do {
        String value = m.group(2);
        value = value.replaceAll("\\.|,", "");
        int intValue = Integer.valueOf(value);
        switch (m.group(1)) {
          case "five":
            a.fiveStarRatings = intValue;
            break;
          case "four":
            a.fourStarRatings = intValue;
            break;
          case "three":
            a.threeStarRatings = intValue;
            break;
          case "two":
            a.twoStarRatings = intValue;
            break;
          case "one":
            a.oneStarRatings = intValue;
            break;
          default:
            System.out.println(html);
            throw new IllegalArgumentException("Weird rating group " + m.group(1) + "for app: " + id);
        }
      } while (m.find());
    } else {
      //this seems to be a valid case
      //apparently there are apps without ratings
      //see: https://play.google.com/store/apps/details?id=de.vogel.friseur2
      //System.out.println(html);
      //throw new IllegalArgumentException("Could not find app's rating for app: " + id);
    }

    try {
      a.version = loadItemProp(ITEM_PROP_SOFTWARE_VERSION, html, id);
    } catch (IllegalArgumentException e) {
      //this seems to be a valid case
      //apparently there are apps without version
      //see: https://play.google.com/store/apps/details?id=com.inspiredapps.mydietcoachpro
    }
    try {
      a.datePublished = loadItemProp(ITEM_PROP_DATE_PUBLISHED, html, id);
    } catch (IllegalArgumentException e) {
      //this seems to be a valid case
      //apparently there are apps that have not been published yet
      //see: https://play.google.com/store/apps/details?id=net.ilius.android.fresh
    }

    try {
      a.numDownloads = loadItemProp(ITEM_PROP_NUM_DOWNLOADS, html, id);
    } catch (IllegalArgumentException e) {
      //this seems to be a valid case
      //apparently there are apps without downloads
      //see: https://play.google.com/store/apps/details?id=com.geeksdeck.thewatchingdead&amp;hl=en
    }
    try {
      a.operatingSystems = loadItemProp(ITEM_PROP_OPERATING_SYSTEM, html, id);
    } catch (IllegalArgumentException e) {
      //this seems to be a valid case
      //apparently there are apps that have not been published yet
      //see: https://play.google.com/store/apps/details?id=net.ilius.android.fresh
    }
    try {
      a.contentRating = loadItemProp(ITEM_PROP_CONTENT_RATING, html, id);
    } catch (IllegalArgumentException e) {
      //this seems to be a valid case
      //apparently there are apps that have not been published yet
      //see: https://play.google.com/store/apps/details?id=net.ilius.android.fresh
    }

    m = patternInApp.matcher(html);
    if (m.find()) {
      a.inAppMsg = m.group(1);
    }

    return a;
  }

  private static String loadItemProp(String property, String html, String appId) {
    Pattern patternitemProp = Pattern.compile("<div class=\"content\" itemprop=\"" + property + "\">(.+?)</div>");
    Matcher m = patternitemProp.matcher(html);
    if (m.find()) {
      return m.group(1).trim();
    } else {
      throw new IllegalArgumentException(appId + " Itemproperty " + property + " has not been found");
    }
  }

  public String getDeveloperId() {
    return developerId;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Application{" +
        "id='" + id + '\'' +
        ", category='" + category + '\'' +
        ", name='" + name + '\'' +
        ", developerId='" + developerId + '\'' +
        ", oneStarRatings=" + oneStarRatings +
        ", twoStarRatings=" + twoStarRatings +
        ", threeStarRatings=" + threeStarRatings +
        ", fourStarRatings=" + fourStarRatings +
        ", fiveStarRatings=" + fiveStarRatings +
        ", version='" + version + '\'' +
        ", datePublished='" + datePublished + '\'' +
        ", numDownloads='" + numDownloads + '\'' +
        ", operatingSystems='" + operatingSystems + '\'' +
        ", contentRating='" + contentRating + '\'' +
        "} " + super.toString();
  }
}
