package giada.josetta;

import com.github.javaparser.ParseException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.io.File;
import java.nio.file.Files;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * The main class
 *
 * @author gianpiero.di.blasi
 */
public class Josetta {

  /**
   * Transpiles a file into another file
   *
   * @param in The input file
   * @param out The output file
   * @throws Exception thrown if an error occurs
   */
  public static void transpile(File in, File out) throws Exception {
    String javaCode = Files.readString(in.toPath());
    String esCode = Josetta.transpile(javaCode);
    Files.writeString(out.toPath(), esCode);
  }

  /**
   * Transpiles a Java code into ES6 code
   *
   * @param javaCode The java code
   * @return The ES6 code
   * @throws Exception thrown if an error occurs
   */
  public static String transpile(String javaCode) throws Exception {
    CompilationUnit compilationUnit = StaticJavaParser.parse(javaCode);
    
    Josetta.codeCleaning(compilationUnit);
    JosettaChecker.checkCompilationUnit(compilationUnit);
    
    JosettaPrinterVisitor visitor = new JosettaPrinterVisitor();
    compilationUnit.accept(visitor, null);
    return visitor.toString();
  }
  
  private static void codeCleaning(CompilationUnit compilationUnit) {
    compilationUnit.removePackageDeclaration();
    compilationUnit.findAll(ImportDeclaration.class).forEach(importDeclaration -> compilationUnit.remove(importDeclaration));
  }
  
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static void transpileDir(File in, File out) throws Exception {
    if (!in.exists() || in.isHidden()) {
    } else if (in.isDirectory()) {
      for (File child : in.listFiles()) {
        Josetta.transpileDir(child, new File(out, child.getName()));
      }
    } else if (in.isFile() && in.getName().toLowerCase().endsWith(".java")) {
      out = new File(out.getParentFile(), out.getName().toLowerCase().replace(".java", ".js"));
      System.out.println("transpiling " + in + " into " + out);
      Josetta.transpile(in, out);
    }
  }
  
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(Option.builder("in").hasArg().desc("Input dir/file").argName("in").required().build());
    options.addOption(Option.builder("out").hasArg().desc("Output dir/file").argName("out").required().build());
    options.addOption(Option.builder("w").desc("Watch for files changes").argName("w").build());
    
    try {
      CommandLine cmd = new DefaultParser().parse(options, args);
      
      if (cmd.hasOption("w")) {
      } else {
        File in = new File(cmd.getOptionValue("in"));
        File out = new File(cmd.getOptionValue("out"));
        Josetta.transpileDir(in, out);
      }
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      new HelpFormatter().printHelp("Options info", options);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }
}
