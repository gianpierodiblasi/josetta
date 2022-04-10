package giada.josetta;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The josetta checker
 *
 * @author gianpiero.di.blasi
 */
public class JosettaChecker {

  /**
   * Checks a compilation unit
   *
   * @param compilationUnit The compilation unit
   * @param ag The list of array setter methods
   * @param as The list of array getter methods
   * @param nt The list of no transpilation symbols
   */
  public static void checkCompilationUnit(CompilationUnit compilationUnit, String[] ag, String[] as, String[] nt) {
    compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterface -> JosettaChecker.checkClassOrInterface(classOrInterface, nt));
    compilationUnit.findAll(EnumDeclaration.class).forEach(enumDeclaration -> JosettaChecker.checkEnumDeclaration(enumDeclaration));
    compilationUnit.findAll(ConstructorDeclaration.class).forEach(constructor -> JosettaChecker.checkConstructor(constructor));
    compilationUnit.findAll(FieldDeclaration.class).forEach(field -> JosettaChecker.checkField(field));
    compilationUnit.findAll(MethodDeclaration.class).forEach(method -> JosettaChecker.checkMethod(method));
    compilationUnit.findAll(Parameter.class).forEach(parameter -> JosettaChecker.checkParameter(parameter));
    compilationUnit.findAll(VariableDeclarator.class).forEach(variable -> JosettaChecker.checkVariableDeclarator(variable));
    compilationUnit.findAll(VariableDeclarationExpr.class).forEach(variable -> JosettaChecker.checkVariableDeclarationExpr(variable));
  }

  private static void checkClassOrInterface(ClassOrInterfaceDeclaration classOrInterface, String[] nt) {
    if (classOrInterface.isInnerClass() && !startsWith(classOrInterface.getNameAsString(), nt)) {
      throw new RuntimeException("Class/Interface " + classOrInterface.getNameAsString() + " is an inner class. NOT COVERED.");
    }

    if (classOrInterface.getConstructors().size() > 1) {
      throw new RuntimeException("Class/Interface " + classOrInterface.getNameAsString() + " has more than one constructor");
    }

    NodeList<ClassOrInterfaceType> types = classOrInterface.getExtendedTypes();
    types.addAll(classOrInterface.getImplementedTypes());

    if (types.size() > 1) {
      throw new RuntimeException("Class/Interface " + classOrInterface.getNameAsString() + " extends/implements more than one class/interface");
    }

    classOrInterface.setAbstract(false);
    classOrInterface.setAnnotations(new NodeList<>());
    classOrInterface.setExtendedTypes(types);
    classOrInterface.setFinal(false);
    classOrInterface.setImplementedTypes(new NodeList<>());
    classOrInterface.setInterface(false);
    classOrInterface.setModifiers(new NodeList<>());
    classOrInterface.setPrivate(false);
    classOrInterface.setProtected(false);
    classOrInterface.setPublic(false);
    classOrInterface.setStatic(false);
    classOrInterface.setTypeParameters(new NodeList<>());

    Map<String, Long> map = JosettaChecker.checkMethods(classOrInterface, nt);
    JosettaChecker.checkFields(classOrInterface, map);
  }

  private static void checkEnumDeclaration(EnumDeclaration enumDeclaration) {
    throw new RuntimeException(enumDeclaration.getNameAsString() + " is an enum. NOT COVERED.");
  }

  private static Map<String, Long> checkMethods(ClassOrInterfaceDeclaration classOrInterface, String[] nt) {
    Map<String, Long> map = classOrInterface.getMethods().stream().filter(method -> !startsWith(method.getNameAsString(), nt)).collect(Collectors.groupingBy(MethodDeclaration::getNameAsString, Collectors.counting()));

    if (map.values().stream().anyMatch(value -> value > 1)) {
      throw new RuntimeException("Class/Interface " + classOrInterface.getNameAsString() + " has some overloaded methods");
    }

    return map;
  }

  private static void checkFields(ClassOrInterfaceDeclaration classOrInterface, Map<String, Long> map) {
    boolean fieldAndMethodWithSameName = classOrInterface.getFields().stream().flatMap(field -> field.getVariables().stream()).anyMatch(variable -> map.containsKey(variable.getNameAsString()));
    if (fieldAndMethodWithSameName) {
      throw new RuntimeException("Class/Interface " + classOrInterface.getNameAsString() + " has some fields and methods with the same name");
    }
  }

  private static void checkConstructor(ConstructorDeclaration constructor) {
    constructor.setAbstract(false);
    constructor.setAnnotations(new NodeList<>());
    constructor.setFinal(false);
    constructor.setModifiers(new NodeList<>());
    constructor.setName("constructor");
    constructor.setPrivate(false);
    constructor.setProtected(false);
    constructor.setPublic(false);
    constructor.setStatic(false);
    constructor.setThrownExceptions(new NodeList<>());
  }

  private static void checkField(FieldDeclaration field) {
    if (field.getVariables().size() > 1) {
      throw new RuntimeException("Field declaration " + field + " has more than one variable in a single row");
    }

    field.getVariables().forEach(variable -> JosettaChecker.checkVariableDeclarator(variable));

    field.setAllTypes(new UnknownType());
    field.setFinal(false);
    field.setPrivate(false);
    field.setProtected(false);
    field.setPublic(false);
    field.setTransient(false);
    field.setVolatile(false);
  }

  private static void checkMethod(MethodDeclaration method) {
    method.setAbstract(false);
    method.setAnnotations(new NodeList<>());
    method.setDefault(false);
    method.setFinal(false);
    method.setPublic(false);
    method.setPrivate(false);
    method.setProtected(false);
    method.setNative(false);
    method.setSynchronized(false);
    method.setThrownExceptions(new NodeList<>());
    method.setType(new UnknownType());

    if (method.getBody().isEmpty()) {
      method.setBody(new BlockStmt());
    }
  }

  private static void checkParameter(Parameter parameter) {
    parameter.setAnnotations(new NodeList<>());
    parameter.setFinal(false);
    parameter.setModifiers(new NodeList<>());
    parameter.setType(new UnknownType());
  }

  private static void checkVariableDeclarator(VariableDeclarator variable) {
    if (variable.getInitializer().isEmpty()) {
      Type type = variable.getType();

      type.ifClassOrInterfaceType(ty -> {
        variable.setInitializer(new NullLiteralExpr());
      });

      type.ifPrimitiveType(ty -> {
        switch (ty.getType()) {
          case BOOLEAN:
            variable.setInitializer(new BooleanLiteralExpr(false));
            break;
          case BYTE:
          case SHORT:
          case INT:
          case LONG:
            variable.setInitializer(new IntegerLiteralExpr("0"));
            break;
          case FLOAT:
          case DOUBLE:
            variable.setInitializer(new DoubleLiteralExpr(0));
            break;
          case CHAR:
            variable.setInitializer(new StringLiteralExpr(""));
            break;
        }
      });

      if (variable.getInitializer().isEmpty()) {
        throw new RuntimeException("Variable declaration type not yet handled => [" + type.getClass().getSimpleName() + "] " + type);
      }
    }
  }

  private static void checkVariableDeclarationExpr(VariableDeclarationExpr variable) {
    variable.setAllTypes(new UnknownType());
    variable.setAnnotations(new NodeList<>());
    variable.setFinal(false);
    variable.setModifiers(new NodeList<>());
    variable.setAllTypes(new ClassOrInterfaceType(null, "let"));
  }

  private static boolean startsWith(String string, String strs[]) {
    for (String str : strs) {
      if (string.startsWith(str)) {
        return true;
      }
    }
    return false;
  }

  private JosettaChecker() {
  }
}
