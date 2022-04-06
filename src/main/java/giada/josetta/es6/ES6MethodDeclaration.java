package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;

/**
 * The declaration of a ES6 method
 *
 * @author gianpiero.di.blasi
 */
public class ES6MethodDeclaration extends ES6CallableDeclaration {

  private final String methodName;
  private final Type type;

  /**
   * The types of methods
   */
  public enum Type {
    /**
     * An instance method
     */
    INSTANCE,
    /**
     * A static method
     */
    STATIC
  }

  /**
   * Creates a method declarator
   *
   * @param methodName The name of the method
   * @param type The method type
   */
  public ES6MethodDeclaration(String methodName, Type type) {
    this.methodName = methodName;
    this.type = type;
  }

  @Override
  public String toString() {
    return new JosettaStringBuilder().
            append("  ").appendIf(() -> this.type == Type.STATIC, "static ").append(this.methodName, super.toString()).toString();
  }
}
