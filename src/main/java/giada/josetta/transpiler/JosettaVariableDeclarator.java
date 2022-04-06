package giada.josetta.transpiler;

import com.github.javaparser.ast.body.VariableDeclarator;
import giada.josetta.es6.ES6VariableDeclarator;
import giada.josetta.util.JosettaException;

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
              () -> {/*new ClassParameterInitializationTranspiler().transpile(writer, className, variable)*/
              }
      );
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }
  }
}
