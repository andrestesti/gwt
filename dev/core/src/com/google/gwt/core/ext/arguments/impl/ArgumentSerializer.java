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
package com.google.gwt.core.ext.arguments.impl;

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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Static methods to de/serialize generator argument lists.
 */
public class ArgumentSerializer {

  public static final int ARRAY = 0;
  public static final int BOOLEAN = 1;
  public static final int CHAR = 2;
  public static final int CLASS = 3;
  public static final int DOUBLE = 4;
  public static final int ENUM = 5;
  public static final int FLOAT = 6;
  public static final int INT = 7;
  public static final int LONG = 8;
  public static final int NULL = 9;
  public static final int STRING = 10;
  public static final int VARIABLE = 11;

  public static void serialize(DataOutput out, JArgument argument) throws IOException {
    if (argument instanceof JArrayArgument) {
      out.writeByte(ARRAY);
      JArrayArgument value = (JArrayArgument) argument;
      JArgument[] elements = value.getElements();
      out.writeInt(elements.length);
      for (JArgument e : elements) {
        serialize(out, e);
      }
    } else if (argument instanceof JBooleanArgument) {
      out.writeByte(BOOLEAN);
      JBooleanArgument value = (JBooleanArgument) argument;
      out.writeBoolean(value.getValue());
    } else if (argument instanceof JCharArgument) {
      out.writeByte(CHAR);
      JCharArgument value = (JCharArgument) argument;
      out.writeChar(value.getValue());
    } else if (argument instanceof JClassArgument) {
      out.writeByte(CLASS);
      JClassArgument value = (JClassArgument) argument;
      out.writeUTF(value.getClassName());
    } else if (argument instanceof JDoubleArgument) {
      out.writeByte(DOUBLE);
      JDoubleArgument value = (JDoubleArgument) argument;
      out.writeDouble(value.getValue());
    } else if (argument instanceof JEnumArgument) {
      out.writeByte(ENUM);
      JEnumArgument value = (JEnumArgument) argument;
      out.writeUTF(value.getEnumClassName());
      out.writeInt(value.getEnumOrdinal());
    } else if (argument instanceof JFloatArgument) {
      out.writeByte(FLOAT);
      JFloatArgument value = (JFloatArgument) argument;
      out.writeFloat(value.getValue());
    } else if (argument instanceof JIntArgument) {
      out.writeByte(INT);
      JIntArgument value = (JIntArgument) argument;
      out.writeInt(value.getValue());
    } else if (argument instanceof JLongArgument) {
      out.writeByte(LONG);
      JLongArgument value = (JLongArgument) argument;
      out.writeLong(value.getValue());
    } else if (argument instanceof JNullArgument) {
      out.writeByte(NULL);
    } else if (argument instanceof JStringArgument) {
      out.writeByte(STRING);
      JStringArgument value = (JStringArgument) argument;
      out.writeUTF(value.getValue());
    } else if (argument instanceof JVariableArgument) {
      out.writeByte(VARIABLE);
    } else {
      throw new RuntimeException("Unknown argument type");
    }
  }

  public static JArgument deserialize(DataInput in) throws IOException {

    byte type = in.readByte();
    switch (type) {
      case ARRAY:
        JArgument[] elements = new JArgument[in.readInt()];
        for (int i = 0; i < elements.length; i++) {
          elements[i] = deserialize(in);
        }
        return JArrayArgument.valueOf(elements);
      case BOOLEAN:
        byte value = in.readByte();
        if (value > 1) {
          throw new RuntimeException("Bad boolean value: " + value);
        }
        return JBooleanArgument.valueOf(in.readBoolean());
      case CHAR:
        return JCharArgument.valueOf(in.readChar());
      case CLASS:
        return JClassArgument.valueOf(in.readUTF());
      case DOUBLE:
        return JDoubleArgument.valueOf(in.readDouble());
      case ENUM:
        return JEnumArgument.valueOf(in.readUTF(), in.readInt());
      case FLOAT:
        return JFloatArgument.valueOf(in.readFloat());
      case INT:
        return JIntArgument.valueOf(in.readInt());
      case LONG:
        return JLongArgument.valueOf(in.readLong());
      case NULL:
        return JNullArgument.value();
      case VARIABLE:
        return JVariableArgument.value();
      case STRING:
        return JStringArgument.valueOf(in.readUTF());
    }
    throw new RuntimeException("Unknown type");
  }
}
