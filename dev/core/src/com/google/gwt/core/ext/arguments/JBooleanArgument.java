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
 * Compile time boolean argument.
 */
public class JBooleanArgument extends JConstantArgument {

  private static final JBooleanArgument FALSE = new JBooleanArgument(false);
  private static final JBooleanArgument TRUE = new JBooleanArgument(true);

  public static JBooleanArgument valueOf(boolean value) {
    return value ? TRUE : FALSE;
  }

  private final boolean value;

  private JBooleanArgument(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }
}