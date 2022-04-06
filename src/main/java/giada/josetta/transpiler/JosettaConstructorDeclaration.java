package giada.josetta.transpiler;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import giada.josetta.es6.ES6ConstructorDeclaration;
import giada.josetta.util.JosettaException;

/**
 * The transpiler of a constructor declaration
 *
 * @author gianpiero.di.blasi
 */
public class JosettaConstructorDeclaration {

  /**
   * Transpiles a Java constructor declaration into a ES6 constructor
   * declaration
   *
   * @param javaDeclaration The Java constructor declaration
   * @param es6Declaration The ES6 constructor declaration
   * @throws JosettaException thrown if an error occurs
   */
  public void transpile(ConstructorDeclaration javaDeclaration, ES6ConstructorDeclaration es6Declaration) throws JosettaException {

    javaDeclaration.getParameters().forEach(parameter -> es6Declaration.addParameter(parameter.getNameAsString()));

//      //BODY CONSTRUCTOR
  }
}
