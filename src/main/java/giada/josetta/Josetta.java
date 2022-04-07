package giada.josetta;

import com.github.javaparser.ParseException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
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
    } else if (in.isFile() && in.getName().endsWith(".java")) {
      out = new File(out.getParentFile(), out.getName().replace(".java", ".js"));
      System.out.println("transpiling " + in + " into " + out);
      Josetta.transpile(in, out);
    }
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static void watch(File in, File out) throws Exception {
    System.out.println("watching " + in);

    Path inPath = in.toPath();
    Path outPath = out.toPath();

    WatchService watchService = FileSystems.getDefault().newWatchService();
    inPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

    while (true) {
      WatchKey watchKey = watchService.take();

      watchKey.pollEvents().forEach(event -> {
        Kind<?> kind = event.kind();
        File inFile = inPath.resolve((Path) event.context()).toFile();
        File outFile = outPath.resolve((Path) event.context()).toFile();

        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
          if (inFile.isDirectory()) {
            System.out.println("creating folder " + outFile);
            outFile.mkdirs();
          } else if (inFile.isFile() && inFile.getName().endsWith(".java")) {
            outFile = new File(outFile.getParentFile(), inFile.getName().replace(".java", ".js"));
            try {
              Josetta.transpile(inFile, outFile);
            } catch (Exception ex) {
              System.out.println(ex.getMessage());
            }
          }
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
          if (inFile.isDirectory()) {
          } else if (inFile.isFile()) {
          }
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
          System.out.println("deleting file/folder " + outFile);
          Josetta.delete(outFile);
        }
      });

      watchKey.reset();
    }
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static void delete(File file) {
    try {
      if (!file.exists()) {
        return;
      }

      if (file.isDirectory()) {
        for (File f : file.listFiles()) {
          Josetta.delete(f);
        }
      }
      Files.delete(file.toPath());
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
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
      File in = new File(cmd.getOptionValue("in"));
      File out = new File(cmd.getOptionValue("out"));

      if (cmd.hasOption("w")) {
        Josetta.watch(in, out);
      } else {
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
