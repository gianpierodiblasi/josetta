package giada.josetta;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
   * Transpiles a file into another file
   *
   * @param in The input file
   * @param out The output file
   * @param ag The list of array getter methods
   * @param as The list of array setter methods
   * @param ex The list of exists methods
   * @param to The list of typeof methods
   * @param ap The list of apply methods
   * @param nt The list of no transpilation symbols
   * @throws Exception thrown if an error occurs
   */
  public static void transpile(File in, File out, String[] ag, String[] as, String[] ex, String[] to, String[] ap, String[] nt) throws Exception {
    String javaCode = Files.readString(in.toPath());
    String esCode = Josetta.transpile(javaCode, ag, as, ex, to, ap, nt).replaceAll("\\R{3,}+", "\n");

    if (!esCode.trim().isEmpty() && !esCode.isBlank()) {
      out.getParentFile().mkdirs();
      Files.writeString(out.toPath(), esCode);
    }
  }

  /**
   * Transpiles a Java code into ES6 code
   *
   * @param javaCode The java code
   * @param ag The list of array getter methods
   * @param as The list of array setter methods
   * @param ex The list of exists methods
   * @param to The list of typeof methods
   * @param ap The list of apply methods
   * @param nt The list of no transpilation symbols
   * @return The ES6 code
   * @throws Exception thrown if an error occurs
   */
  public static String transpile(String javaCode, String[] ag, String[] as, String[] ex, String[] to, String[] ap, String[] nt) throws Exception {
    CompilationUnit compilationUnit = StaticJavaParser.parse(javaCode);

    Josetta.codeCleaning(compilationUnit);
    JosettaChecker.checkCompilationUnit(compilationUnit, ag, as, ex, to, ap, nt);

    JosettaPrinterVisitor visitor = new JosettaPrinterVisitor(ag, as, ex, to, ap, nt);
    compilationUnit.accept(visitor, null);
    return visitor.toString();
  }

  private static void codeCleaning(CompilationUnit compilationUnit) {
    compilationUnit.removePackageDeclaration();
    compilationUnit.findAll(ImportDeclaration.class).forEach(importDeclaration -> compilationUnit.remove(importDeclaration));
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static void transpileDir(File in, File out, String[] ag, String[] as, String[] ex, String[] to, String[] ap, String[] nt) throws Exception {
    if (!in.exists() || in.isHidden()) {
    } else if (in.isDirectory()) {
      for (File child : in.listFiles()) {
        Josetta.transpileDir(child, new File(out, child.getName()), ag, as, ex, to, ap, nt);
      }
    } else if (in.isFile() && in.getName().endsWith(".java")) {
      out = new File(out.getParentFile(), out.getName().replace(".java", ".js"));
      System.out.println("transpiling " + in + " into " + out);
      Josetta.transpile(in, out, ag, as, ex, to, ap, nt);
    }
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private static void watch(File in, File out, String[] ag, String[] as, String[] ex, String[] to, String[] ap, String[] nt) throws Exception {
    System.out.println("watching " + in + " into " + out);

    Path inPath = in.toPath();
    Path outPath = out.toPath();
    Map<WatchKey, Path> map = new HashMap<>();

    WatchService watchService = FileSystems.getDefault().newWatchService();
    map.put(inPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE), inPath);

    Files.walkFileTree(inPath, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {
        map.put(path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE), path);
        return FileVisitResult.CONTINUE;
      }
    });

    while (true) {
      WatchKey watchKey = watchService.take();

      watchKey.pollEvents().forEach(event -> {
        Kind<?> kind = event.kind();
        Path mapPath = map.get(watchKey);
        Path path = (Path) event.context();

        Path inEventPath = mapPath.resolve(path);
        Path outEventPath = outPath.resolve(inPath.relativize(mapPath)).resolve(path);
        File inEventFile = inEventPath.toFile();
        File outEventFile = outEventPath.toFile();

        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
          if (inEventFile.isDirectory()) {
            System.out.println("creating folder " + outEventFile);
            outEventFile.mkdirs();

            try {
              outEventPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            } catch (IOException exc) {
              throw new RuntimeException(exc.getMessage(), exc);
            }
          } else {
            Josetta.transpileInWatch(inEventFile, outEventFile, ag, as, ex, to, ap, nt);
          }
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
          if (inEventFile.isFile()) {
            Josetta.transpileInWatch(inEventFile, outEventFile, ag, as, ex, to, ap, nt);
          }
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
          if (outEventFile.isDirectory()) {
            System.out.println("deleting folder " + outEventFile);
            Josetta.delete(outEventFile);
          }

          outEventFile = new File(outEventFile.getParentFile(), outEventFile.getName().replace(".java", ".js"));
          if (outEventFile.isFile()) {
            System.out.println("deleting file " + outEventFile);
            Josetta.delete(outEventFile);
          }
        }
      });

      watchKey.reset();
    }
  }

  @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
  private static void transpileInWatch(File inFile, File outFile, String[] ag, String[] as, String[] ex, String[] to, String[] ap, String[] nt) {
    if (inFile.isFile() && inFile.getName().endsWith(".java")) {
      outFile = new File(outFile.getParentFile(), outFile.getName().replace(".java", ".js"));

      try {
        System.out.println("transpiling " + inFile + " into " + outFile);
        Josetta.transpile(inFile, outFile, ag, as, ex, to, ap, nt);
      } catch (Exception exc) {
        System.out.println(exc.getMessage());
        exc.printStackTrace();
      }
    }
  }

  @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
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
      ex.printStackTrace();
    }
  }

  @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(Option.builder("in").hasArg().desc("Input dir/file").argName("in").required().build());
    options.addOption(Option.builder("out").hasArg().desc("Output dir/file").argName("out").required().build());
    options.addOption(Option.builder("w").desc("Watch for files changes").argName("w").build());
    options.addOption(Option.builder("ag").desc("Array getter methods").argName("ag").build());
    options.addOption(Option.builder("as").desc("Array setter methods").argName("as").build());
    options.addOption(Option.builder("ex").desc("Exists methods").argName("ex").build());
    options.addOption(Option.builder("to").desc("TypeOf methods").argName("to").build());
    options.addOption(Option.builder("ap").desc("Apply methods").argName("ap").build());
    options.addOption(Option.builder("nt").desc("No transpilation symbols").argName("nt").build());

    try {
      CommandLine cmd = new DefaultParser().parse(options, args);
      File in = new File(cmd.getOptionValue("in"));
      File out = new File(cmd.getOptionValue("out"));

      String ag[] = cmd.hasOption("ag") ? cmd.getOptionValue("ag").split(",") : new String[]{"$get"};
      String as[] = cmd.hasOption("as") ? cmd.getOptionValue("as").split(",") : new String[]{"$set"};
      String ex[] = cmd.hasOption("ex") ? cmd.getOptionValue("ex").split(",") : new String[]{"$exists"};
      String to[] = cmd.hasOption("to") ? cmd.getOptionValue("to").split(",") : new String[]{"$typeof"};
      String ap[] = cmd.hasOption("ap") ? cmd.getOptionValue("ap").split(",") : new String[]{"$apply"};
      String nt[] = cmd.hasOption("nt") ? cmd.getOptionValue("nt").split(",") : new String[]{"$"};

      if (cmd.hasOption("w")) {
        Josetta.watch(in, out, ag, as, ex, to, ap, nt);
      } else {
        Josetta.transpileDir(in, out, ag, as, ex, to, ap, nt);
      }
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
      new HelpFormatter().printHelp("Options info", options);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }
}
