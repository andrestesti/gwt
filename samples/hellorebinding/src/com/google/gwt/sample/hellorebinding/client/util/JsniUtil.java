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
 * Class with utility methods to execute JSNI code from a {@link String} literal.
 */
public class JsniUtil {
  
  private JsniUtil() {    
  }
  
  /**
   * Executes JSNI code from a {@link String} literal.
   * This method doesn't allows snippet arguments.
   * 
   * @param jsniLiteral JSNI code
   *
   * @return the result of the JSNI execution
   */
  @Rebind(type = JsniSnippet0.class)
  public static <T> T jsni(@Rebind.Param final String jsniLiteral) {
    JsniSnippet0 snippet = GWT.create(JsniSnippet0.class, jsniLiteral);
    return snippet.execute();
  }
  
  /**
   * Executes JSNI code from a {@link String} literal with one argument.
   * 
   * @param jsniLiteral JSNI code
   * @param a the argument
   *
   * @return the result of the JSNI execution
   */
  @Rebind(type = JsniSnippet1.class)
  public static <T> T jsni(@Rebind.Param final String jsniLiteral, Object a) {
    JsniSnippet1 snippet = GWT.create(JsniSnippet1.class, jsniLiteral);
    return snippet.execute(a);
  }
  
  /**
   * Executes JSNI code from a {@link String} literal with 2 arguments.
   * 
   * @param jsniLiteral JSNI code
   * @param a0 the 1st argument
   * @param a1 the 2nd argument
   * 
   * @return the result of the JSNI execution
   */
  @Rebind(type = JsniSnippet2.class)
  public static <T> T jsni(@Rebind.Param final String jsniLiteral, Object a0, Object a1) {
    JsniSnippet2 snippet = GWT.create(JsniSnippet2.class, jsniLiteral);
    return snippet.execute(a0, a1);
  }
  
  /**
   * Executes JSNI code from a {@link String} literal with 3 arguments.
   * 
   * @param jsniLiteral JSNI code
   * @param a0 the 1st argument
   * @param a1 the 2nd argument
   * @param a2 the 3rd argument
   *
   * @return the result of the JSNI execution
   */
  @Rebind(type = JsniSnippet3.class)
  public static <T> T jsni(@Rebind.Param final String jsniLiteral, Object a0, Object a1, 
      Object a2) {
    JsniSnippet3 snippet = GWT.create(JsniSnippet3.class, jsniLiteral);
    return snippet.execute(a0, a1, a2);
  }
}
