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
package com.google.gwt.core.ext;

import com.google.gwt.core.ext.arguments.JArgument;
import com.google.gwt.core.ext.arguments.impl.ArgumentExtractor;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * Generates source code for subclasses during deferred binding requests with argument lists.
 * Subclasses must be thread-safe.
 */
public abstract class ParameterizedGenerator extends Generator {

  @Override
  public final String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    String requestTypeName;
    JArgument[] arguments;
    JClassType type = context.getTypeOracle().findType(typeName);
    if (type != null && ArgumentExtractor.hasArguments(type)) {
      requestTypeName = ArgumentExtractor.getRequestTypeName(type);
      arguments = ArgumentExtractor.getArguments(type);
    } else {
      requestTypeName = typeName;
      arguments = new JArgument[0];
    }
    return generate(logger, context, requestTypeName, arguments);
  }

  /**
   * Generate a default constructible subclass of the requested type with an argument list.
   * The generator throws <code>UnableToCompleteException</code> if for any reason
   * it cannot provide a substitute class
   * 
   * @return the name of a subclass to substitute for the requested class, or
   *         return <code>null</code> to cause the requested type itself to be
   *         used
   * 
   */
  public abstract String generate(TreeLogger logger, GeneratorContext context, String typeName,
      JArgument[] arguments) throws UnableToCompleteException;
}
