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
package com.google.gwt.dev.jjs.ast;

import java.io.Serializable;
import java.util.List;

/**
 * Codegen atributes for methods.
 */
public class RebindSignature implements Serializable {

  private JMethod method;
  private String typeName;
  private int typeParamIndex;
  private List<Integer> ctorParamIndices;
  
  private JParameter factoryParam = null;

  public RebindSignature(JMethod method, String typeName, int typeParamIndex,
      List<Integer> ctorParamIndices) {
    this.method = method;
    this.typeName = typeName;
    this.typeParamIndex = typeParamIndex;
    this.ctorParamIndices = ctorParamIndices;
  }

  public List<Integer> getCtorParamIndices() {
    return ctorParamIndices;
  }

  public JParameter getFactoryParam() {
    return factoryParam;
  }

  public JMethod getMethod() {
    return method;
  }

  public String getTypeName() {
    return typeName;
  }

  public JParameter getTypeParam() {
    assert hasTypeParam();
    return method.getParams().get(typeParamIndex);
  }

  public int getTypeParamIndex() {
    return typeParamIndex;
  }

  /**
   * Determines if there are multiple parameters for the type.
   */
  public boolean hasMultipleTypeParam() {
    return typeParamIndex < -1;
  }

  /**
   * Determines if the type is a parameter.
   */
  public boolean hasTypeParam() {
    return typeParamIndex >= 0;
  }

  public void setCtorParamsIndexes(List<Integer> ctorParamsIndexes) {
    this.ctorParamIndices = ctorParamsIndexes;
  }

  public void setFactoryParam(JParameter factoryParam) {
    this.factoryParam = factoryParam;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public void setTypeParamIndex(int typeParamIndex) {
    this.typeParamIndex = typeParamIndex;
  }
}
