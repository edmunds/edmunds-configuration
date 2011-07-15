/*
 * Copyright 2011 Edmunds.com, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edmunds.common.configuration.dns;

import com.edmunds.autotest.AutoTestGetterSetter;
import org.testng.annotations.Test;

/**
 * Tests the setters and getters of all the beans.
 * <p/>
 * Copyright (C) 2008 Edmunds.com
 * <p/>
 * Date: Nov 13, 2008 11:27:03 AM
 *
 * @author egramond
 */
@Test(groups = {"GetterSetter"})
public class GetterSetterTest {

    @Test
    public void testAll() {
        new AutoTestGetterSetter(null, "com.edmunds.common.configuration.dns").validateAll();
    }
}
