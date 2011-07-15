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
package com.edmunds.common.configuration.api;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.testng.Assert.assertEquals;

@Test(groups = {"ConfigurationApi"})
public class EnvironmentPropertyFactoryTest {

    private EnvironmentPropertyFactory environmentPropertyFactory;
    private ConfigurationUtil configurationUtil;

    @BeforeClass
    public void initLogging() {
        BasicConfigurator.configure();
        // Don't output debug.
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    @BeforeMethod
    public void setup() {
        configurationUtil = createMock("configurationUtil", ConfigurationUtil.class);

        environmentPropertyFactory = new EnvironmentPropertyFactory(configurationUtil);
        environmentPropertyFactory.setLocal("localValue");
        environmentPropertyFactory.setManaged("managedValue");
    }

    @Test
    public void getPropertyTest() {
        expect(configurationUtil.replacePropertyTokens("localValue", "managedValue")).andReturn("propertyResultValue");
        replay(configurationUtil);

        assertEquals(environmentPropertyFactory.getProperty(), "propertyResultValue");
    }

    @Test
    public void parseValueTest() {
        expect(configurationUtil.replaceTokens("inputValue")).andReturn("parseResultValue");
        replay(configurationUtil);

        assertEquals(environmentPropertyFactory.parseValue("inputValue"), "parseResultValue");
    }

    @Test
    public void getObjectTest() {
        expect(configurationUtil.replacePropertyTokens("localValue", "managedValue")).andReturn("objectResultValue");
        replay(configurationUtil);

        assertEquals(environmentPropertyFactory.getObject(), "objectResultValue");
    }

    @Test
    public void getObjectTypeTest() {
        // Assert no calls to configurationUtil.
        replay(configurationUtil);

        assertEquals(environmentPropertyFactory.getObjectType(), String.class);
    }

    @Test
    public void isSingletonTest() {
        // Assert no calls to configurationUtil.
        replay(configurationUtil);

        assertEquals(environmentPropertyFactory.isSingleton(), true);
    }

    @AfterMethod
    public void cleanup() {
        verify(configurationUtil);
    }
}
