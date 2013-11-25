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
import com.google.gwt.core.ext.arguments.JClassArgument;
import com.google.gwt.core.ext.arguments.JStringArgument;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.sample.hellorebinding.client.util.UiBinderCreator;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;

import java.io.PrintWriter;

/**
 * Generates {@link UiBinderCreator} implementations depending on {@link JArgument}s.
 */
public class UiBinderCreatorGenerator extends ParameterizedGenerator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName,
      JArgument[] arguments) throws UnableToCompleteException {

    TypeOracle oracle = context.getTypeOracle();

    if (arguments.length < 1) {
      logger.log(TreeLogger.ERROR, "Missing root type argument");
      throw new UnableToCompleteException();
    }

    if (!(arguments[0] instanceof JClassArgument)) {
      logger.log(TreeLogger.ERROR, "Root type argument must be a Class literal");
      throw new UnableToCompleteException();
    }

    JClassArgument rootTypeArg = (JClassArgument) arguments[0];
    JClassType rootType = oracle.findType(rootTypeArg.getValue());

    if (arguments.length < 2) {
      logger.log(TreeLogger.ERROR, "Missing owner type argument");
      throw new UnableToCompleteException();
    }

    if (!(arguments[1] instanceof JClassArgument)) {
      logger.log(TreeLogger.ERROR, "Root type argument must be a Class literal");
      throw new UnableToCompleteException();
    }

    JClassArgument ownerTypeArg = (JClassArgument) arguments[1];
    JClassType ownerType = oracle.findType(ownerTypeArg.getValue());

    String template;
    if (arguments.length > 2) {
      if (!(arguments[2] instanceof JStringArgument)) {
        logger.log(TreeLogger.ERROR, "Template argument must be a String literal");
        throw new UnableToCompleteException();
      }
      JStringArgument templateArg = (JStringArgument) arguments[2];
      template = templateArg.getValue();
    } else {
      template = ownerType.getSimpleSourceName() + ".ui.xml";
    }

    if (arguments.length > 3) {
      logger.log(TreeLogger.ERROR, "Too much arguments");
      throw new UnableToCompleteException();
    }

    String argumentsKey = JArguments.getKey(arguments);

    String packageName = ownerType.getPackage().getName();
    String creatorTypeName =
        ownerType.getSimpleSourceName() + "_UiBinderCreatorImpl_" + argumentsKey;
    String qualifiedCreatorTypeName = packageName + "." + creatorTypeName;

    String rootTypeName = rootType.getQualifiedSourceName();
    String ownerTypeName = ownerType.getQualifiedSourceName();

    PrintWriter pw = context.tryCreate(logger, packageName, creatorTypeName);
    if (pw != null) {

      pw.printf("package %s;\n", packageName);
      pw.println();

      pw.printf("import %s;\n", GWT.class.getCanonicalName());
      pw.printf("import %s;\n", UiBinder.class.getCanonicalName());
      pw.printf("import %s;\n", UiTemplate.class.getCanonicalName());
      pw.printf("import %s;\n", UiBinderCreator.class.getCanonicalName());

      pw.println();
      pw.printf("public class %s implements UiBinderCreator<%s, %s> {\n", creatorTypeName,
          rootTypeName, ownerTypeName);
      pw.println();

      pw.printf("  @UiTemplate(\"%s\")\n", escape(template));
      pw.printf("  public static interface Internal extends UiBinder<%s, %s> {}\n", rootTypeName,
          ownerTypeName);
      pw.println();

      // Add constructor
      if (arguments.length > 2) {
        pw.printf("  public %s(Class<%s> rootType, Class<%s> ownerType, String template) {}\n",
            creatorTypeName, rootTypeName, ownerTypeName);
      } else {
        pw.printf("  public %s(Class<%s> rootType, Class<%s> ownerType) {}\n", creatorTypeName,
            rootTypeName, ownerTypeName);
      }
      pw.println();

      // Implement create() method
      pw.printf("  @Override public UiBinder<%s, %s> createUiBinder() {\n", rootTypeName,
          ownerTypeName);
      pw.println("    return GWT.create(Internal.class);");
      pw.println("  }");
      pw.println("}");

      context.commit(logger, pw);
    }

    return qualifiedCreatorTypeName;
  }
}
