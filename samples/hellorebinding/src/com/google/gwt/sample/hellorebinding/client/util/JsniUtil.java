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
 * Class with an utility method to execute JSNI code from a {@link String} literal.
 */
public class JsniUtil {
  
  private JsniUtil() {    
  }
  
  /**
   * Executes JSNI code from a {@link String} literal.
   * 
   * @param jsniLiteral JSNI code
   * @param args arguments for the JSNI snippet
   *
   * @return the result of the JSNI execution
   */
  @Rebind(type = JsniSnippet.class)
  public static <T> T jsni(@Rebind.Param final String jsniLiteral, Object...args) {
    JsniSnippet snippet = GWT.create(JsniSnippet.class, jsniLiteral);
    return snippet.execute(args);
  }
}
