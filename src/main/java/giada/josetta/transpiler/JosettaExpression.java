package giada.josetta.transpiler;

import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import giada.josetta.es6.ES6Expression;
import giada.josetta.util.JosettaException;

/**
 * The transpiler of an expression
 *
 * @author gianpiero.di.blasi
 */
public class JosettaExpression {

  /**
   * Transpiles a Java expression into a ES6 expression
   *
   * @param javaExpression The Java expression
   * @param es6Expression The ES6 expression
   * @throws JosettaException thrown if the expression type is not yet handled
   */
  public void transpile(Expression javaExpression, ES6Expression es6Expression) throws JosettaException {

    try {
      javaExpression.ifBooleanLiteralExpr(exp -> this.setExpression(javaExpression, es6Expression));
      javaExpression.ifCharLiteralExpr(exp -> this.setExpression(javaExpression, es6Expression));
      javaExpression.ifDoubleLiteralExpr(exp -> this.setExpression(javaExpression.asDoubleLiteralExpr().asDouble(), es6Expression));
      javaExpression.ifIntegerLiteralExpr(exp -> this.setExpression(javaExpression.asIntegerLiteralExpr().asNumber(), es6Expression));
      javaExpression.ifLongLiteralExpr(exp -> this.setExpression(javaExpression.asLongLiteralExpr().asNumber(), es6Expression));
      javaExpression.ifNullLiteralExpr(exp -> this.setExpression("null", es6Expression));
      javaExpression.ifStringLiteralExpr(exp -> this.setExpression(javaExpression, es6Expression));
      javaExpression.ifObjectCreationExpr(exp -> this.setExpression(javaExpression, es6Expression));

      javaExpression.ifBinaryExpr(exp -> {
        Expression left = exp.getLeft();
        Operator operator = exp.getOperator();
        Expression right = exp.getRight();
      });

      javaExpression.ifCastExpr(exp -> {
        try {
          this.transpile(javaExpression.asCastExpr().getExpression(), es6Expression);
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }

    if (!es6Expression.hasExpression()) {
      throw new JosettaException("Expression type not yet handled => [" + javaExpression.getClass().getSimpleName() + "]" + javaExpression);
    }
  }

  private void setExpression(Object object, ES6Expression es6Expression) {
    if (!es6Expression.hasExpression()) {
      es6Expression.setExpression(object.toString());
    }
  }
}
