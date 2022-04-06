package giada.josetta;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import giada.josetta.es6.ES6CompilationUnit;
import giada.josetta.transpiler.JosettaCompilationUnit;
import java.io.File;
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
   * @return The ES6 code
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
    String javaCode = Files.readString(in.toPath());
    String esCode = this.transpile(javaCode);
    Files.writeString(out.toPath(), esCode);
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private void transpileDir(File in, File out) throws Exception {
    if (!in.exists() || in.isHidden()) {
    } else if (in.isDirectory()) {
      for (File child : in.listFiles()) {
        this.transpileDir(child, new File(out, child.getName()));
      }
    } else if (in.isFile() && in.getName().toLowerCase().endsWith(".java")) {
      out = new File(out.getParentFile(), out.getName().toLowerCase().replace(".java", ".js"));
      System.out.println("transpiling " + in + " into " + out);
      this.transpile(in, out);
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
        new Josetta().transpileDir(in, out);
      }
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      new HelpFormatter().printHelp("Options info", options);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }
}
