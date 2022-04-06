package giada.josetta.transpiler;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import giada.josetta.es6.ES6ConstructorDeclaration;

/**
 * The transpiler of a constructor declaration
 *
 * @author gianpiero.di.blasi
 */
public class JosettaConstructorDeclaration extends JosettaCallableDeclaration<ConstructorDeclaration, ES6ConstructorDeclaration> {
}
