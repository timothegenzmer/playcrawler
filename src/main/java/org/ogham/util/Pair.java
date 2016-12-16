package org.ogham.util;

public class Pair<K, V> {
  public K k;
  public V v;

  public Pair(K k, V v) {
    this.k = k;
    this.v = v;
  }


  /**
   * <p>Test this <code>Pair</code> for equality with another
   * <code>Object</code>.</p>
   * <p>
   * <p>If the <code>Object</code> to be tested is not a
   * <code>Pair</code> or is <code>null</code>, then this method
   * returns <code>false</code>.</p>
   * <p>
   * <p>Two <code>Pair</code>s are considered equal if and only if
   * both the names and values are equal.</p>
   *
   * @param o the <code>Object</code> to test for
   *          equality with this <code>Pair</code>
   * @return <code>true</code> if the given <code>Object</code> is
   * equal to this <code>Pair</code> else <code>false</code>
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof Pair) {
      Pair pair = (Pair) o;
      if (k != null ? !k.equals(pair.k) : pair.k != null) return false;
      if (v != null ? !v.equals(pair.v) : pair.v != null) return false;
      return true;
    }
    return false;
  }
}
