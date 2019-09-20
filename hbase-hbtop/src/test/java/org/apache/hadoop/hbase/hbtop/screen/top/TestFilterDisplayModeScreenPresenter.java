/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.hbtop.screen.top;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.hbtop.RecordFilter;
import org.apache.hadoop.hbase.hbtop.field.Field;
import org.apache.hadoop.hbase.hbtop.field.FieldInfo;
import org.apache.hadoop.hbase.hbtop.mode.Mode;
import org.apache.hadoop.hbase.hbtop.screen.ScreenView;
import org.apache.hadoop.hbase.testclassification.SmallTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@Category(SmallTests.class)
@RunWith(MockitoJUnitRunner.class)
public class TestFilterDisplayModeScreenPresenter {

  @Mock
  private FilterDisplayModeScreenView filterDisplayModeScreenView;

  @Mock
  private TopScreenView topScreenView;

  private FilterDisplayModeScreenPresenter filterDisplayModeScreenPresenter;

  @Before
  public void setup() {
    List<Field> fields = new ArrayList<>();
    for (FieldInfo fieldInfo : Mode.REGION.getFieldInfos()) {
      fields.add(fieldInfo.getField());
    }

    List<RecordFilter>  filters = new ArrayList<>();
    filters.add(RecordFilter.parse("NAMESPACE==namespace", fields, true));
    filters.add(RecordFilter.parse("TABLE==table", fields, true));

    filterDisplayModeScreenPresenter = new FilterDisplayModeScreenPresenter(
      filterDisplayModeScreenView, filters, topScreenView);
  }

  @Test
  public void testInit() {
    filterDisplayModeScreenPresenter.init();

    verify(filterDisplayModeScreenView).showFilters(argThat(
      new ArgumentMatcher<List<RecordFilter>>() {
        @Override
        @SuppressWarnings("unchecked")
        public boolean matches(Object argument) {
          List<RecordFilter> filters = (List<RecordFilter>) argument;
          return filters.size() == 2
            && filters.get(0).toString().equals("NAMESPACE==namespace")
            && filters.get(1).toString().equals("TABLE==table");
        }
      }));
  }

  @Test
  public void testReturnToTopScreen() {
    assertThat(filterDisplayModeScreenPresenter.returnToNextScreen(),
      is((ScreenView) topScreenView));
  }
}
