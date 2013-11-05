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
package com.google.gwt.core.shared;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to define rebind methods.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
@Documented
public @interface Rebind {

  /**
   * Default class for type parameter.
   */
  final class ByParameter {
    private ByParameter() {
    }
  }

  /**
   * Request type to be generated. 
   * Default stands for a type in the parameter list.
   */
  Class<?> type() default ByParameter.class;

  /**
   * Annotation to define rebind type parameters.
   */
  @Target({ElementType.PARAMETER})
  @interface Type {
  }

  /**
   * Annotation to define rebind constructor parameters.
   */
  @Target({ElementType.PARAMETER})
  @interface Param {
  }
}
