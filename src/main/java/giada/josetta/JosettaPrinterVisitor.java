package giada.josetta;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.DefaultPrettyPrinterVisitor;
import com.github.javaparser.printer.configuration.ConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.Indentation;
import static com.github.javaparser.utils.PositionUtils.sortByBeginPosition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * The Josetta printer visitor
 *
 * @author gianpiero.di.blasi
 */
public class JosettaPrinterVisitor extends DefaultPrettyPrinterVisitor {

  private final static Indentation INDENTATION = new Indentation(Indentation.IndentType.SPACES, 2);
  private final static DefaultConfigurationOption INDENTATION_OPTION = new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, JosettaPrinterVisitor.INDENTATION);

  /**
   * Creates a printer visitor
   */
  public JosettaPrinterVisitor() {
    super(new DefaultPrinterConfiguration().addOption(JosettaPrinterVisitor.INDENTATION_OPTION));
  }

  @Override
  public void visit(PackageDeclaration n, Void arg) {
  }

  @Override
  public void visit(ImportDeclaration n, Void arg) {
  }

  @Override
  public void visit(NormalAnnotationExpr n, Void arg) {
  }

  @Override
  public void visit(SingleMemberAnnotationExpr n, Void arg) {
  }

  @Override
  public void visit(MarkerAnnotationExpr n, Void arg) {
  }

  @Override
  public void visit(AnnotationMemberDeclaration n, Void arg) {
  }

  @Override
  public void visit(AnnotationDeclaration n, Void arg) {
  }

  @Override
  public void visit(EnumConstantDeclaration n, Void arg) {
  }

  @Override
  public void visit(EnumDeclaration n, Void arg) {
  }

  @Override
  public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
    if (!n.getNameAsString().startsWith("$")) {
      super.visit(n, arg);
    }
  }

  @Override
  public void visit(final ObjectCreationExpr n, final Void arg) {
    String name = n.getType().getName().asString();
    if (name.startsWith("$")) {
      n.getType().setName(name.substring(1));
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(final MethodDeclaration n, final Void arg) {
    if (!n.getNameAsString().startsWith("$")) {
      super.visit(n, arg);
    }
  }

  @Override
  public void visit(MethodReferenceExpr n, Void arg) {
    String identifier = n.getIdentifier();
    if (identifier != null && identifier.startsWith("$")) {
      n.setIdentifier(identifier.substring(1));
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(final MethodCallExpr n, final Void arg) {
    String name = n.getName().asString();
    if (name.startsWith("$")) {
      n.setName(name.substring(1));
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(BinaryExpr n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);
    n.getLeft().accept(this, arg);
    if (getOption(DefaultPrinterConfiguration.ConfigOption.SPACE_AROUND_OPERATORS).isPresent()) {
      printer.print(" ");
    }

    printer.print(n.getOperator().asString());
    switch (n.getOperator()) {
      case EQUALS:
      case NOT_EQUALS:
        printer.print("=");
        break;
    }

    if (getOption(DefaultPrinterConfiguration.ConfigOption.SPACE_AROUND_OPERATORS).isPresent()) {
      printer.print(" ");
    }
    n.getRight().accept(this, arg);
  }

  @Override
  @SuppressWarnings({"unchecked", "null"})
  public void visit(LambdaExpr n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);

    final NodeList<Parameter> parameters = n.getParameters();
    final boolean printPar = n.isEnclosingParameters();

    if (printPar) {
      printer.print("(");
    }
    for (Iterator<Parameter> i = parameters.iterator(); i.hasNext();) {
      Parameter p = i.next();
      p.accept(this, arg);
      if (i.hasNext()) {
        printer.print(", ");
      }
    }
    if (printPar) {
      printer.print(")");
    }

    printer.print(" => ");
    final Statement body = n.getBody();
    if (body instanceof ExpressionStmt) {
      // Print the expression directly
      ((NodeWithExpression<ExpressionStmt>) body).getExpression().accept(this, arg);
    } else {
      body.accept(this, arg);
    }
  }

  @SuppressWarnings("null")
  private void printOrphanCommentsBeforeThisChildNode(final Node node) {
    if (!getOption(DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS).isPresent()) {
      return;
    }
    if (node instanceof Comment) {
      return;
    }

    Node parent = node.getParentNode().orElse(null);
    if (parent == null) {
      return;
    }
    List<Node> everything = new ArrayList<>(parent.getChildNodes());
    sortByBeginPosition(everything);
    int positionOfTheChild = -1;
    for (int i = 0; i < everything.size(); ++i) { // indexOf is by equality, so this is used to index by identity
      if (everything.get(i) == node) {
        positionOfTheChild = i;
        break;
      }
    }
    if (positionOfTheChild == -1) {
      throw new AssertionError("I am not a child of my parent.");
    }
    int positionOfPreviousChild = -1;
    for (int i = positionOfTheChild - 1; i >= 0 && positionOfPreviousChild == -1; i--) {
      if (!(everything.get(i) instanceof Comment)) {
        positionOfPreviousChild = i;
      }
    }
    for (int i = positionOfPreviousChild + 1; i < positionOfTheChild; i++) {
      Node nodeToPrint = everything.get(i);
      if (!(nodeToPrint instanceof Comment)) {
        throw new RuntimeException(
                "Expected comment, instead " + nodeToPrint.getClass() + ". Position of previous child: "
                + positionOfPreviousChild + ", position of child " + positionOfTheChild);
      }
      nodeToPrint.accept(this, null);
    }
  }

  private Optional<ConfigurationOption> getOption(DefaultPrinterConfiguration.ConfigOption cOption) {
    return configuration.get(new DefaultConfigurationOption(cOption));
  }
}
