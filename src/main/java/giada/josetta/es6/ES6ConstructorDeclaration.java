package giada.josetta.es6;

import giada.josetta.util.JosettaStringBuilder;

/**
 * The declaration of a ES6 constructor
 *
 * @author gianpiero.di.blasi
 */
public class ES6ConstructorDeclaration extends ES6CallableDeclaration {

  @Override
  public String toString() {
    return new JosettaStringBuilder().append("  constructor", super.toString()).toString();
  }
}
