package giada.josetta;

import com.github.javaparser.ast.expr.Expression;

public class ExpressionTranspiler {

  public static void transpile(StringBuilder builder, String name, Expression expression) {
    if (expression.isAnnotationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isArrayAccessExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isArrayCreationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isArrayInitializerExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isAssignExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isBinaryExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isBooleanLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isCastExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isCharLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isClassExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isConditionalExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isDoubleLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isEnclosedExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isFieldAccessExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isInstanceOfExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isIntegerLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isLambdaExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isLiteralStringValueExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isLongLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isMarkerAnnotationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isMethodCallExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isMethodReferenceExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isNameExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isNormalAnnotationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isNullLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isObjectCreationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isPatternExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isPolyExpression()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isSingleMemberAnnotationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isStandaloneExpression()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isStringLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isSuperExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isSwitchExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isTextBlockLiteralExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isThisExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isTypeExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isUnaryExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else if (expression.isVariableDeclarationExpr()) {
      throw new RuntimeException("Class " + name + ": expression type not yet handled => " + expression);
    } else {
      throw new RuntimeException("Class " + name + ": unknown expression type and not yet handled => " + expression);
    }
  }

  private ExpressionTranspiler() {
  }
}
