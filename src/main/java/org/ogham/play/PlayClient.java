package org.ogham.play;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ogham.database.model.Application;
import org.ogham.database.model.SimilarApplication;
import org.ogham.play.exceptions.ContentForbidden;
import org.ogham.play.exceptions.ContentNotFound;
import org.ogham.play.exceptions.PlayException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Timothe Genzmer 546765
 */
public class PlayClient {

  private static final Pattern categoryPattern = Pattern.compile("href=\"/store/apps/category/(\\w+)\" ");
  private static final Pattern categoryTopSellingFreePattern = Pattern.compile("class=\"title\" href=\"/store/apps/details\\?id=(.*?)\"");

  private CloseableHttpClient client;

  public PlayClient() {
    RequestConfig.Builder requestConfig = RequestConfig.custom();
    requestConfig.setConnectTimeout(30 * 1000);
    requestConfig.setConnectionRequestTimeout(30 * 1000);
    requestConfig.setSocketTimeout(30 * 1000);


    HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    clientBuilder.setDefaultRequestConfig(requestConfig.build());
    clientBuilder.disableCookieManagement();
    clientBuilder.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
    client = clientBuilder.build();
  }

  public List<String> getCategories() throws IOException, PlayException {
    String mainPage = getHTML(PlayUrls.getBaseUri());

    Matcher m = categoryPattern.matcher(mainPage);

    List<String> categories = new ArrayList<>();
    while (m.find()) {
      categories.add(m.group(1));
    }

    return categories;
  }

  public List<String> getCollection(String collection, String category, String locale) throws IOException, PlayException {
    List<String> results = new ArrayList<>();
    int numberOfApps = 100;
    for (int i = 0; i <= 500; i += numberOfApps) {
      URI collectionUri = PlayUrls.getCollectionUri(category, collection, i, numberOfApps, locale);
      String html = getHTML(collectionUri);
      Matcher m = categoryTopSellingFreePattern.matcher(html);
      while (m.find()) {
        results.add(m.group(1));
      }
      if (results.size() != i + numberOfApps) {
        //System.out.println(category + " " + collection + " is exhausted at " + appIds.size());
        //collection is exhausted
        break;
      }
    }
    return results;
  }

  public Application getApplication(String id) throws IOException, PlayException {
    String appPage = getHTML(PlayUrls.getAppUri(id));
    return Application.fromHTML(id, appPage);
  }

  public List<SimilarApplication> getSimilarApplications(String appId) throws IOException, PlayException {
    List<SimilarApplication> results = new ArrayList<>();
    String similarPage = getHTML(PlayUrls.getSimilarAppUri(appId));
    Matcher m = categoryTopSellingFreePattern.matcher(similarPage);
    while (m.find()) {
      results.add(new SimilarApplication(appId, m.group(1)));
    }
    return results;
  }

  private String getHTML(URI uri) throws IOException, PlayException {
    String line;
    int statusCode;
    StringBuilder result = new StringBuilder();

    HttpGet request = new HttpGet(uri);

    try (CloseableHttpResponse response = client.execute(request)) {
      statusCode = response.getStatusLine().getStatusCode();
      switch (statusCode) {
        case 200:
          BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          while ((line = rd.readLine()) != null) {
            result.append(line);
          }
          rd.close();
          break;
        case 404:
          throw new ContentNotFound("Content not found for URL: " + uri.toString());
        case 403:
          throw new ContentForbidden("Content Forbidden. Maybe your Country is disallowed for URL: " + uri.toString());
        default:
          throw new PlayException(statusCode, "Unexpected Statuscode: " + statusCode + "for URL: " + uri.toString());
      }

    }
    return result.toString();
  }
}
