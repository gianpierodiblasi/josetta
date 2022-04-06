package giada.josetta.transpiler;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import giada.josetta.es6.ES6CallableDeclaration;
import giada.josetta.es6.ES6Expression;
import giada.josetta.util.JosettaException;
import giada.josetta.util.JosettaStringBuilder;
import java.util.stream.Collectors;

/**
 * The transpiler of a callable declaration (method or constructor)
 *
 * @author gianpiero.di.blasi
 * @param <T> A CallableDeclaration
 * @param <S> A ES6CallableDeclaration
 */
public class JosettaCallableDeclaration<T extends CallableDeclaration<T>, S extends ES6CallableDeclaration> {

  /**
   * Transpiles a Java method declaration into a ES6 method declaration
   *
   * @param javaDeclaration The Java method declaration
   * @param es6Declaration The ES6 method declaration
   * @throws JosettaException thrown if an error occurs
   */
  public void transpile(T javaDeclaration, S es6Declaration) throws JosettaException {
    javaDeclaration.getParameters().forEach(parameter -> es6Declaration.addParameter(parameter.getNameAsString()));

    try {
      javaDeclaration.ifMethodDeclaration(declaration -> declaration.getBody().ifPresent(body -> {
        try {
          es6Declaration.setBody(this.transpile(body, ""));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      }));
      javaDeclaration.ifConstructorDeclaration(declaration -> {
        try {
          es6Declaration.setBody(this.transpile(declaration.getBody(), "  "));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }
  }

  private String transpile(Statement statement, String indent) throws JosettaException {
    JosettaStringBuilder builder = new JosettaStringBuilder();

    try {
      statement.ifBlockStmt(stmt -> {
        builder.append("{\n").
                append(stmt.getStatements().stream().map(st -> {
                  try {
                    return this.transpile(st, indent + "  ");
                  } catch (JosettaException ex) {
                    throw new RuntimeException(ex.getMessage());
                  }
                }).collect(Collectors.joining("\n"))).
                append("}");
      });

      statement.ifReturnStmt(stmt -> {
        stmt.getExpression().ifPresentOrElse(expression -> {
          try {
            ES6Expression es6Expression = new ES6Expression();
            new JosettaExpression().transpile(expression, es6Expression);
            builder.append(indent, es6Expression.toString(), ";");
          } catch (JosettaException ex) {
            throw new RuntimeException(ex.getMessage());
          }
        }, () -> builder.append(indent, "return;"));
      });

      statement.ifExpressionStmt(stmt -> {
        try {
          ES6Expression es6Expression = new ES6Expression();
          new JosettaExpression().transpile(stmt.getExpression(), es6Expression);
          builder.append(indent, es6Expression.toString(), ";");
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }

    if (builder.isEmpty()) {
      throw new JosettaException("Block statement not yet handled => [" + statement.getClass().getSimpleName() + "] " + statement);
    } else {
      return builder.toString();
    }
  }
}
