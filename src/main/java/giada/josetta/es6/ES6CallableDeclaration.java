package giada.josetta.es6;

import java.util.ArrayList;
import java.util.List;

/**
 * The declaration of a ES6 callable (method or constructor)
 *
 * @author gianpiero.di.blasi
 */
public class ES6CallableDeclaration {

  protected final List<String> parameters = new ArrayList<>();

  /**
   * Adds a parameter
   *
   * @param parameter The parameter
   */
  public void addParameter(String parameter) {
    this.parameters.add(parameter);
  }
}
