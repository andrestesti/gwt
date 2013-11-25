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
import com.google.gwt.core.ext.arguments.JArrayArgument;
import com.google.gwt.core.ext.arguments.JNullArgument;
import com.google.gwt.core.ext.arguments.JOpaqueArgument;
import com.google.gwt.core.ext.arguments.JStringArgument;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.sample.hellorebinding.client.util.RuntimeStringFormatter;
import com.google.gwt.sample.hellorebinding.client.util.StringFormatter;

import java.io.PrintWriter;

/**
 * Generates {@link StringFormatter} implementations depending on {@link JArgument}s.
 */
public class StringFormatterGenerator extends ParameterizedGenerator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName,
      JArgument[] arguments) throws UnableToCompleteException {

    if (arguments.length < 1) {
      logger.log(TreeLogger.ERROR, "Missing format argument");
      throw new UnableToCompleteException();
    }

    JArgument arg0 = arguments[0];
    
    if (!((arg0 instanceof JOpaqueArgument) || (arg0 instanceof JStringArgument))) {
      logger.log(TreeLogger.ERROR, "Format argument must be a String literal or reference");
      throw new UnableToCompleteException();
    }

    if (arguments.length < 2) {
      logger.log(TreeLogger.ERROR, "Missing argument list");
      throw new UnableToCompleteException();
    }

    JArgument arg1 = arguments[1];

    if (!((arg1 instanceof JOpaqueArgument) || (arg1 instanceof JArrayArgument))) {
      logger.log(TreeLogger.ERROR, "Argument list must be a String array literal or reference");
      throw new UnableToCompleteException();
    }

    // If the format is opaque, it needs a runtime implementation.
    if (arg0 instanceof JOpaqueArgument) {
      return RuntimeStringFormatter.class.getCanonicalName();
    }

    // If the argument list is opaque, it needs a runtime implementation.
    if (arg1 instanceof JOpaqueArgument) {
      return RuntimeStringFormatter.class.getCanonicalName();
    }

    String argumentsKey = JArguments.getKey(arguments);

    String packageName = StringFormatter.class.getPackage().getName();
    String formatterTypeName = StringFormatter.class.getSimpleName() + "_impl_" + argumentsKey;
    String qualifiedFormatterTypeName = packageName + "." + formatterTypeName;

    PrintWriter pw = context.tryCreate(logger, packageName, formatterTypeName);

    if (pw != null) {
      pw.printf("package %s;\n\n", packageName);
      pw.printf("import %s;\n\n", GWT.class.getCanonicalName());
      pw.printf("public class %s implements StringFormatter {\n\n", formatterTypeName);
      
      pw.println("  private final String[] arguments;");
      pw.println();
      
      pw.printf("  public %s(String format, String[] arguments) {\n", formatterTypeName);
      pw.println("    this.arguments = arguments;");
      pw.println("  }");      
      pw.println();

      pw.printf("  @Override public String format() {\n");

      String format = ((JStringArgument) arg0).getValue();
      JArgument[] args = ((JArrayArgument) arg1).getElements();
      String[] parts = format.split("%s", -1);

      if (parts.length < 2) {
        pw.printf("    return \"%s\";\n", escape(parts[0]));
      } else {
        pw.println("    StringBuilder sb = new StringBuilder();");
        int argTop = args.length < parts.length ? args.length : parts.length - 1;
        for (int i = 0; i < parts.length; i++) {
          pw.printf("    sb.append(\"%s\");\n", escape(parts[i]));
          if (argTop > i) {
            JArgument a = args[i];
            if (a instanceof JOpaqueArgument) {
              pw.printf("    sb.append(this.arguments[%d]);\n", i);
            } else if (a instanceof JStringArgument) {
              JStringArgument s = (JStringArgument) a;
              pw.printf("    sb.append(\"%s\");\n", escape(s.getValue()));
            } else if (a instanceof JNullArgument) {
              pw.printf("    sb.append(\"null\");\n");
            } else {
              logger.log(TreeLogger.ERROR, "Argument must be of type String");
              throw new UnableToCompleteException();
            }
          }
        }
        pw.printf("    return sb.toString();\n");
      }
      pw.println("  }");
      pw.println("}");
      context.commit(logger, pw);
    }

    return qualifiedFormatterTypeName;
  }
}
