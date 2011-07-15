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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Test(groups = {"DNSConfiguration"})
public class ConfigurationUtilImplTest {

    private EnvironmentConfiguration configuration;
    private EnvironmentConnection connection;
    private ConfigurationUtilImpl configurationUtil;

    @BeforeClass
    public void initLogging() {
        BasicConfigurator.configure();
        // Don't output debug.
        Logger.getRootLogger().setLevel(Level.INFO);
    }

    @BeforeMethod
    public void setup() {
        configuration = new EnvironmentConfiguration();
        connection = new EnvironmentConnection();
        configurationUtil = new ConfigurationUtilImpl(configuration, connection);
    }

    @Test
    public void defaultConstructorTest() {
        // Just make sure no exceptions are thrown.
        new ConfigurationUtilImpl();
    }

    @Test
    public void getLegacyEnvironmentNameTestLocal() {
        expectGetConfiguration(true, "local", "lax1", "edmunds", "");
        assertEquals(configurationUtil.getLegacyEnvironmentName(), "LOCAL");
    }

    @Test
    public void getLegacyEnvironmentNameTestEpe3() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3");
        assertEquals(configurationUtil.getLegacyEnvironmentName(), "DEV-EPE3");
    }

    @Test
    public void getLegacyEnvironmentNameTestProd() {
        expectGetConfiguration(false, "prod", "lax1", "insideline", "prod");
        assertEquals(configurationUtil.getLegacyEnvironmentName(), "LAX1-PROD");
    }

    @Test
    public void replaceTokensTestEnvironmentNameProd() {
        expectGetConfiguration(false, "prod", "lax1", "insideline", "prod");
        assertEquals(configurationUtil.replaceTokens("[PRE]_ENVIRONMENT_NAME_[POST]"), "[PRE]LAX1-PROD[POST]");
    }

    @Test
    public void replaceTokensTestUrlPrefixEpe3() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "DEV-EPE3-");
        assertEquals(configurationUtil.replaceTokens("[PRE]_URL_PREFIX_[POST]"), "[PRE]dev-epe3-[POST]");
    }

    @Test
    public void replaceTokensTestUrlPrefixLocal() {
        expectGetConfiguration(true, "local", "lax1", "edmunds", "");
        assertEquals(configurationUtil.replaceTokens("[PRE]_URL_PREFIX_[POST]"), "[PRE][POST]");
    }

    @Test
    public void replaceTokensTestUrlPrefixProd() {
        expectGetConfiguration(false, "prod", "lax1", "insideline", "");
        assertEquals(configurationUtil.replaceTokens("[PRE]_URL_PREFIX_[POST]"), "[PRE][POST]");
    }

    @Test
    public void replaceTokensTestHostName() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3");
        final String value = configurationUtil.replaceTokens("[PRE]_HOST_NAME_[POST]");
        assertTrue(value.startsWith("[PRE]"));
        assertTrue(value.endsWith("[POST]"));
        assertFalse(value.contains("_HOST_NAME_"));
    }

    @Test
    public void replaceTokensTestHostCanonicalName() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3");
        final String value = configurationUtil.replaceTokens("[PRE]_HOST_CANONICAL_NAME_[POST]");
        assertTrue(value.startsWith("[PRE]"));
        assertTrue(value.endsWith("[POST]"));
        assertFalse(value.contains("_HOST_CANONICAL_NAME_"));
    }

    @Test
    public void replaceTokensTestUrlPrefix() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        assertEquals(configurationUtil.replaceTokens("[PRE][URL_PREFIX][POST]"), "[PRE]dev-epe3-[POST]");
    }

    @Test
    public void replaceTokensTestUrlPrefixNoDash() {
        expectGetConfiguration(false, "dev-epe8", "lax1", "insideline", "dev-epe8-");
        assertEquals(configurationUtil.replaceTokens("text[URL_PREFIX_NODASH]."), "textdev-epe8.");
    }

    @Test
    public void replaceTokensTestLocalEnvironmentName() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        assertEquals(configurationUtil.replaceTokens("[PRE][LOCAL_ENVIRONMENT_NAME][POST]"), "[PRE]dev-epe3[POST]");
    }

    @Test
    public void replaceTokensTestLocalEnvironmentDataCenter() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        assertEquals(configurationUtil.replaceTokens("[PRE][LOCAL_ENVIRONMENT_DATA_CENTER][POST]"), "[PRE]lax1[POST]");
    }

    @Test
    public void replaceTokensTestLocalEnvironmentSite() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        assertEquals(configurationUtil.replaceTokens("[PRE][LOCAL_ENVIRONMENT_SITE][POST]"), "[PRE]insideline[POST]");
    }

    @Test
    public void replaceTokensTestInternalEnvironmentName() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        expectConnection("di", "ord");
        assertEquals(configurationUtil.replaceTokens("[PRE][INTERNAL_ENVIRONMENT_NAME][POST]"), "[PRE]di[POST]");
    }

    @Test
    public void replaceTokensTestInternalEnvironmentDataCenter() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3-");
        expectConnection("di", "ord");
        assertEquals(configurationUtil.replaceTokens("[PRE][INTERNAL_ENVIRONMENT_DATA_CENTER][POST]"), "[PRE]ord[POST]");
    }

    @Test
    public void replacePropertyTokensTestLocal() {
        expectGetConfiguration(true, "local", "lax1", "edmunds", "");
        assertEquals(configurationUtil.replacePropertyTokens(
                "local[PRE]_ENVIRONMENT_NAME_[POST]", "managed[PRE]_ENVIRONMENT_NAME_[POST]"),
                "local[PRE]LOCAL[POST]");
    }

    @Test
    public void replacePropertyTokensTestEpe3() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3");
        assertEquals(configurationUtil.replacePropertyTokens(
                "local[PRE]_ENVIRONMENT_NAME_[POST]", "managed[PRE]_ENVIRONMENT_NAME_[POST]"),
                "managed[PRE]DEV-EPE3[POST]");
    }

    @Test
    public void replacePropertyTokensTestNullLocal() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3");
        assertEquals(configurationUtil.replacePropertyTokens(
                null, "managed[PRE]_ENVIRONMENT_NAME_[POST]"),
                "managed[PRE]DEV-EPE3[POST]");
    }

    @Test
    public void replacePropertyTokensTestNullManaged() {
        expectGetConfiguration(false, "dev-epe3", "lax1", "insideline", "dev-epe3");
        assertNull(configurationUtil.replacePropertyTokens(
                "local[PRE]_ENVIRONMENT_NAME_[POST]", null));
    }

    @AfterMethod
    public void tearDown() {
        configurationUtil = null;
        configuration = null;
        connection = null;
    }

    private void expectGetConfiguration(
            boolean localEnvironment, String environmentName, String dataCenter, String site, String urlPrefix) {

        configuration.setLocalEnvironment(localEnvironment);
        configuration.setEnvironmentName(environmentName);
        configuration.setDataCenter(dataCenter);
        configuration.setSite(site);
        configuration.setUrlPrefix(urlPrefix);
        configuration.setUrlLegacyPrefix(urlPrefix);
    }

    private void expectConnection(
            String environmentName, String dataCenter) {

        connection.setInternalEnvironmentName(environmentName);
        connection.setInternalDataCenter(dataCenter);
    }
}
