package giada.josetta.transpiler;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import giada.josetta.es6.ES6CallableDeclaration;
import giada.josetta.util.JosettaException;

/**
 * The transpiler of a callable declaration (method or constructor)
 *
 * @author gianpiero.di.blasi
 * @param <T> A CallableDeclaration
 * @param <S> A ES6CallableDeclaration
 */
public class JosettaCallableDeclaration<T extends CallableDeclaration<T>, S extends ES6CallableDeclaration> {

  /**
   * Transpiles a Java method declaration into a ES6 method declaration
   *
   * @param javaDeclaration The Java method declaration
   * @param es6Declaration The ES6 method declaration
   * @throws JosettaException thrown if an error occurs
   */
  public void transpile(T javaDeclaration, S es6Declaration) throws JosettaException {
    javaDeclaration.getParameters().forEach(parameter -> es6Declaration.addParameter(parameter.getNameAsString()));

    javaDeclaration.ifMethodDeclaration(declaration -> declaration.getBody().ifPresent(body -> transpile(body, es6Declaration)));
    javaDeclaration.ifConstructorDeclaration(declaration -> transpile(declaration.getBody(), es6Declaration));
  }

  private void transpile(BlockStmt body, S es6Declaration) {
  }
}

//    methodDeclaration.getBody().ifPresent(body -> {
//      if (body.isAssertStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isBlockStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isBreakStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isContinueStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isDoStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isEmptyStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isExplicitConstructorInvocationStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isExpressionStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isForEachStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isForStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isIfStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isLabeledStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isLocalClassDeclarationStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isLocalRecordDeclarationStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isReturnStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isSwitchStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isSynchronizedStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isThrowStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isTryStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isUnparsableStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isWhileStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else if (body.isYieldStmt()) {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      } else {
//        throw new RuntimeException(body.toString() + "NOT MANAGED");
//      }
//    });
