package giada.josetta;

import giada.josetta.Josetta;
import java.io.File;

public class JosettaTest {

  public static void main(String[] args) throws Exception {
    File in = new File(System.getProperty("user.dir"), "/src/test/java/giada/josetta/Prova.java");
    File out = new File(in.getParentFile(), in.getName().replace(".java", ".js"));
    Josetta.transpile(in, out);
  }
}
