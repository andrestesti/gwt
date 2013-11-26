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
package com.google.gwt.dev.jjs.impl;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.javac.testing.impl.MockJavaResource;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JProgram;

/**
 * Tests AST of rebind methods.
 */
public class RebindMethodAstTest extends JJSTestBase {

  private JProgram program;

  @Override
  public void setUp() {
    sourceOracle.addOrReplace(new MockJavaResource("test.Creator") {
      @Override
      public CharSequence getContent() {
        StringBuffer code = new StringBuffer();
        code.append("package test;\n");
        code.append("import com.google.gwt.core.shared.Rebind;\n");
        code.append("public interface Creator {\n");
        code.append("  @Rebind\n");
        code.append("  <T> T create(@Rebind.Type Class<T> type);\n");
        code.append("}\n");
        return code;
      }
    });
    sourceOracle.addOrReplace(new MockJavaResource("test.CreatorImpl") {
      @Override
      public CharSequence getContent() {
        StringBuffer code = new StringBuffer();
        code.append("package test;\n");
        code.append("import com.google.gwt.core.shared.GWT;\n");
        code.append("import com.google.gwt.core.shared.Rebind;\n");
        code.append("public class CreatorImpl implements Creator {\n");
        code.append("  @Rebind\n");
        code.append("  @Override public <T> T create(@Rebind.Type final Class<T> type) {\n");
        
        // We can't invoke GWT.create() in this test suite
        code.append("    return null;\n");
        code.append("  }\n");
        code.append("}\n");
        return code;
      }
    });

    try {
      program =
          compileSnippet("test.CreatorImpl", "return new CreatorImpl();");
    } catch (UnableToCompleteException e) {
      throw new RuntimeException(e);
    }
  }

  public void testRebindSignature() {    
    JMethod rebindMethod = findQualifiedMethod(program, "test.CreatorImpl.create");
    assertNotNull(rebindMethod.getSignature());
  }
}
