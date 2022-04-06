package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The declaration of a ES6 constructor
 *
 * @author gianpiero.di.blasi
 */
public class ES6ConstructorDeclaration {

  private final List<String> parameters = new ArrayList<>();

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
            append("  constructor(").append(") {\n").
            append(this.parameters.stream().collect(Collectors.joining(", "))).
            append("  }\n");

    return builder.toString();
  }
}
