/*
 * Copyright 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.dev.jjs.impl;

import com.google.gwt.core.ext.arguments.JArgument;
import com.google.gwt.core.ext.arguments.JArrayArgument;
import com.google.gwt.core.ext.arguments.JBooleanArgument;
import com.google.gwt.core.ext.arguments.JCharArgument;
import com.google.gwt.core.ext.arguments.JClassArgument;
import com.google.gwt.core.ext.arguments.JDoubleArgument;
import com.google.gwt.core.ext.arguments.JEnumArgument;
import com.google.gwt.core.ext.arguments.JFloatArgument;
import com.google.gwt.core.ext.arguments.JIntArgument;
import com.google.gwt.core.ext.arguments.JLongArgument;
import com.google.gwt.core.ext.arguments.JVariableArgument;
import com.google.gwt.core.ext.arguments.JNullArgument;
import com.google.gwt.core.ext.arguments.JStringArgument;
import com.google.gwt.dev.jjs.ast.JBooleanLiteral;
import com.google.gwt.dev.jjs.ast.JCharLiteral;
import com.google.gwt.dev.jjs.ast.JClassLiteral;
import com.google.gwt.dev.jjs.ast.JDoubleLiteral;
import com.google.gwt.dev.jjs.ast.JEnumField;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JFieldRef;
import com.google.gwt.dev.jjs.ast.JFloatLiteral;
import com.google.gwt.dev.jjs.ast.JIntLiteral;
import com.google.gwt.dev.jjs.ast.JLiteral;
import com.google.gwt.dev.jjs.ast.JLongLiteral;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JNewArray;
import com.google.gwt.dev.jjs.ast.JNullLiteral;
import com.google.gwt.dev.jjs.ast.JPrimitiveType;
import com.google.gwt.dev.jjs.ast.JStringLiteral;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Translates lists of {@link JExpression} arguments to lists of {@link JArgument}s-
 */
class ExpressionToArgumentTranslator {

  static {
    HashMap<String, JPrimitiveType> wrappers = new HashMap<String, JPrimitiveType>();

    addWrapper(wrappers, JPrimitiveType.BOOLEAN);
    addWrapper(wrappers, JPrimitiveType.CHAR);
    addWrapper(wrappers, JPrimitiveType.DOUBLE);
    addWrapper(wrappers, JPrimitiveType.FLOAT);
    addWrapper(wrappers, JPrimitiveType.INT);
    addWrapper(wrappers, JPrimitiveType.LONG);

    WRAPPERS = Collections.unmodifiableMap(wrappers);
  }

  private static final Map<String, JPrimitiveType> WRAPPERS;

  private static void addWrapper(Map<String, JPrimitiveType> map, JPrimitiveType type) {
    map.put(type.getWrapperTypeName(), type);
  }

  public static JArgument translate(JExpression expression) {
    if (expression instanceof JNewArray) {
      JNewArray newArray = (JNewArray) expression;
      List<JExpression> initializers = newArray.initializers;
      if (initializers != null) {
        JArgument[] elements = new JArgument[initializers.size()];
        for (int i = 0; i < elements.length; i++) {
          elements[i] = translate(initializers.get(i));
        }
        return JArrayArgument.valueOf(elements);
      }
    }
    if (expression instanceof JLiteral) {
      return translateLiteral((JLiteral) expression);
    }
    if (expression instanceof JFieldRef) {
      JField field = ((JFieldRef) expression).getField();
      if (field instanceof JEnumField) {
        JEnumField enumField = (JEnumField) field;
        return JEnumArgument.valueOf(enumField.getEnclosingType().getName(), enumField.ordinal());
      }
      if (field.isCompileTimeConstant()) {
        return translateLiteral(field.getConstInitializer());
      }
    }
    if (expression instanceof JMethodCall) {
      JMethodCall call = (JMethodCall) expression;
      JMethod method = call.getTarget();
      if ("valueOf".equals(method.getName()) && call.getArgs().size() == 1) {
        JPrimitiveType primitive = WRAPPERS.get(method.getEnclosingType().getName());
        if (primitive != null && primitive.equals(call.getArgs().get(0).getType())) {
          JExpression lit = call.getArgs().get(0);
          if (lit instanceof JLiteral) {
            return translateLiteral((JLiteral) lit);
          }
        }
      }
    }
    return JVariableArgument.value();
  }

  public static JArgument[] translate(List<JExpression> expressions) {
    JArgument[] translation = new JArgument[expressions.size()];
    for (int i = 0; i < translation.length; i++) {
      translation[i] = translate(expressions.get(i));
    }
    return translation;
  }

  private static JArgument translateLiteral(JLiteral literal) {
    if (literal instanceof JBooleanLiteral) {
      return JBooleanArgument.valueOf(((JBooleanLiteral) literal).getValue());
    }
    if (literal instanceof JCharLiteral) {
      return JCharArgument.valueOf(((JCharLiteral) literal).getValue());
    }
    if (literal instanceof JClassLiteral) {
      return JClassArgument.valueOf(((JClassLiteral) literal).getRefType().getName());
    }
    if (literal instanceof JDoubleLiteral) {
      return JDoubleArgument.valueOf(((JDoubleLiteral) literal).getValue());
    }
    if (literal instanceof JFloatLiteral) {
      return JFloatArgument.valueOf(((JFloatLiteral) literal).getValue());
    }
    if (literal instanceof JIntLiteral) {
      return JIntArgument.valueOf(((JIntLiteral) literal).getValue());
    }
    if (literal instanceof JLongLiteral) {
      return JLongArgument.valueOf(((JLongLiteral) literal).getValue());
    }
    if (literal instanceof JNullLiteral) {
      return JNullArgument.value();
    }
    if (literal instanceof JStringLiteral) {
      return JStringArgument.valueOf(((JStringLiteral) literal).getValue());
    }
    throw new IllegalArgumentException("Unknown literal");
  }
}