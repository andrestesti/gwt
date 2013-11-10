/*
 * Copyright 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.core.ext.arguments;

/**
 * Provides unique keys for a argument lists.
 */
public class JArguments {

  private static void appendKey(StringBuilder sb, JArgument argument) {
    if (argument instanceof JBooleanArgument) {
      JBooleanArgument cast = (JBooleanArgument) argument;
      sb.append('B');
      sb.append(cast.getValue() ? 1 : 0);
    } else if (argument instanceof JCharArgument) {
      JCharArgument cast = (JCharArgument) argument;
      sb.append('H');
      sb.append(Integer.toHexString(cast.getValue()));
    } else if (argument instanceof JClassArgument) {
      JClassArgument cast = (JClassArgument) argument;
      sb.append('C');
      sb.append(Integer.toHexString(cast.getClassName().hashCode()));
    } else if (argument instanceof JArrayArgument) {
      JArrayArgument cast = (JArrayArgument) argument;
      sb.append('A');
      appendKeys(sb, cast.getElements());
      sb.append('_');
    } else if (argument instanceof JDoubleArgument) {
      JDoubleArgument cast = (JDoubleArgument) argument;
      sb.append('D');
      sb.append(Long.toHexString(Double.doubleToRawLongBits(cast.getValue())));
    } else if (argument instanceof JEnumArgument) {
      JEnumArgument cast = (JEnumArgument) argument;
      sb.append('E');
      sb.append(Integer.toHexString(cast.getEnumClassName().hashCode()));
      sb.append('o');
      sb.append(Integer.toHexString(cast.getEnumOrdinal()));
    } else if (argument instanceof JFloatArgument) {
      JFloatArgument cast = (JFloatArgument) argument;
      sb.append('F');
      sb.append(Integer.toHexString(Float.floatToRawIntBits(cast.getValue())));
    } else if (argument instanceof JIntArgument) {
      JIntArgument cast = (JIntArgument) argument;
      sb.append('I');
      sb.append(Integer.toHexString(cast.getValue()));
    } else if (argument instanceof JLongArgument) {
      JLongArgument cast = (JLongArgument) argument;
      sb.append('L');
      sb.append(Long.toHexString(cast.getValue()));
    } else if (argument instanceof JNullArgument) {
      sb.append('N');
    } else if (argument instanceof JStringArgument) {
      JStringArgument cast = (JStringArgument) argument;
      sb.append('S');
      sb.append(Integer.toHexString(cast.getValue().hashCode()));
    } else if (argument instanceof JOpaqueArgument) {
      sb.append('O');
    }
  }

  private static void appendKeys(StringBuilder sb, JArgument[] arguments) {
    for (JArgument a : arguments) {
      appendKey(sb, a);
    }
  }

  /**
   * Calculates a unique key for the list of arguments.
   * 
   * @param arguments list of arguments.
   * @return a unique key.
   */
  public static String getKey(JArgument... arguments) {
    StringBuilder sb = new StringBuilder();
    appendKeys(sb, arguments);
    return sb.toString();
  }

  private JArguments() {
  }
}
