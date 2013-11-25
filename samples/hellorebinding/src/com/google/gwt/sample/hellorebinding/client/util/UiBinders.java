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
import com.google.gwt.uibinder.client.UiBinder;

/**
 * Class with utility methods to bind user interfaces instances without 
 * subclassing {@link UiBinder}.
 */
public class UiBinders {
  
  private UiBinders() {    
  }  
  
  /**
   * Creates and returns the root object of the UI, and fills any fields of owner
   * tagged with {@link UiField}.
   *
   * @param rootType the type of the root object of the generated UI, typically a
   *        subclass of {@link com.google.gwt.dom.client.Element Element} or
   *        {@link com.google.gwt.user.client.ui.UIObject UiObject}
   * @param ownerType the type of the object that will own the generated UI
   * @param owner the object whose {@link com.google.gwt.uibinder.client.UiField UiField}
   *              needs will be filled
   */
  @Rebind(type = UiBinderCreator.class)
  public static <U, O> U createAndBindUi(@Rebind.Param final Class<U> rootType,
      @Rebind.Param final Class<O> ownerType, O owner) {
    UiBinderCreator<U, O> creator = GWT.create(UiBinderCreator.class, rootType, ownerType);
    UiBinder<U, O> uiBinder = creator.createUiBinder();
    return uiBinder.createAndBindUi(owner);
  }
  
  /**
   * Creates and returns the root object of the UI, and fills any fields of owner
   * tagged with {@link UiField}.
   *
   * @param rootType the type of the root object of the generated UI, typically a
   *        subclass of {@link com.google.gwt.dom.client.Element Element} or
   *        {@link com.google.gwt.user.client.ui.UIObject UiObject}
   * @param ownerType the type of the object that will own the generated UI
   * @param template the template file
   * @param owner the object whose {@literal @}UiField needs will be filled
   */
  @Rebind(type = UiBinderCreator.class)
  public static <U, O> U createAndBindUi(@Rebind.Param final Class<U> rootType,
      @Rebind.Param final Class<O> ownerType, @Rebind.Param final String template, O owner) {
    UiBinderCreator<U, O> creator = 
        GWT.create(UiBinderCreator.class, rootType, ownerType, template);
    UiBinder<U, O> uiBinder = creator.createUiBinder();
    return uiBinder.createAndBindUi(owner);
  }
}
