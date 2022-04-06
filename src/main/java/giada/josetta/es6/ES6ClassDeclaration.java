package giada.josetta.es6;

import giada.josetta.util.JosettaException;
import giada.josetta.util.JosettaStringBuilder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * The declaration of a ES6 class
 *
 * @author gianpiero.di.blasi
 */
public class ES6ClassDeclaration {

  private final String className;
  private String extendedClassName;
  private final HashMap<String, ES6VariableDeclarator> variableDeclarators = new LinkedHashMap<>();
  private final ES6ConstructorDeclaration constructorDeclaration = new ES6ConstructorDeclaration();
  private final HashMap<String, ES6MethodDeclaration> methodDeclarations = new LinkedHashMap<>();

  /**
   * Creates a class declaration
   *
   * @param className The name of the class
   */
  public ES6ClassDeclaration(String className) {
    this.className = className;
  }

  /**
   * Returns the name of the class
   *
   * @return The name of the class
   */
  public String getClassName() {
    return this.className;
  }

  /**
   * Sets the extended class
   *
   * @param extendedClassName The name of the extended class
   * @return This class declaration
   * @throws JosettaException thrown if this class already extends another class
   */
  public ES6ClassDeclaration setExtends(String extendedClassName) throws JosettaException {
    if (this.extendedClassName != null) {
      throw new JosettaException("Class name " + className + " already extends a class, current extended class name = " + this.extendedClassName + ", new extended class name = " + extendedClassName);
    } else {
      this.extendedClassName = extendedClassName;
    }
    return this;
  }

  /**
   * Adds a new variable
   *
   * @param variableName The variable name
   * @param type The variable type
   * @return The new variable declarator
   * @throws JosettaException thrown if the variable name is already used (also
   * as a method name)
   */
  public ES6VariableDeclarator addVariable(String variableName, ES6VariableDeclarator.Type type) throws JosettaException {
    if (this.variableDeclarators.containsKey(variableName)) {
      throw new JosettaException("Variable name " + variableName + " is already used");
    } else if (this.methodDeclarations.containsKey(variableName)) {
      throw new JosettaException("Variable name " + variableName + " is already used as a method name");
    } else {
      ES6VariableDeclarator es6VariableDeclarator = new ES6VariableDeclarator(variableName, type);
      this.variableDeclarators.put(variableName, es6VariableDeclarator);
      return es6VariableDeclarator;
    }
  }

  /**
   * Returns the constructor declaration
   *
   * @return The constructor declaration
   */
  public ES6ConstructorDeclaration getConstructorDeclaration() {
    return this.constructorDeclaration;
  }

  /**
   * Adds a new method
   *
   * @param methodName The method name
   * @param type The method type
   * @return The new method declaration
   * @throws JosettaException thrown if the variable name is already used (also
   * as a method name)
   */
  public ES6MethodDeclaration addMethod(String methodName, ES6MethodDeclaration.Type type) throws JosettaException {
    if (this.variableDeclarators.containsKey(methodName)) {
      throw new JosettaException("Method name " + methodName + " is already used as a variable name");
    } else if (this.methodDeclarations.containsKey(methodName)) {
      throw new JosettaException("Method name " + methodName + " is already used");
    } else {
      ES6MethodDeclaration es6MethodDeclaration = new ES6MethodDeclaration(methodName, type);
      this.methodDeclarations.put(methodName, es6MethodDeclaration);
      return es6MethodDeclaration;
    }
  }

  @Override
  public String toString() {
    return new JosettaStringBuilder().
            append("class ", this.className).appendIf(() -> this.extendedClassName != null, "extends ", this.extendedClassName).append(" {\n").
            append(this.variableDeclarators.values().stream().map(ES6VariableDeclarator::toString).map(str -> "  " + str).collect(Collectors.joining("\n")), "\n\n").
            append(this.constructorDeclaration.toString(), "\n").
            append(this.methodDeclarations.values().stream().map(ES6MethodDeclaration::toString).collect(Collectors.joining("\n"))).
            append("}\n").
            toString();
  }
}
