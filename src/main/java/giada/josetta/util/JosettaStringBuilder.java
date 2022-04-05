package giada.josetta.util;

import java.util.function.Supplier;

/**
 * A wrapper of the standard StringBuilder
 *
 * @author gianpiero.di.blasi
 */
public class JosettaStringBuilder {

  private final StringBuilder builder = new StringBuilder();

  /**
   * Appends a list of objects
   *
   * @param obj The first object
   * @param others The other objects
   * @return This builder
   */
  public JosettaStringBuilder append(Object obj, Object... others) {
    builder.append(obj);
    for (Object other : others) {
      builder.append(other);
    }
    return this;
  }

  /**
   * Appends a list of objects if a condition is verified
   *
   * @param condition The condition
   * @param obj The first object
   * @param others The other objects
   * @return This builder
   */
  public JosettaStringBuilder appendIf(Supplier<Boolean> condition, Object obj, Object... others) {
    if (condition.get()) {
      this.append(obj, others);
    }
    return this;
  }

  @Override
  public String toString() {
    return builder.toString();
  }
}
