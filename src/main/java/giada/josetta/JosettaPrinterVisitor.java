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
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.EQUALS;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.NOT_EQUALS;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
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
 * @author gianpiero.diblasi
 */
public class JosettaPrinterVisitor extends DefaultPrettyPrinterVisitor {

  private final String[] ag, as, ex, to, ap, nt;

  private final boolean nbmo;
  private final List<String> nbmoMethodList = List.of("getContentPane", "setTitle");
  private final List<String> nbmoVariableListNO = List.of("gridBagConstraints", "layout");
  private boolean initComponents;

  private final static Indentation INDENTATION = new Indentation(Indentation.IndentType.SPACES, 2);
  private final static DefaultConfigurationOption INDENTATION_OPTION = new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.INDENTATION, JosettaPrinterVisitor.INDENTATION);

  /**
   * Creates a printer visitor
   *
   * @param ag The list of array getter methods
   * @param as The list of array setter methods
   * @param ex The list of exists methods
   * @param to The list of typeof methods
   * @param ap The list of apply methods
   * @param nt The list of no transpilation symbols
   * @param nbmo true to perform the NetBeans Matisse Optimization, false
   * otherwise
   */
  public JosettaPrinterVisitor(String[] ag, String[] as, String[] ex, String[] to, String[] ap, String[] nt, boolean nbmo) {
    super(new DefaultPrinterConfiguration().addOption(JosettaPrinterVisitor.INDENTATION_OPTION));

    this.ag = ag;
    this.as = as;
    this.ex = ex;
    this.to = to;
    this.ap = ap;
    this.nt = nt;
    this.nbmo = nbmo;
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
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);
    
    printer.print("static ");
    n.getName().accept(this, arg);
    printer.println(" = '" + n.getName() + "';");
  }

  @Override
  public void visit(EnumDeclaration n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);

    printer.print("class ");
    n.getName().accept(this, arg);

    printer.println(" {");
    printer.indent();
    printer.println();
    n.getEntries().forEach(entry -> entry.accept(this, arg));
    printer.unindent();
    printer.print("}");
  }

  @Override
  public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
    if (startsWith(n.getNameAsString()) == 0) {
      super.visit(n, arg);
    }
  }

  @Override
  public void visit(final ClassOrInterfaceType n, final Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);
    if (n.getScope().isPresent()) {
      n.getScope().get().accept(this, arg);
      printer.print(".");
    }
    printAnnotations(n.getAnnotations(), false, arg);

    n.getName().accept(this, arg);
  }

  @Override
  public void visit(final ObjectCreationExpr n, final Void arg) {
    ClassOrInterfaceType type = n.getType();
    type.setScope(null);

    String name = type.getName().asString();
    int startsWith = startsWith(name);
    if (startsWith != 0) {
      name = name.substring(startsWith);
      type.setName(name);
    }

    super.visit(n, arg);
  }

  @Override
  public void visit(final CastExpr n, final Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);
    n.getExpression().accept(this, arg);
  }

  @Override
  public void visit(final MethodDeclaration n, final Void arg) {
    String name = n.getNameAsString();
    if (startsWith(name) == 0) {
      initComponents = name.equals("initComponents");
      super.visit(n, arg);
      initComponents = false;
    }
  }

  @Override
  public void visit(final MethodCallExpr n, final Void arg) {
    String name = n.getName().asString();
    int startsWith = startsWith(name);

    if (isGetter(name)) {
      this.visit(new ArrayAccessExpr(n.getScope().get(), n.getArguments().get(0)), arg);
    } else if (isSetter(name)) {
      Expression expression = n.getScope().get();
      expression.ifNameExpr(exp -> this.visit(exp, arg));
      expression.ifFieldAccessExpr(exp -> this.visit(exp, arg));
      printer.print("[");
      n.getArguments().get(0).accept(this, arg);
      printer.print("] = ");
      n.getArguments().get(1).accept(this, arg);
    } else if (isExists(name)) {
      n.getParentNode().ifPresentOrElse(node -> {
        if (VariableDeclarator.class.isInstance(node)) {
          printer.print("!!(");
          n.getArguments().get(0).accept(this, arg);
          printer.print(")");
        } else if (IfStmt.class.isInstance(node)) {
          n.getArguments().get(0).accept(this, arg);
        } else if (BinaryExpr.class.isInstance(node)) {
          n.getArguments().get(0).accept(this, arg);
        } else if (ConditionalExpr.class.isInstance(node)) {
          n.getArguments().get(0).accept(this, arg);
        } else if (UnaryExpr.class.isInstance(node)) {
          n.getArguments().get(0).accept(this, arg);
        } else {
          printer.print("!!(");
          n.getArguments().get(0).accept(this, arg);
          printer.print(")");
        }
      }, () -> {
        printer.print("!!(");
        n.getArguments().get(0).accept(this, arg);
        printer.print(")");
      });

    } else if (isTypeOf(name)) {
      printer.print("typeof ");
      n.getArguments().get(0).accept(this, arg);
      printer.print(" === ");
      n.getArguments().get(1).accept(this, arg);
    } else if (isApply(name)) {
      Expression expression = n.getScope().get();
      expression.ifFieldAccessExpr(exp -> this.visit(new MethodCallExpr(exp.getScope(), exp.getNameAsString(), n.getArguments()), arg));
      expression.ifNameExpr(exp -> this.visit(new MethodCallExpr(null, exp.getNameAsString(), n.getArguments()), arg));
    } else if (startsWith != 0) {
      n.setName(name.substring(startsWith));
      super.visit(n, arg);
    } else if (nbmo && initComponents) {
      n.setName((nbmoMethodList.contains(name) && !n.isQualified() ? "this." : "") + name);
      super.visit(n, arg);
    } else {
      super.visit(n, arg);
    }
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

  @Override
  public void visit(MethodReferenceExpr n, Void arg) {
    printOrphanCommentsBeforeThisChildNode(n);
    printComment(n.getComment(), arg);

    printer.print("(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10) => ");
    n.getScope().accept(this, arg);
    printer.print("." + n.getIdentifier() + "(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)");
  }

  @Override
  public void visit(AssignExpr n, Void arg) {
    if (nbmo && initComponents) {
      NodeWithSimpleName<?> target = (NodeWithSimpleName) n.getTarget();

      boolean startsWith = false;
      String name = target.getNameAsString();
      for (String variable : nbmoVariableListNO) {
        startsWith |= target.toString().startsWith(variable);
      }

      if (!startsWith && !nbmoVariableListNO.contains(name)) {
        target.setName("this." + name);
        super.visit(n, arg);
        printer.print(";let " + name + " = this." + name);
      } else {
        super.visit(n, arg);
      }
    } else {
      super.visit(n, arg);
    }
  }

  @Override
  public void visit(ArrayCreationExpr n, Void arg) {
    if (nbmo && initComponents) {
      printer.print("[");
      n.getInitializer().ifPresent(initializer -> initializer.getValues().forEach(expression -> {
        expression.accept(this, arg);
        printer.print(", ");
      }));
      printer.print("]");
    } else {
      super.visit(n, arg);
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

  private boolean isGetter(String string) {
    for (String str : ag) {
      if (string.equals(str)) {
        return true;
      }
    }
    return false;
  }

  private boolean isSetter(String string) {
    for (String str : as) {
      if (string.equals(str)) {
        return true;
      }
    }
    return false;
  }

  private boolean isExists(String string) {
    for (String str : ex) {
      if (string.equals(str)) {
        return true;
      }
    }
    return false;
  }

  private boolean isTypeOf(String string) {
    for (String str : to) {
      if (string.equals(str)) {
        return true;
      }
    }
    return false;
  }

  private boolean isApply(String string) {
    for (String str : ap) {
      if (string.equals(str)) {
        return true;
      }
    }
    return false;
  }

  private int startsWith(String string) {
    for (String str : nt) {
      if (string.startsWith(str)) {
        return str.length();
      }
    }
    return 0;
  }
}
