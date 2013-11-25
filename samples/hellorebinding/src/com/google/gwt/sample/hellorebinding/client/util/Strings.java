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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.core.shared.Rebind;

/**
 * Class with an utility method to format {@link String}s in compile time while possible.
 */
public class Strings {
  
  private Strings() {    
  }
  
  /**
   * Formats a {@link String} in compile time if the format argument is a {@link String} literal. 
   * Otherwise the {@link String} if formatted at runtime.
   * 
   * @param format A simplified format {@link String} that recognizes only %s patterns
   * @param args Arguments referenced by the format specifiers in the format {@link String}
   *
   * @return a formatted {@link String}
   */
  @Rebind(type = StringFormatter.class)
  public static String format(@Rebind.Param final String format, 
      @Rebind.Param final String... args) {
    StringFormatter formatter = GWT.create(StringFormatter.class, format, args);
    return formatter.format();
  }
}
