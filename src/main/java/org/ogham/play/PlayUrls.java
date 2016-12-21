package org.ogham.play;


import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Timothe Genzmer 546765
 */
public class PlayUrls {

  private static final String LANGUAGE_PARAM = "hl";
  private static final String DEFAULT_LANGUAGE = "de";
  private static final String[] LANGUAGES = {"en", "ar", "bg", "ca", "cs", "da", "de", "el", "es", "et", "fi", "fr", "hi",
      "hr", "hu", "id", "it", "ja", "ko", "lt", "lv", "ms", "nl", "no", "pl", "pt", "ro", "ru", "sk", "sr", "sv", "th",
      "tr", "uk", "vi", "zh-cn", "zh-tw"};
  private static final String LOCALE_PARAM = "gl";
  public static final String DEFAULT_LOCALE = "DE";
  public static final String[] LOCALES = {"AE", "AR", "AT", "AU", "BE", "BG", "BR", "CA", "CH", "CL", "CN", "CO", "CZ",
      "DE", "DK", "EE", "EG", "ES", "FI", "FR", "GB", "GR", "HK", "HR", "HU", "ID", "IL", "IN", "IT", "JP", "KR", "LT",
      "LV", "MX", "MY", "NL", "NO", "NZ", "PE", "PH", "PL", "PT", "RO", "RS", "RU", "SA", "SE", "SG", "SK", "TH", "TR",
      "TW", "UA", "US", "VN", "ZA"};


  private static final String SCHEMA = "https";
  private static final String HOST = "play.google.com";
  private static final String BASE_PATH = "/store/apps";

  //collections
  private static final String START_PARAM = "start";
  private static final String NUM_PARAM = "num";

  private static final String CATEGORY_URL = "/category/";
  /**
   * Array of all known categories. null means no category
   */
  public static final String[] CATEGORIES = {"ALL", "ANDROID_WEAR", "AUTO_AND_VEHICLES", "BEAUTY", "BOOKS_AND_REFERENCE", "BUSINESS",
      "COMICS", "DATING", "PRODUCTIVITY", "PARENTING", "FOOD_AND_DRINK", "FINANCE", "PHOTOGRAPHY", "HEALTH_AND_FITNESS",
      "HOUSE_AND_HOME", "MAPS_AND_NAVIGATION", "COMMUNICATION", "ART_AND_DESIGN", "EDUCATION", "LIFESTYLE", "MEDICAL",
      "MUSIC_AND_AUDIO", "NEWS_AND_MAGAZINES", "PERSONALIZATION", "TRAVEL_AND_LOCAL", "SHOPPING", "LIBRARIES_AND_DEMO",
      "SOCIAL", "SPORTS", "EVENTS", "TOOLS", "ENTERTAINMENT", "VIDEO_PLAYERS", "WEATHER", "GAME", "GAME_ADVENTURE", "GAME_ACTION",
      "GAME_ARCADE", "GAME_BOARD", "GAME_PUZZLE", "GAME_CASUAL", "GAME_CARD", "GAME_CASINO", "GAME_EDUCATIONAL", "GAME_MUSIC",
      "GAME_TRIVIA", "GAME_RACING", "GAME_ROLE_PLAYING", "GAME_SIMULATION", "GAME_SPORTS", "GAME_STRATEGY", "GAME_WORD",
      "FAMILY", "FAMILY_ACTION", "FAMILY_EDUCATION", "FAMILY_BRAINGAMES", "FAMILY_CREATE", "FAMILY_MUSICVIDEO", "FAMILY_PRETEND"};
  private static final String COLLECTION = "/collection/";

  public static final String TOP_SELLING = "topselling_paid";
  public static final String TOP_FREE = "topselling_free";
  public static final String TOP_GROSSING = "topgrossing";
  public static final String TOP_NEW_FREE = "topselling_new_free";
  public static final String TOP_NEW_PAID = "topselling_new_paid";
  public static final String MOVERS_SHAKERS = "movers_shakers";
  public static final String[] COLLECTIONS = {TOP_SELLING, TOP_FREE, TOP_GROSSING, TOP_NEW_FREE, TOP_NEW_PAID, MOVERS_SHAKERS};

  private static final String ID_PARAM = "id";
  private static final String APP_URL = BASE_PATH + "/details";

  private static final String SIMILAR_URL = BASE_PATH + "/similar";

  private static final String DEVELOPER_URL = BASE_PATH + "/developer";

  public static URI getBaseUri() {
    try {
      return protoUriBuilder(BASE_PATH).build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static URI getCollectionUri(String category, String collection, int start, int num, String locale) {
    if (start < 0 || start > 500) {
      throw new IllegalArgumentException("start has to be between 0 and 500 but was: " + start);
    }
    if (num < 0 || num > 120) {
      throw new IllegalArgumentException("num has to be between 0 and 120 but was: " + num);
    }
    URIBuilder builder = protoUriBuilder(BASE_PATH + ("ALL".equals(category) ? "" : CATEGORY_URL + category) + COLLECTION + collection);
    builder.addParameter(START_PARAM, "" + start);
    builder.addParameter(NUM_PARAM, "" + num);
    builder.addParameter(LOCALE_PARAM, locale);
    try {
      return builder.build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static URI getAppUri(String appId) {
    return getIdRoute(APP_URL, appId);
  }

  public static URI getSimilarAppUri(String appId) {
    return getIdRoute(SIMILAR_URL, appId);
  }

  public static URI getDeveloperUri(String developerId) {
    return getIdRoute(DEVELOPER_URL, developerId);
  }

  private static URI getIdRoute(String path, String id) {
    try {
      return protoUriBuilder(path).addParameter(ID_PARAM, id).build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static URIBuilder protoUriBuilder(String path) {
    URIBuilder builder = new URIBuilder();
    builder.setScheme(SCHEMA);
    builder.setHost(HOST);
    builder.setPath(path);
    builder.addParameter(LANGUAGE_PARAM, DEFAULT_LANGUAGE);
    return builder;
  }


}
