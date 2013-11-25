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
package com.google.gwt.core.ext.arguments;

/**
 * Compile time enum argument.
 */
public class JEnumArgument extends JAtomicArgument<JEnumArgument.Value> {
  
  /**
   * Enum value pair.
   */
  public static class Value {

    private final int enumOrdinal;

    private final String enumClassName;

    Value(int enumOrdinal, String enumClassName) {
      this.enumOrdinal = enumOrdinal;
      this.enumClassName = enumClassName;
    }

    public String getEnumClassName() {
      return enumClassName;
    }

    public int getEnumOrdinal() {
      return enumOrdinal;
    }    
  }
  
  JEnumArgument(int enumOrdinal, String enumClassName) {
    super(new Value(enumOrdinal, enumClassName));
  }

  public static JEnumArgument valueOf(String enumClassName, int enumOrdinal) {
    return new JEnumArgument(enumOrdinal, enumClassName);
  }
}
