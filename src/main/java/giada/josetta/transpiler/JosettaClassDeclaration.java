package giada.josetta.transpiler;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import giada.josetta.es6.ES6ClassDeclaration;
import giada.josetta.es6.ES6MethodDeclaration;
import giada.josetta.es6.ES6VariableDeclarator;
import giada.josetta.util.JosettaException;
import java.util.List;

/**
 * The transpiler of a class declaration
 *
 * @author gianpiero.di.blasi
 */
public class JosettaClassDeclaration {

  /**
   * Transpiles a Java class declaration into a ES6 class declaration
   *
   * @param javaDeclaration The Java class declaration
   * @param es6Declaration The ES6 class declaration
   * @throws JosettaException thrown if an error occurs
   */
  public void transpile(ClassOrInterfaceDeclaration javaDeclaration, ES6ClassDeclaration es6Declaration) throws JosettaException {
    try {
      javaDeclaration.getExtendedTypes().forEach(extendedType -> {
        try {
          es6Declaration.setExtends(extendedType.getNameAsString());
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });

      javaDeclaration.getFields().forEach(javaFieldDeclaration -> javaFieldDeclaration.getVariables().forEach(variableDeclarator -> {
        try {
          new JosettaVariableDeclarator().transpile(variableDeclarator, es6Declaration.addVariable(variableDeclarator.getNameAsString(), javaFieldDeclaration.isStatic() ? ES6VariableDeclarator.Type.STATIC : ES6VariableDeclarator.Type.INSTANCE));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      }));

      List<ConstructorDeclaration> constructors = javaDeclaration.getConstructors();
      if (constructors.size() > 1) {
        throw new RuntimeException("Class " + es6Declaration.getClassName() + " has more than one constructor");
      } else {
        constructors.forEach(constructorDeclaration -> {
          try {
            new JosettaConstructorDeclaration().transpile(constructorDeclaration, es6Declaration.getConstructorDeclaration());
          } catch (JosettaException ex) {
            throw new RuntimeException(ex.getMessage());
          }
        });
      }

      javaDeclaration.getMethods().forEach(methodDeclaration -> {
        try {
          new JosettaMethodDeclaration().transpile(methodDeclaration, es6Declaration.addMethod(methodDeclaration.getNameAsString(), methodDeclaration.isStatic() ? ES6MethodDeclaration.Type.STATIC : ES6MethodDeclaration.Type.INSTANCE));
        } catch (JosettaException ex) {
          throw new RuntimeException(ex.getMessage());
        }
      });
    } catch (RuntimeException ex) {
      throw new JosettaException(ex.getMessage());
    }
  }
}
