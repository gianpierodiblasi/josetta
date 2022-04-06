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
  private final ES6Expression es6Expression = new ES6Expression();

  /**
   * The types of variables
   */
  public enum Type {
    /**
     * An instance variable
     */
    INSTANCE,
    /**
     * A static variable
     */
    STATIC,
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

  /**
   * Returns the variable initializer
   *
   * @return The variable initializer
   */
  public ES6Expression getInitializer() {
    return this.es6Expression;
  }

  @Override
  public String toString() {
    JosettaStringBuilder builder = new JosettaStringBuilder().
            appendIf(() -> this.type == Type.STATIC, "static ").
            appendIf(() -> this.type == Type.VARIABLE, "let ").
            append(this.variableName).
            appendIf(() -> this.type != Type.PARAMETER, " = ").
            append(this.es6Expression.toString()).
            appendIf(() -> this.type != Type.PARAMETER, ";");

    return builder.toString();
  }
}
