/*
 * Copyright 2010 Google Inc.
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
package com.google.gwt.requestfactory.client.impl;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyListRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <span style="color:red">Experimental API: This class is still under rapid
 * development, and is very likely to be deleted. Use it at your own risk.
 * </span>
 * </p>
 * Abstract implementation of
 * {@link com.google.gwt.requestfactory.shared.RequestObject
 * RequestFactory.RequestObject} for requests that return lists of
 * {@link EntityProxy}.
 * 
 * @param <T> the type of entities returned
 * @param <R> this request type
 */
public abstract class //
    AbstractJsonProxyListRequest<T extends EntityProxy,
    R extends AbstractJsonProxyListRequest<T, R>>
    extends AbstractJsonProxyCollectionRequest<List<T>, T, R>
    implements ProxyListRequest<T> {
  protected final ProxySchema<?> schema;

  public AbstractJsonProxyListRequest(ProxySchema<? extends T> schema,
      RequestFactoryJsonImpl requestService) {
    super(schema, requestService);
    this.schema = schema;
  }

  protected List<T> createCollection() {
    return new ArrayList<T>();
  }
}