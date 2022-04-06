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
  }
}

//  private void transpileClassParameter(Writer writer, FieldDeclarator variableDeclarator) {
//    variableDeclarator.getVariables().forEach(variable -> {
//      append(writer, "  ", variable.getNameAsString(), " = ");
//      variable.getInitializer().ifPresentOrElse(
//              expression -> new ExpressionTranspiler().transpile(writer, className, expression),
//              () -> new ClassParameterInitializationTranspiler().transpile(writer, className, variable)
//      );
//      append(writer, ";\n");
//    });
//  }
