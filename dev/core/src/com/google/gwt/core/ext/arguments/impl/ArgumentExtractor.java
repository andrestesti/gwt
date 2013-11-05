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
package com.google.gwt.core.ext.arguments.impl;

import com.google.gwt.core.ext.arguments.JArgument;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import java.io.DataInput;
import java.io.IOException;

/**
 * Extracts an argument list from a type.
 */
public class ArgumentExtractor {
  
  private ArgumentExtractor() {
  }

  public static JArgument[] getArguments(JClassType type) {
    GwtCreateRequest annotation = type.getAnnotation(GwtCreateRequest.class);
    if (annotation == null) {
      return new JArgument[0];
    }

    DataInput in = ByteStreams.newDataInput(annotation.bytes().getBytes());
    JArgument[] arguments = new JArgument[annotation.size()];

    for (int i = 0; i < arguments.length; i++) {
      try {
        arguments[i] = ArgumentSerializer.deserialize(in);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return arguments;
  }

  public static String getRequestTypeName(JClassType type) {
    GwtCreateRequest args = type.getAnnotation(GwtCreateRequest.class);
    if (args != null) {
      return args.typeName();
    }
    return type.getName();
  }

  public static boolean hasArguments(JClassType type) {
    return type.isAnnotationPresent(GwtCreateRequest.class);
  }
}
