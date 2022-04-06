package giada.josetta.transpiler;

import com.github.javaparser.ast.expr.Expression;
import giada.josetta.es6.ES6Expression;
import giada.josetta.es6.ES6VariableDeclarator;
import giada.josetta.util.JosettaException;
import giada.josetta.util.JosettaStringBuilder;
import java.util.stream.Collectors;

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
    String expression = this.transpile(javaExpression);
    es6Expression.setExpression(expression);
  }

  private String transpile(Expression javaExpression) throws JosettaException {
    JosettaStringBuilder builder = new JosettaStringBuilder();

    try {
      javaExpression.ifBooleanLiteralExpr(exp -> builder.append(exp));
      javaExpression.ifCharLiteralExpr(exp -> builder.append(exp));
      javaExpression.ifDoubleLiteralExpr(exp -> builder.append(exp.asDouble()));
      javaExpression.ifIntegerLiteralExpr(exp -> builder.append(exp.asNumber()));
      javaExpression.ifLongLiteralExpr(exp -> builder.append(exp.asNumber()));
      javaExpression.ifNullLiteralExpr(exp -> builder.append("null"));
      javaExpression.ifStringLiteralExpr(exp -> builder.append(exp));
      javaExpression.ifObjectCreationExpr(exp -> builder.append(exp));
      javaExpression.ifFieldAccessExpr(exp -> builder.append(exp));
      javaExpression.ifNameExpr(exp -> builder.append(exp));

      javaExpression.ifMethodCallExpr(exp -> {
        builder.append(exp.getScope().get(), ".", exp.getName(), "(").
                append(exp.getArguments().stream().map(argument -> {
                  try {
                    return this.transpile(argument);
                  } catch (JosettaException ex) {
                    throw new RuntimeException(ex.getMessage());
                  }
                }).collect(Collectors.joining(", "))).
                append(")");
      });

      javaExpression.ifEnclosedExpr(exp -> {
        try {
          builder.append("(", this.transpile(exp.getInner()), ")");
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });

      javaExpression.ifCastExpr(exp -> {
        try {
          builder.append(this.transpile(exp.getExpression()));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });

      javaExpression.ifBinaryExpr(exp -> {
        try {
          builder.append(this.transpile(exp.getLeft()), " ", exp.getOperator().asString(), " ", this.transpile(exp.getRight()));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });

      javaExpression.ifVariableDeclarationExpr(exp -> {
        builder.append(exp.getVariables().stream().map(variable -> {
          try {
            ES6VariableDeclarator es6VariableDeclarator = new ES6VariableDeclarator(variable.getNameAsString(), ES6VariableDeclarator.Type.VARIABLE);
            new JosettaVariableDeclarator().transpile(variable, es6VariableDeclarator);
            return es6VariableDeclarator.toString();
          } catch (JosettaException ex) {
            throw new RuntimeException(ex.getMessage());
          }
        }).collect(Collectors.joining("; ")));
      });

      javaExpression.ifConditionalExpr(exp -> {
        try {
          builder.append(this.transpile(exp.getCondition()), " ? ", this.transpile(exp.getThenExpr()), " : " + this.transpile(exp.getElseExpr()));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });

      javaExpression.ifAssignExpr(exp -> {
        try {
          builder.append(this.transpile(exp.getTarget()), " ", exp.getOperator().asString(), this.transpile(exp.getValue()));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });

      javaExpression.ifUnaryExpr(exp -> {
        try {
          if (exp.isPrefix()) {
            builder.append(exp.getOperator().asString(), this.transpile(exp.getExpression()));
          }
          if (exp.isPostfix()) {
            builder.append(this.transpile(exp.getExpression()), " ", exp.getOperator().asString());
          }
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }

    if (builder.isEmpty()) {
      throw new JosettaException("Expression type not yet handled => [" + javaExpression.getClass().getSimpleName() + "] " + javaExpression);
    } else {
      return builder.toString();
    }
  }
}
