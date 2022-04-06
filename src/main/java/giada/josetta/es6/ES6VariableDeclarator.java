package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;

/**
 * The declarator of a ES6 variable
 *
 * @author gianpiero.di.blasi
 */
public class ES6VariableDeclarator {

  private final String variableName;
  private final Type type;

  /**
   * The types of variables
   */
  public enum Type {
    /**
     * An instance variable
     */
    INSTANCE,
    /**
     * A static instance variable
     */
    STATIC_INSTANCE,
    /**
     * A method/constructor parameter
     */
    PARAMETER,
    /**
     * A variable inside a method/constructor
     */
    VARIABLE
  }

  /**
   * Creates a variable declarator
   *
   * @param variableName The name of the variable
   * @param type The variable type
   */
  public ES6VariableDeclarator(String variableName, Type type) {
    this.variableName = variableName;
    this.type = type;
  }

  @Override
  public String toString() {
    JosettaStringBuilder builder = new JosettaStringBuilder().
            appendIf(() -> type == Type.STATIC_INSTANCE, "static ").
            appendIf(() -> type == Type.VARIABLE, "let ").
            append(variableName, ";");

    return builder.toString();
  }
}
