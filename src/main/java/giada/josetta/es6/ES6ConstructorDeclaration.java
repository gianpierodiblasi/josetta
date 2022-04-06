package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;
import java.util.stream.Collectors;

/**
 * The declaration of a ES6 constructor
 *
 * @author gianpiero.di.blasi
 */
public class ES6ConstructorDeclaration extends ES6CallableDeclaration {

  @Override
  public String toString() {
    JosettaStringBuilder builder = new JosettaStringBuilder().
            append("  constructor(").append(") {\n").
            append(this.parameters.stream().collect(Collectors.joining(", "))).
            append("  }\n");

    return builder.toString();
  }
}
