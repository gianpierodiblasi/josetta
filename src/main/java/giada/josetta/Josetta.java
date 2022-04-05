package giada.josetta;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Josetta {

  private String className;
  private int parameterCount;

  public void transpile(File in, File out) throws Exception {
    try ( Writer writer = new BufferedWriter(new FileWriter(out))) {
      StaticJavaParser.parse(in).findAll(ClassOrInterfaceDeclaration.class).forEach(declaration -> transpileClass(writer, declaration));
    } catch (Exception ex) {
      out.delete();
      throw ex;
    }
  }

  private void transpileClass(Writer writer, ClassOrInterfaceDeclaration classDeclaration) {
    className = classDeclaration.getNameAsString();
    append(writer, "class ", className);
    classDeclaration.getExtendedTypes().forEach(extendedType -> append(writer, " extends ", extendedType.getNameAsString()));
    append(writer, " {\n");

    classDeclaration.getFields().forEach(declaration -> transpileClassParameter(writer, declaration));
    append(writer, "\n");

    List<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
    if (constructors.isEmpty()) {
      append(writer, "  constructor() {}\n");
    } else if (constructors.size() > 1) {
      throw new RuntimeException("Class " + className + " has more than one constructor");
    } else {
      constructors.forEach(constructorDeclaration -> transpileConstructor(writer, constructorDeclaration));
    }
    append(writer, "\n");

    classDeclaration.getMethods().forEach(declaration -> transpileMethod(writer, declaration));

    append(writer, "}");
  }

  private void transpileClassParameter(Writer writer, FieldDeclaration fieldDeclaration) {
    fieldDeclaration.getVariables().forEach(variable -> {
      append(writer, "  ", variable.getNameAsString(), " = ");
      variable.getInitializer().ifPresentOrElse(
              expression -> new ExpressionTranspiler().transpile(writer, className, expression),
              () -> new ClassParameterInitializationTranspiler().transpile(writer, className, variable)
      );
      append(writer, ";\n");
    });
  }

  private void transpileConstructor(Writer writer, ConstructorDeclaration constructorDeclaration) {
    parameterCount = 0;
    append(writer, "  constructor(");
    constructorDeclaration.getParameters().forEach(parameter -> append(writer, (parameterCount++) == 0 ? "" : ", ", parameter.getNameAsString()));
    append(writer, ") {\n");

    //BODY CONSTRUCTOR
    append(writer, "  }\n");
  }

  private void transpileMethod(Writer writer, MethodDeclaration methodDeclaration) {
    parameterCount = 0;
    String methodName = methodDeclaration.getNameAsString();

    append(writer, "  ", methodName, "(");
    methodDeclaration.getParameters().forEach(parameter -> append(writer, (parameterCount++) == 0 ? "" : ", ", parameter.getNameAsString()));
    append(writer, ") {\n");

    //BODY METHOD
    append(writer, "  }\n");
  }

  private Writer append(Writer writer, String... args) {
    for (String arg : args) {
      try {
        writer.append(arg);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    return writer;
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static void transpileDir(Josetta josetta, File in, File out) throws Exception {
    if (!in.exists() || in.isHidden()) {
    } else if (in.isDirectory()) {
      for (File child : in.listFiles((dir, name) -> name.toLowerCase().endsWith(".java"))) {
        transpileDir(josetta, child, new File(out, child.getName()));
      }
    } else if (in.isFile()) {
      out = new File(out.getParentFile(), out.getName().replace(".java", ".js"));
      System.out.println("transpile " + in + " to " + out);
      josetta.transpile(in, out);
    }
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(Option.builder("in").hasArg().desc("Input dir/file").argName("inDir").required().build());
    options.addOption(Option.builder("out").hasArg().desc("Output dir/file").argName("outDir").required().build());
    options.addOption(Option.builder("w").desc("Watch for file changes").argName("w").build());

    try {
      CommandLine cmd = new DefaultParser().parse(options, args);

      if (cmd.hasOption("w")) {
      } else {
        File inDir = new File(cmd.getOptionValue("inDir"));
        File outDir = new File(cmd.getOptionValue("inDir"));
        transpileDir(new Josetta(), inDir, outDir);
      }
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      new HelpFormatter().printHelp("Options info", options);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }
}

//    methodDeclaration.getBody().ifPresent(body -> {
//      if (body.isAssertStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isBlockStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isBreakStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isContinueStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isDoStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isEmptyStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isExplicitConstructorInvocationStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isExpressionStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isForEachStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isForStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isIfStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isLabeledStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isLocalClassDeclarationStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isLocalRecordDeclarationStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isReturnStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isSwitchStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isSynchronizedStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isThrowStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isTryStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isUnparsableStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isWhileStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isYieldStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      }
//    });

