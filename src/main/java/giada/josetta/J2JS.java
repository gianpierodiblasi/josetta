package giada.josetta;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class J2JS {

  private static void transpile(File in) throws Exception {
    StringBuilder builder = new StringBuilder();

    CompilationUnit compilationUnit = StaticJavaParser.parse(in);
    compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(declaration -> transpileClass(builder, declaration));

    File out = new File(in.getParentFile(), in.getName().replace(".java", ".js"));
    try ( BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
      writer.write(builder.toString());
    }
  }

  private static void transpileClass(StringBuilder builder, ClassOrInterfaceDeclaration classDeclaration) {
    String name = classDeclaration.getNameAsString();

    builder.append("class ").append(name).append(" {\n");
    classDeclaration.getFields().forEach(declaration -> transpileParameter(builder, declaration));
    builder.append("\n");
    classDeclaration.getMethods().forEach(declaration -> transpileMethod(builder, declaration));
    builder.append("}");
  }

  private static void transpileParameter(StringBuilder builder, FieldDeclaration fieldDeclaration) {
    fieldDeclaration.getVariables().forEach(variable -> {
      builder.append("  ").append(variable.getNameAsString());
      variable.getInitializer().ifPresent(expression -> builder.append(" = ").append(expression.toString()));
      builder.append(";\n");
    });
  }

  private static void transpileMethod(StringBuilder builder, MethodDeclaration methodDeclaration) {
    String name = methodDeclaration.getNameAsString();

    builder.append("  ").append(name).append("(");
    int len = builder.length();
    methodDeclaration.getParameters().forEach(parameter -> builder.append(len != builder.length() ? ", " : "").append(parameter.getNameAsString()));
    builder.append(") {\n");

    methodDeclaration.getBody().ifPresent(body -> {
      if (body.isAssertStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isBlockStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isBreakStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isContinueStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isDoStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isEmptyStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isExplicitConstructorInvocationStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isExpressionStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isForEachStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isForStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isIfStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isLabeledStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isLocalClassDeclarationStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isLocalRecordDeclarationStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isReturnStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isSwitchStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isSynchronizedStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isThrowStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isTryStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isUnparsableStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isWhileStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else if (body.isYieldStmt()) {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      } else {
        throw new RuntimeException(body.toString() + "NOT MANAGED");
      }
    });

    builder.append("  }\n");
  }

  public static void main(String[] args) throws Exception {
    transpile(new File("C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\J2JS\\src\\main\\java\\giada\\j2js\\Prova.java"));
  }
}
