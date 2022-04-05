package giada.josetta;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;

public class ClassParameterInitializationTranspiler {

  private static boolean done = false;

  public static void transpile(StringBuilder builder, String name, VariableDeclarator variableDeclarator) {
    done = false;
    Type type = variableDeclarator.getType();

    type.ifArrayType(ty -> throwException(name, type));

    type.ifClassOrInterfaceType(ty -> append(builder, "null"));
    type.ifIntersectionType(ty -> throwException(name, type));
    type.ifPrimitiveType(ty -> {
      switch (ty.getType()) {
        case BOOLEAN:
          append(builder, false);
          break;
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
          append(builder, 0);
          break;
        case CHAR:
          append(builder, "\"\"");
          break;
      }
    });
    type.ifReferenceType(ty -> throwException(name, type));
    type.ifUnionType(ty -> throwException(name, type));
    type.ifUnknownType(ty -> throwException(name, type));
    type.ifVarType(ty -> throwException(name, type));
    type.ifVoidType(ty -> throwException(name, type));
    type.ifWildcardType(ty -> throwException(name, type));

    throwException(name, type);
  }

  private static void append(StringBuilder builder, Object object) {
    if (!done) {
      builder.append(object);
      done = true;
    }
  }

  private static void throwException(String name, Type type) {
    if (!done) {
      throw new RuntimeException("Class " + name + ": variable declaration type not yet handled => [" + type.getClass().getSimpleName() + "]" + type);
    }
  }

  private ClassParameterInitializationTranspiler() {
  }
}
