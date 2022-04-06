package giada.josetta.es6;

import giada.josetta.util.JosettaException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

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
   * @throws JosettaException thrown if the class name is already used
   */
  public ES6ClassDeclaration addClass(String className) throws JosettaException {
    if (this.classDeclarations.containsKey(className)) {
      throw new JosettaException("Class name " + className + " is already used");
    } else {
      ES6ClassDeclaration eS6ClassDeclaration = new ES6ClassDeclaration(className);
      this.classDeclarations.put(className, eS6ClassDeclaration);
      return eS6ClassDeclaration;
    }
  }

  @Override
  public String toString() {
    return this.classDeclarations.values().stream().map(ES6ClassDeclaration::toString).collect(Collectors.joining("\n"));
  }
}
