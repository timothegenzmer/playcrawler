package org.ogham.crawler.tasks;

import org.ogham.play.PlayClient;

/**
 * @author Timothe Genzmer 546765
 */
public abstract class CrawlerTask implements Runnable {

  protected PlayClient playClient;

  public CrawlerTask(PlayClient playClient) {
    this.playClient = playClient;
  }
}
