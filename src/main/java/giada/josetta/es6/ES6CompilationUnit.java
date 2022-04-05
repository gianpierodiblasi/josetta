package giada.josetta.es6;

import giada.josetta.util.JosettaException;
import giada.josetta.util.JosettaStringBuilder;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The compilation unit of a ES6 code
 *
 * @author gianpiero.di.blasi
 */
public class ES6CompilationUnit {

  private final HashMap<String, ES6ClassDeclaration> classDeclarations = new LinkedHashMap<>();

  /**
   * Adds a new class
   *
   * @param className The class name
   * @return The new class declaration
   * @throws JosettaException throws if the class name is already used
   */
  public ES6ClassDeclaration addClass(String className) throws JosettaException {
    if (classDeclarations.containsKey(className)) {
      throw new JosettaException("Class name " + className + " is already used");
    } else {
      return classDeclarations.put(className, new ES6ClassDeclaration(className));
    }
  }

  @Override
  public String toString() {
    JosettaStringBuilder builder = new JosettaStringBuilder();
    classDeclarations.forEach((key, value) -> builder.append(value.toString(), "\n"));
    return builder.toString();
  }
}
