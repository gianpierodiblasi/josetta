package giada.josetta.transpiler;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import giada.josetta.es6.ES6VariableDeclarator;
import giada.josetta.util.JosettaException;
import giada.josetta.util.JosettaStringBuilder;

/**
 * The transpiler of a variable declarator
 *
 * @author gianpiero.di.blasi
 */
public class JosettaVariableDeclarator {

  /**
   * Transpiles a Java variable declarator into a ES6 variable declarator
   *
   * @param javaDeclarator The Java variable declarator
   * @param es6Declarator The ES6 variable declarator
   * @throws JosettaException thrown if an error occurs
   */
  public void transpile(VariableDeclarator javaDeclarator, ES6VariableDeclarator es6Declarator) throws JosettaException {
    try {
      javaDeclarator.getInitializer().ifPresentOrElse(
              expression -> {
                try {
                  new JosettaExpression().transpile(expression, es6Declarator.getInitializer());
                } catch (JosettaException ex) {
                  throw new RuntimeException(ex.getMessage());
                }
              },
              () -> {
                try {
                  es6Declarator.getInitializer().setExpression(this.getDefaultInitializer(javaDeclarator));
                } catch (JosettaException ex) {
                  throw new RuntimeException(ex.getMessage());
                }
              }
      );
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }
  }

  private String getDefaultInitializer(VariableDeclarator variableDeclarator) throws JosettaException {
    JosettaStringBuilder builder = new JosettaStringBuilder();

    Type type = variableDeclarator.getType();
    type.ifClassOrInterfaceType(ty -> builder.append("null"));
    type.ifPrimitiveType(ty -> {
      switch (ty.getType()) {
        case BOOLEAN:
          builder.append(false);
          break;
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
          builder.append(0);
          break;
        case CHAR:
          builder.append("\"\"");
          break;
      }
    });

    if (builder.isEmpty()) {
      throw new JosettaException("Variable declaration type not yet handled => [" + type.getClass().getSimpleName() + "] " + type);
    } else {
      return builder.toString();
    }
  }
}
