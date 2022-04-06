package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The declaration of a ES6 callable (method or constructor)
 *
 * @author gianpiero.di.blasi
 */
public class ES6CallableDeclaration {

  private final List<String> parameters = new ArrayList<>();
  private String body;

  /**
   * Adds a parameter
   *
   * @param parameter The parameter
   */
  public void addParameter(String parameter) {
    this.parameters.add(parameter);
  }

  /**
   * Sets the body
   *
   * @param body The body
   */
  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public String toString() {
    return new JosettaStringBuilder().
            append("(", this.parameters.stream().collect(Collectors.joining(", "))).append(") ").
            append(this.body, "\n").
            toString();
  }
}
