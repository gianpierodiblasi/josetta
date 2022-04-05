package giada.josetta;

import com.github.javaparser.ast.expr.Expression;

public class ExpressionTranspiler {

  private static boolean done = false;

  public static void transpile(StringBuilder builder, String name, Expression expression) {
    done = false;

    expression.ifAnnotationExpr(exp -> throwException(name, expression));
    expression.ifBooleanLiteralExpr(exp -> append(builder, expression));
    expression.ifCharLiteralExpr(exp -> append(builder, expression));
    expression.ifDoubleLiteralExpr(exp -> append(builder, expression.asDoubleLiteralExpr().asDouble()));
    expression.ifIntegerLiteralExpr(exp -> append(builder, expression.asIntegerLiteralExpr().asNumber()));
    expression.ifLongLiteralExpr(exp -> append(builder, expression.asLongLiteralExpr().asNumber()));
    expression.ifNullLiteralExpr(exp -> append(builder, "null"));
    expression.ifStringLiteralExpr(exp -> append(builder, expression));
    expression.ifTextBlockLiteralExpr(exp -> throwException(name, expression));
    expression.ifLiteralStringValueExpr(exp -> throwException(name, expression));
    expression.ifLiteralExpr(exp -> throwException(name, expression));
    expression.ifArrayAccessExpr(exp -> throwException(name, expression));
    expression.ifArrayCreationExpr(exp -> throwException(name, expression));
    expression.ifArrayInitializerExpr(exp -> throwException(name, expression));
    expression.ifAssignExpr(exp -> throwException(name, expression));
    expression.ifBinaryExpr(exp -> throwException(name, expression));
    expression.ifCastExpr(exp -> transpile(builder, name, expression.asCastExpr().getExpression()));
    expression.ifClassExpr(exp -> throwException(name, expression));
    expression.ifConditionalExpr(exp -> throwException(name, expression));
    expression.ifEnclosedExpr(exp -> throwException(name, expression));
    expression.ifFieldAccessExpr(exp -> throwException(name, expression));
    expression.ifInstanceOfExpr(exp -> throwException(name, expression));
    expression.ifLambdaExpr(exp -> throwException(name, expression));
    expression.ifMarkerAnnotationExpr(exp -> throwException(name, expression));
    expression.ifMethodCallExpr(exp -> throwException(name, expression));
    expression.ifMethodReferenceExpr(exp -> throwException(name, expression));
    expression.ifNameExpr(exp -> throwException(name, expression));
    expression.ifNormalAnnotationExpr(exp -> throwException(name, expression));
    expression.ifObjectCreationExpr(exp -> append(builder, expression));
    expression.ifPatternExpr(exp -> throwException(name, expression));
    expression.ifSingleMemberAnnotationExpr(exp -> throwException(name, expression));
    expression.ifSuperExpr(exp -> throwException(name, expression));
    expression.ifSwitchExpr(exp -> throwException(name, expression));
    expression.ifThisExpr(exp -> throwException(name, expression));
    expression.ifTypeExpr(exp -> throwException(name, expression));
    expression.ifUnaryExpr(exp -> throwException(name, expression));
    expression.ifVariableDeclarationExpr(exp -> throwException(name, expression));

    throwException(name, expression);
  }

  private static void append(StringBuilder builder, Object object) {
    if (!done) {
      builder.append(object);
      done = true;
    }
  }

  private static void throwException(String name, Expression expression) {
    if (!done) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => [" + expression.getClass().getSimpleName() + "]" + expression);
    }
  }

  private ExpressionTranspiler() {
  }
}
