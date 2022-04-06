package giada.josetta;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import giada.josetta.es6.ES6CompilationUnit;
import giada.josetta.transpiler.JosettaCompilationUnit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The main class
 *
 * @author gianpiero.di.blasi
 */
public class Josetta {

  /**
   * Transpiles a Java code
   *
   * @param javacode The java code
   * @return The ES6
   * @throws Exception thrown if an error occurs
   */
  public String transpile(String javacode) throws Exception {
    CompilationUnit javaCompilationUnit = StaticJavaParser.parse(javacode);
    ES6CompilationUnit es6CompilationUnit = new ES6CompilationUnit();
    
    new JosettaCompilationUnit().transpile(javaCompilationUnit, es6CompilationUnit);
    
    return es6CompilationUnit.toString();
  }

  /**
   * Transpiles a file into another file
   *
   * @param in The input file
   * @param out The output file
   * @throws Exception thrown if an error occurs
   */
  public void transpile(File in, File out) throws Exception {
    String esCode = this.transpile(Files.readString(in.toPath()));

    try ( Writer writer = new BufferedWriter(new FileWriter(out))) {
      writer.write(esCode);
    }
  }

//  private void transpileClassParameter(Writer writer, FieldDeclaration fieldDeclaration) {
//    fieldDeclaration.getVariables().forEach(variable -> {
//      append(writer, "  ", variable.getNameAsString(), " = ");
//      variable.getInitializer().ifPresentOrElse(
//              expression -> new ExpressionTranspiler().transpile(writer, className, expression),
//              () -> new ClassParameterInitializationTranspiler().transpile(writer, className, variable)
//      );
//      append(writer, ";\n");
//    });
//  }
//  private void transpileConstructor(Writer writer, ConstructorDeclaration constructorDeclaration) {
//    if (!constructorDeclaration.isPrivate()) {
//      parameterCount = 0;
//      append(writer, "  constructor(");
//      constructorDeclaration.getParameters().forEach(parameter -> append(writer, (parameterCount++) == 0 ? "" : ", ", parameter.getNameAsString()));
//      append(writer, ") {\n");
//
//      //BODY CONSTRUCTOR
//      append(writer, "  }\n");
//    }
//  }
//  private void transpileMethod(Writer writer, MethodDeclaration methodDeclaration) {
//    parameterCount = 0;
//    String methodName = methodDeclaration.getNameAsString();
//
//    append(writer, "  ", methodName, "(");
//    methodDeclaration.getParameters().forEach(parameter -> append(writer, (parameterCount++) == 0 ? "" : ", ", parameter.getNameAsString()));
//    append(writer, ") {\n");
//
//    //BODY METHOD
//    append(writer, "  }\n");
//  }
//  private Writer append(Writer writer, String... args) {
//    for (String arg : args) {
//      try {
//        writer.append(arg);
//      } catch (IOException ex) {
//        throw new RuntimeException(ex);
//      }
//    }
//    return writer;
//  }
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
