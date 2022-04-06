package giada.josetta.transpiler;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import giada.josetta.es6.ES6ClassDeclaration;
import giada.josetta.util.JosettaException;

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
   * @throws JosettaException throws if an error occurs
   */
  public void transpile(ClassOrInterfaceDeclaration javaDeclaration, ES6ClassDeclaration es6Declaration) throws JosettaException {
    javaDeclaration.getExtendedTypes().forEach(extendedType -> es6Declaration.setExtends(extendedType.getNameAsString()));

//    classDeclaration.getFields().forEach(declaration -> transpileClassParameter(writer, declaration));
//    append(writer, "\n");
//
//    List<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
//    if (constructors.size() > 1) {
//      throw new RuntimeException("Class " + className + " has more than one constructor");
//    } else {
//      constructors.forEach(constructorDeclaration -> transpileConstructor(writer, constructorDeclaration));
//    }
//    append(writer, "\n");
//
//    classDeclaration.getMethods().forEach(declaration -> transpileMethod(writer, declaration));
//
//    append(writer, "}");

  }
}
