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
package com.google.gwt.sample.hellorebinding.client.util;

/**
 * Runtime implementation for {@link StringFormatter}. This implementation is bound when the 
 * format argument of {@link Strings#format(String, String...)} is not a {@link String} literal.
 */
public class RuntimeStringFormatter implements StringFormatter {

  private final String format;

  private final String[] arguments;

  public RuntimeStringFormatter(String format, String[] arguments) {
    this.format = format;
    this.arguments = arguments;
  }

  @Override
  public String format() {
    String[] parts = format.split("%s", -1);
    if (parts.length < 2) {
      return format;
    }
    StringBuilder sb = new StringBuilder();
    int argTop = arguments.length < parts.length ? arguments.length : parts.length - 1;
    for (int i = 0; i < parts.length; i++) {
      sb.append(parts[i]);
      if (argTop > i) {
        sb.append(arguments[i]);
      }
    }
    return sb.toString();
  }
}
