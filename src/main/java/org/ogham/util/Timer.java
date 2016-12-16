package org.ogham.util;

public class Timer {
  private long start;
  private String msg;

  public Timer(String msg) {
    start(msg);
  }

  public void start() {
    start = System.currentTimeMillis();
  }

  public void start(String msg) {
    start();
    this.msg = msg;
  }

  public void stop() {
    long duration = System.currentTimeMillis() - start;
    System.out.println(msg + " " + duration + "ms");
  }
}
