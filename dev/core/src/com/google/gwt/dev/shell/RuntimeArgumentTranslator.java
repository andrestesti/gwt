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
package com.google.gwt.dev.shell;

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
import com.google.gwt.core.ext.arguments.JNullArgument;
import com.google.gwt.core.ext.arguments.JStringArgument;
import com.google.gwt.core.ext.arguments.JVariableArgument;

/**
 * Translates a runtime argument to a compile time argument.
 */
class RuntimeArgumentTranslator {

  public static JArgument translate(Object argument) {
    if (argument == null) {
      return JNullArgument.value();
    }
    if (argument instanceof Boolean) {
      return JBooleanArgument.valueOf((Boolean) argument);
    }
    if (argument instanceof Character) {
      return JCharArgument.valueOf((Character) argument);
    }
    if (argument instanceof Class) {
      return JClassArgument.valueOf(((Class<?>) argument).getCanonicalName());
    }
    if (argument instanceof Double) {
      return JDoubleArgument.valueOf((Double) argument);
    }
    if (argument instanceof Enum) {
      Enum<?> enumValue = (Enum<?>) argument;
      return JEnumArgument.valueOf(enumValue.getDeclaringClass().getCanonicalName(), enumValue
          .ordinal());
    }
    if (argument instanceof Float) {
      return JFloatArgument.valueOf((Float) argument);
    }
    if (argument instanceof Integer) {
      return JIntArgument.valueOf((Integer) argument);
    }
    if (argument instanceof Long) {
      return JLongArgument.valueOf((Long) argument);
    }
    if (argument instanceof String) {
      return JStringArgument.valueOf((String) argument);
    }
    if (argument instanceof Object[]) {
      return JArrayArgument.valueOf(translate((Object[]) argument));
    }
    if (argument instanceof boolean[]) {
      boolean[] array = (boolean[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JBooleanArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    if (argument instanceof byte[]) {
      byte[] array = (byte[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JIntArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    if (argument instanceof char[]) {
      char[] array = (char[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JCharArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    if (argument instanceof double[]) {
      double[] array = (double[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JDoubleArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    if (argument instanceof float[]) {
      float[] array = (float[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JFloatArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    if (argument instanceof long[]) {
      long[] array = (long[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JLongArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    if (argument instanceof short[]) {
      short[] array = (short[]) argument;
      JArgument[] elements = new JArgument[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = JIntArgument.valueOf(array[i]);
      }
      return JArrayArgument.valueOf(elements);
    }
    return JVariableArgument.value();
  }

  public static JArgument[] translate(Object[] arguments) {
    int last = arguments.length - 1;
    JArgument[] translation = new JArgument[arguments.length];
    for (int i = 0; i < last; i++) {
      translation[i] = translate(arguments[i]);
    }
    if (last >= 0) {
      Object lastArgument = arguments[last];
      // we assume a vararg by default
      if (lastArgument instanceof Object[]) {
        Object[] listArgument = (Object[]) lastArgument;
        JArgument[] elements = new JArgument[listArgument.length];
        for (int i = 0; i < listArgument.length; i++) {
          elements[i] = translate(listArgument[i]);
        }
        translation[last] = JArrayArgument.valueOf(elements);
      } else {
        translation[last] = translate(arguments[last]);
      }
    }
    return translation;
  }
}
