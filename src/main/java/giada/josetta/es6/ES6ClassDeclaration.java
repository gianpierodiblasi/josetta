package giada.josetta.es6;

import giada.josetta.util.JosettaException;
import giada.josetta.util.JosettaStringBuilder;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The declaration of a ES6 class
 *
 * @author gianpiero.di.blasi
 */
public class ES6ClassDeclaration {

  private final String className;
  private String extendedClassName;
  private final HashMap<String, ES6VariableDeclarator> variableDeclarator = new LinkedHashMap<>();
  private final HashMap<String, ES6VariableDeclarator> methodDeclarations = new LinkedHashMap<>();

  /**
   * Creates a class declaration
   *
   * @param className The name of the class
   */
  public ES6ClassDeclaration(String className) {
    this.className = className;
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
   * @return The new variable declarator
   * @throws JosettaException thrown if the variable name is already used (also as
   * a method name)
   */
  public ES6VariableDeclarator addVariable(String variableName) throws JosettaException {
    if (this.variableDeclarator.containsKey(variableName)) {
      throw new JosettaException("Variable name " + variableName + " is already used");
    } else if (this.methodDeclarations.containsKey(variableName)) {
      throw new JosettaException("Variable name " + variableName + " is already used as a method name");
    } else {
      return this.variableDeclarator.put(variableName, new ES6VariableDeclarator(variableName));
    }
  }

  @Override
  public String toString() {
    JosettaStringBuilder builder = new JosettaStringBuilder().
            append("class ", className).
            appendIf(() -> extendedClassName != null, "extends ", extendedClassName).
            append(" {\n").
            append("}\n");

    return builder.toString();
  }
}
