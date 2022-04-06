package giada.josetta.es6;

/**
 * A ES6 expression
 *
 * @author gianpiero.di.blasi
 */
public class ES6Expression {

  private String expression;

  /**
   * Sets the expression
   *
   * @param expression The expression
   */
  public void setExpression(String expression) {
    this.expression = expression;
  }

  @Override
  public String toString() {
    return this.expression;
  }
}
