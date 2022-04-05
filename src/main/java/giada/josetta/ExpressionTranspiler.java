package giada.josetta;

import com.github.javaparser.ast.expr.Expression;
import java.io.IOException;
import java.io.Writer;

public class ExpressionTranspiler {

  private boolean done;

  public void transpile(Writer writer, String className, Expression expression) {
    done = false;

    expression.ifAnnotationExpr(exp -> throwException(className, expression));
    expression.ifBooleanLiteralExpr(exp -> append(writer, expression));
    expression.ifCharLiteralExpr(exp -> append(writer, expression));
    expression.ifDoubleLiteralExpr(exp -> append(writer, expression.asDoubleLiteralExpr().asDouble()));
    expression.ifIntegerLiteralExpr(exp -> append(writer, expression.asIntegerLiteralExpr().asNumber()));
    expression.ifLongLiteralExpr(exp -> append(writer, expression.asLongLiteralExpr().asNumber()));
    expression.ifNullLiteralExpr(exp -> append(writer, "null"));
    expression.ifStringLiteralExpr(exp -> append(writer, expression));
    expression.ifTextBlockLiteralExpr(exp -> throwException(className, expression));
    expression.ifLiteralStringValueExpr(exp -> throwException(className, expression));
    expression.ifLiteralExpr(exp -> throwException(className, expression));
    expression.ifArrayAccessExpr(exp -> throwException(className, expression));
    expression.ifArrayCreationExpr(exp -> throwException(className, expression));
    expression.ifArrayInitializerExpr(exp -> throwException(className, expression));
    expression.ifAssignExpr(exp -> throwException(className, expression));
    expression.ifBinaryExpr(exp -> throwException(className, expression));
    expression.ifCastExpr(exp -> transpile(writer, className, expression.asCastExpr().getExpression()));
    expression.ifClassExpr(exp -> throwException(className, expression));
    expression.ifConditionalExpr(exp -> throwException(className, expression));
    expression.ifEnclosedExpr(exp -> throwException(className, expression));
    expression.ifFieldAccessExpr(exp -> throwException(className, expression));
    expression.ifInstanceOfExpr(exp -> throwException(className, expression));
    expression.ifLambdaExpr(exp -> throwException(className, expression));
    expression.ifMarkerAnnotationExpr(exp -> throwException(className, expression));
    expression.ifMethodCallExpr(exp -> throwException(className, expression));
    expression.ifMethodReferenceExpr(exp -> throwException(className, expression));
    expression.ifNameExpr(exp -> throwException(className, expression));
    expression.ifNormalAnnotationExpr(exp -> throwException(className, expression));
    expression.ifObjectCreationExpr(exp -> append(writer, expression));
    expression.ifPatternExpr(exp -> throwException(className, expression));
    expression.ifSingleMemberAnnotationExpr(exp -> throwException(className, expression));
    expression.ifSuperExpr(exp -> throwException(className, expression));
    expression.ifSwitchExpr(exp -> throwException(className, expression));
    expression.ifThisExpr(exp -> throwException(className, expression));
    expression.ifTypeExpr(exp -> throwException(className, expression));
    expression.ifUnaryExpr(exp -> throwException(className, expression));
    expression.ifVariableDeclarationExpr(exp -> throwException(className, expression));

    throwException(className, expression);
  }

  private void append(Writer writer, Object object) {
    if (!done) {
      try {
        writer.append(object.toString());
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      done = true;
    }
  }

  private void throwException(String className, Expression expression) {
    if (!done) {
      throw new RuntimeException("Class " + className + ": expression type not yet handled => [" + expression.getClass().getSimpleName() + "]" + expression);
    }
  }
}
