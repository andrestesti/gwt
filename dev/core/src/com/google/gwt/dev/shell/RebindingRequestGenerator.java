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
package com.google.gwt.dev.shell;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.arguments.JArgument;
import com.google.gwt.core.ext.arguments.JArguments;
import com.google.gwt.core.ext.arguments.impl.ArgumentSerializer;
import com.google.gwt.core.ext.arguments.impl.GwtCreateRequest;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.javac.StandardGeneratorContext;
import com.google.gwt.thirdparty.guava.common.io.ByteArrayDataOutput;
import com.google.gwt.thirdparty.guava.common.io.ByteStreams;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generates an annotated type with arguments for generators.
 */
public class GwtCreateRequestGenerator {

  public static void generate(TreeLogger logger, StandardGeneratorContext context, String typeName,
      String requestTypeName, JArgument[] args) throws UnableToCompleteException {
    TypeOracle oracle = context.getTypeOracle();
    if (typeName.equals(requestTypeName)) {
      return;
    }
    JClassType type = oracle.findType(typeName);
    String packageName = type.getPackage().getName();
    String requestTypeSimpleName = requestTypeName.replaceFirst(packageName + ".", "");

    PrintWriter pw = context.tryCreate(logger, packageName, requestTypeSimpleName);

    if (pw != null) {

      ByteArrayDataOutput out = ByteStreams.newDataOutput();

      for (JArgument a : args) {
        try {
          ArgumentSerializer.serialize(out, a);
        } catch (IOException e) {
          logger.log(Type.ERROR, "Unable to write argument", e);
          throw new UnableToCompleteException();
        }
      }

      byte[] bytes = out.toByteArray();

      pw.println("package " + packageName + ";");
      pw.println();
      pw.printf("@%s(\n", GwtCreateRequest.class.getCanonicalName());
      pw.printf("  typeName = \"%s\",\n", Generator.escape(typeName));
      pw.printf("  size = %d,\n", args.length);
      pw.print("  bytes = { ");
      boolean hasSeparator = false;
      for (byte b : bytes) {
        if (hasSeparator) {
          pw.print(", ");
        } else {
          hasSeparator = true;
        }
        pw.print(b);
      }
      pw.println(" }");      
      pw.println(")");
      pw.println("final class " + requestTypeSimpleName + " {");
      pw.println("  private " + requestTypeSimpleName + "() {}");
      pw.println("}");

      context.commit(logger, pw);
      context.finish(logger);
    }
  }

  public static String getRequestTypeName(String typeName, JArgument[] args) {
    if (args.length > 0) {
      return typeName + "_gwtcreaterequest_" + JArguments.getKey(args);
    }
    return typeName;
  }
}
