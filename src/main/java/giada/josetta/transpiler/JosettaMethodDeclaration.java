package giada.josetta.transpiler;

import com.github.javaparser.ast.body.MethodDeclaration;
import giada.josetta.es6.ES6MethodDeclaration;
import giada.josetta.util.JosettaException;

/**
 * The transpiler of a method declaration
 *
 * @author gianpiero.di.blasi
 */
public class JosettaMethodDeclaration {

  /**
   * Transpiles a Java method declaration into a ES6 method declaration
   *
   * @param javaDeclaration The Java method declaration
   * @param es6Declaration The ES6 method declaration
   * @throws JosettaException thrown if an error occurs
   */
  public void transpile(MethodDeclaration javaDeclaration, ES6MethodDeclaration es6Declaration) throws JosettaException {

    javaDeclaration.getParameters().forEach(parameter -> es6Declaration.addParameter(parameter.getNameAsString()));

//      //BODY CONSTRUCTOR
  }
}
