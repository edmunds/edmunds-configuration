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

import com.edmunds.common.configuration.api.EnvironmentConfiguration;
import com.edmunds.common.configuration.api.EnvironmentConnection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

@Test(groups = {"DNSConfiguration"})
public class DNSConfigurationFactoryTest {

    private ConfigurationEntryReader configurationEntryReader;
    private DNSConfigurationFactory configurationFactory;

    @BeforeClass
    public void initLogging() {
        BasicConfigurator.configure();
        // Don't output debug.
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    @BeforeMethod
    public void setup() {
        configurationEntryReader = createMock("configurationEntryReader", ConfigurationEntryReader.class);
        this.configurationFactory = new DNSConfigurationFactory(configurationEntryReader);
    }

    @Test
    public void defaultConstructorTest() {
        // Don't need the mock here
        configurationEntryReader = null;

        // Just check the default constructor doesn't throw.
        new DNSConfigurationFactory();
    }

    @Test
    public void getConfigurationTest() {
        final EnvironmentConfiguration configuration = createMock("configuration", EnvironmentConfiguration.class);

        replay(configuration);
        replay(configurationEntryReader);

        configurationFactory.setEnvironmentConfiguration(configuration);

        assertSame(configurationFactory.getEnvironmentConfiguration(), configuration);

        verify(configuration);
    }

    @Test
    public void afterPropertiesSetTestFull() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, "DEV-EPE3", "dev-epe3", "DEV-EPE3", "lax1", "insideline");
        TestUtils.expectLogicalEnvironment(configurationEntryReader, "dev-epe3", "a");
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();
        TestUtils.assertConfiguration(configuration, false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        TestUtils.assertLogicalEnvironment(configuration, "dev-epe3", "a");

        testConfigurationUtil("DEV-EPE3", "http://dev-epe3-www.insideline.com/", "[PRE]DEV-EPE3[POST]",
            "[PRE]dev-epe3[POST]",
            "[PRE]a[POST]");
    }

    @Test
    public void afterPropertiesSetTestInvalidLegacy() throws Exception {
        TestUtils.expectInvalidEnvironment(configurationEntryReader, "DEV-EPE3", null);
        replay(configurationEntryReader);

        try {
            configurationFactory.afterPropertiesSet();
            fail("Expected IllegalStateException");
        } catch(IllegalStateException e) {
            // No op
        }
    }

    @Test
    public void afterPropertiesSetTestLegacy() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, "DEV-EPE3", null, "DEV-EPE3", null, null);
        TestUtils.expectLogicalEnvironment(configurationEntryReader, "dev-epe3", "a");
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();

        // Legacy mode returns site edmunds
        TestUtils.assertConfiguration(configuration, false, "dev-epe3", "lax1", "edmunds", "dev-epe3-");
        TestUtils.assertLogicalEnvironment(configuration, "dev-epe3", "a");

        testConfigurationUtil("DEV-EPE3", "http://dev-epe3-www.insideline.com/", "[PRE]DEV-EPE3[POST]",
            "[PRE]dev-epe3[POST]",
            "[PRE]a[POST]");
    }

    @Test
    public void afterPropertiesSetTestLegacyProd() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, "LAX1-PROD", null, "PROD", null, null);
        TestUtils.expectLogicalEnvironment(configurationEntryReader, "prod", "a");
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();

        // Legacy mode returns site edmunds
        TestUtils.assertConfiguration(configuration, false, "prod", "lax1", "edmunds", "");
        TestUtils.assertLogicalEnvironment(configuration, "prod", "a");

        testConfigurationUtil("LAX1-PROD", "http://www.insideline.com/", "[PRE]LAX1-PROD[POST]", "[PRE]prod[POST]",
            "[PRE]a[POST]");
    }

    @Test
    public void afterPropertiesSetTestInsidelineProd() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, null, "prod", "PROD", "lax1", "insideline");
        TestUtils.expectLogicalEnvironment(configurationEntryReader, "prod", "a");
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();

        // Legacy mode returns site edmunds
        TestUtils.assertConfiguration(configuration, false, "prod", "lax1", "insideline", "");
        TestUtils.assertLogicalEnvironment(configuration, "prod", "a");

        testConfigurationUtil("LAX1-PROD", "http://www.insideline.com/", "[PRE]LAX1-PROD[POST]", "[PRE]prod[POST]",
            "[PRE]a[POST]");
    }

    @Test
    public void afterPropertiesSetTestLocal() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, null, null, null, null, null);
        TestUtils.expectLogicalEnvironment(configurationEntryReader, null, null);
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();

        // Legacy mode returns site edmunds
        TestUtils.assertConfiguration(configuration, true, "local", "lax1", "edmunds", "");
        TestUtils.assertLogicalEnvironment(configuration, "local", "a");

        testConfigurationUtil("LOCAL", "http://www.insideline.com/", "[PRE]LOCAL[POST]",
            "[PRE]local[POST]",
            "[PRE]a[POST]");
    }

    @Test
    public void afterPropertiesSetLogEnvAbsenceTest() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, "DEV-EPE3", "dev-epe3", "DEV-EPE3", "lax1", "insideline");
        TestUtils.expectLogicalEnvironment(configurationEntryReader, null, null);
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();
        TestUtils.assertConfiguration(configuration, false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        TestUtils.assertLogicalEnvironment(configuration, "dev-epe3", "a");

        testConfigurationUtil("DEV-EPE3", "http://dev-epe3-www.insideline.com/", "[PRE]DEV-EPE3[POST]",
            "[PRE]dev-epe3[POST]",
            "[PRE]a[POST]");
    }

    @Test
    public void afterPropertiesSetLogAndEnvAbsenceTest() throws Exception {
        TestUtils.expectEnvironment(configurationEntryReader, "managed", null, "DEV-EPE3", "lax1", "insideline");
        TestUtils.expectLogicalEnvironment(configurationEntryReader, null, "b");
        replay(configurationEntryReader);

        configurationFactory.afterPropertiesSet();

        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();
        TestUtils.assertConfiguration(configuration, false, "managed", "lax1", "insideline", "dev-epe3-");
        TestUtils.assertLogicalEnvironment(configuration, "managed", "b");

        testConfigurationUtil("MANAGED", "http://dev-epe3-www.insideline.com/", "[PRE]MANAGED[POST]",
            "[PRE]managed[POST]",
            "[PRE]b[POST]");
    }

    private void testConfigurationUtil(String legacyEnvironmentName, String url, String environmentName,
                                       String logicalEnvName, String envIndex) {
        final EnvironmentConfiguration configuration = configurationFactory.getEnvironmentConfiguration();
        final EnvironmentConnection connection = new EnvironmentConnection();
        final ConfigurationUtilImpl configurationUtil = new ConfigurationUtilImpl(configuration, connection);

        assertEquals(configurationUtil.getLegacyEnvironmentName(), legacyEnvironmentName);
        assertEquals(configurationUtil.replaceTokens("http://_URL_PREFIX_www.insideline.com/"), url);
        assertEquals(configurationUtil.replaceTokens("[PRE]_ENVIRONMENT_NAME_[POST]"), environmentName);
        assertEquals(configurationUtil.replaceTokens("[PRE][LOGICAL_ENVIRONMENT_NAME][POST]"), logicalEnvName);
        assertEquals(configurationUtil.replaceTokens("[PRE][ENVIRONMENT_INDEX][POST]"), envIndex);
    }

    @AfterMethod
    public void tearDown() {
        if(configurationEntryReader != null) {
            verify(configurationEntryReader);
        }
        configurationEntryReader = null;
        configurationFactory = null;
    }
}

