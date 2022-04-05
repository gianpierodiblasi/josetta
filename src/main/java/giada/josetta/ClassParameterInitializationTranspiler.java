package giada.josetta;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import java.io.IOException;
import java.io.Writer;

public class ClassParameterInitializationTranspiler {

  private boolean done;

  public void transpile(Writer writer, String className, VariableDeclarator variableDeclarator) {
    done = false;

    Type type = variableDeclarator.getType();
    type.ifArrayType(ty -> throwException(className, type));
    type.ifClassOrInterfaceType(ty -> append(writer, "null"));
    type.ifIntersectionType(ty -> throwException(className, type));
    type.ifPrimitiveType(ty -> {
      switch (ty.getType()) {
        case BOOLEAN:
          append(writer, false);
          break;
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
          append(writer, 0);
          break;
        case CHAR:
          append(writer, "\"\"");
          break;
      }
    });
    type.ifReferenceType(ty -> throwException(className, type));
    type.ifUnionType(ty -> throwException(className, type));
    type.ifUnknownType(ty -> throwException(className, type));
    type.ifVarType(ty -> throwException(className, type));
    type.ifVoidType(ty -> throwException(className, type));
    type.ifWildcardType(ty -> throwException(className, type));

    throwException(className, type);
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

  private void throwException(String className, Type type) {
    if (!done) {
      throw new RuntimeException("Class " + className + ": variable declaration type not yet handled => [" + type.getClass().getSimpleName() + "]" + type);
    }
  }
}
