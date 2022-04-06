package giada.josetta.transpiler;

import com.github.javaparser.ast.body.CallableDeclaration;
import giada.josetta.es6.ES6CallableDeclaration;
import giada.josetta.util.JosettaException;

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

//      //BODY CONSTRUCTOR
  }
}
