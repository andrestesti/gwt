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
package com.google.gwt.sample.dynatablerf.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.StringEditor;
import com.google.gwt.sample.dynatablerf.shared.PersonProxy;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Edits People.
 */
public class PersonEditor extends Composite implements Editor<PersonProxy> {
  interface Binder extends UiBinder<Widget, PersonEditor> {
  }

  @UiField
  AddressEditor address;

  @UiField
  TextBox name;

  @UiField
  TextBox description;

  @UiField
  TextArea note;

  // TODO: Allow UiBinder to configure this
  final StringEditor nameEditor;
  final StringEditor descriptionEditor;
  final StringEditor noteEditor;

  public PersonEditor() {
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    nameEditor = StringEditor.of(name);
    descriptionEditor = StringEditor.of(description);
    noteEditor = StringEditor.of(note);
  }

  public void focus() {
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        name.setFocus(true);
      }
    });
  }
}