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
package com.google.gwt.sample.hellorebinding.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.ParameterizedGenerator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.arguments.JArgument;
import com.google.gwt.core.ext.arguments.JArguments;
import com.google.gwt.core.ext.arguments.JStringArgument;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.sample.hellorebinding.client.util.JsniSnippet;

import java.io.PrintWriter;

/**
 * Generates {@link JsniSnippet} implementations depending on {@link JArgument}s.
 */
public class JsniSnippetGenerator extends ParameterizedGenerator {

  private int argumentsCount(JClassType type) {
    for (JMethod m : type.getMethods()) {
      if (m.getName().equals("execute")) {
        return m.getParameters().length;
      }
    }
    return -1;
  }

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName,
      JArgument[] arguments) throws UnableToCompleteException {

    JClassType snippetType = context.getTypeOracle().findType(typeName);

    if (snippetType == null) {
      logger.log(TreeLogger.ERROR, "Rebinding type not found");
      throw new UnableToCompleteException();
    }

    if (arguments.length < 1) {
      logger.log(TreeLogger.ERROR, "Missing JSNI literal argument");
      throw new UnableToCompleteException();
    }

    JArgument arg0 = arguments[0];

    if (!(arg0 instanceof JStringArgument)) {
      logger.log(TreeLogger.ERROR, "JSNI literal argument must be a String literal");
      throw new UnableToCompleteException();
    }

    String argumentsKey = JArguments.getKey(arguments);

    String packageName = snippetType.getPackage().getName();
    String snippetTypeName = snippetType.getSimpleSourceName() + "_impl_" + argumentsKey;
    String qualifiedSnippetTypeName = packageName + "." + snippetTypeName;

    PrintWriter pw = context.tryCreate(logger, packageName, snippetTypeName);

    if (pw != null) {

      int argCount = argumentsCount(snippetType);

      if (argCount < 0) {
        logger.log(TreeLogger.ERROR, "execute() method not found");
        throw new UnableToCompleteException();
      }

      String jsniTemplate = ((JStringArgument) arg0).getValue();
      String[] parts = jsniTemplate.split("#", -1);

      if (parts.length != (argCount + 1)) {
        logger.log(TreeLogger.ERROR, "wrong number of snippet arguments");
        throw new UnableToCompleteException();
      }

      pw.printf("package %s;\n\n", packageName);
      pw.printf("public class %s implements %s {\n\n", snippetTypeName, snippetType
          .getSimpleSourceName());
      pw.printf("  public %s(String jsniLiteral) {}\n", snippetTypeName);
      pw.println();

      pw.print("  @Override public native <T> T execute(");

      boolean hasSeparator = false;

      for (int i = 0; i < argCount; i++) {
        if (hasSeparator) {
          pw.print(", ");
        } else {
          hasSeparator = true;
        }
        pw.printf("Object a%d", i);
      }

      pw.println(") /*-{");
      pw.print("    return ");

      if (argCount == 0) {
        pw.print(jsniTemplate);
      } else {
        for (int i = 0; i < argCount; i++) {
          pw.printf("%sa%d", parts[i], i);
        }
        pw.print(parts[argCount]);
      }
      pw.println(";");

      pw.println("  }-*/;");
      pw.println("}");
      context.commit(logger, pw);
    }

    return qualifiedSnippetTypeName;
  }
}
