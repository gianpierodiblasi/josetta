package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The declaration of a ES6 method
 *
 * @author gianpiero.di.blasi
 */
public class ES6MethodDeclaration {

  private final String methodName;
  private final Type type;
  private final List<String> parameters = new ArrayList<>();

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

  /**
   * Adds a parameter
   *
   * @param parameter The parameter
   */
  public void addParameter(String parameter) {
    this.parameters.add(parameter);
  }

  @Override
  public String toString() {
    JosettaStringBuilder builder = new JosettaStringBuilder().
            append("  ").appendIf(() -> this.type == Type.STATIC, "static ").
            append(this.methodName, "(").append(this.parameters.stream().collect(Collectors.joining(", "))).append(") {\n").
            append("  }\n");

    return builder.toString();
  }
}
