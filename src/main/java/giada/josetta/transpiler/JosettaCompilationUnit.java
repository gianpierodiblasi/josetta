package giada.josetta.transpiler;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import giada.josetta.es6.ES6CompilationUnit;
import giada.josetta.util.JosettaException;

/**
 * The transpiler of a compilation unit
 *
 * @author gianpiero.di.blasi
 */
public class JosettaCompilationUnit {

  /**
   * Transpiles a Java compilation unit into a ES6 compilation unit
   *
   * @param javaCompilationUnit The Java compilation unit
   * @param es6CompilationUnit The ES6 compilation unit
   * @throws JosettaException throws if an error occurs
   */
  public void transpile(CompilationUnit javaCompilationUnit, ES6CompilationUnit es6CompilationUnit) throws JosettaException {
    try {
      javaCompilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(javaDeclaration -> {
        String className = javaDeclaration.getNameAsString();

        if (javaDeclaration.isInterface()) {
          throw new RuntimeException(className + " is an interface declaration (NOT MANAGED)");
        } else {
          try {
            new JosettaClassDeclaration().transpile(javaDeclaration, es6CompilationUnit.addClass(className));
          } catch (JosettaException ex) {
            throw new RuntimeException(ex.getMessage());
          }
        }
      });
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }
  }
}