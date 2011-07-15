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

import static org.easymock.EasyMock.expect;
import static org.testng.Assert.assertEquals;

class TestUtils {
    static void expectEnvironment(
            ConfigurationEntryReader configurationEntryReader,
            String legacyEnvironment, String environment, String urlPrefix, String dataCenter, String site) {
        expect(configurationEntryReader.getEntry("environment.edmunds.com")).andReturn(legacyEnvironment);
        expect(configurationEntryReader.getEntry("environment-name.edmunds.com")).andReturn(environment);
        expect(configurationEntryReader.getEntry("url-prefix.edmunds.com")).andReturn(urlPrefix);
        expect(configurationEntryReader.getEntry("environment-datacenter.edmunds.com")).andReturn(dataCenter);
        expect(configurationEntryReader.getEntry("environment-site.edmunds.com")).andReturn(site);
    }

    static void expectLogicalEnvironment(ConfigurationEntryReader configurationEntryReader, String logicalEnvironment,
                                         String environmentIndex) {
        expect(configurationEntryReader.getEntry("logical-environment-name.edmunds.com")).andReturn(logicalEnvironment);
        expect(configurationEntryReader.getEntry("environment-index.edmunds.com")).andReturn(environmentIndex);
    }

    static void expectInvalidEnvironment(
            ConfigurationEntryReader configurationEntryReader,
            String legacyEnvironment, String urlPrefix) {
        expect(configurationEntryReader.getEntry("environment.edmunds.com")).andReturn(legacyEnvironment);
        expect(configurationEntryReader.getEntry("environment-name.edmunds.com")).andReturn(null);
        expect(configurationEntryReader.getEntry("url-prefix.edmunds.com")).andReturn(urlPrefix);
    }

    static void assertConfiguration(
            EnvironmentConfiguration configuration,
            boolean local, String environmentName, String dataCenter, String site, String urlPrefix) {

        assertEquals(configuration.isLocalEnvironment(), local);
        assertEquals(configuration.getEnvironmentName(), environmentName);
        assertEquals(configuration.getDataCenter(), dataCenter);
        assertEquals(configuration.getSite(), site);
        assertEquals(configuration.getUrlPrefix(), urlPrefix);
        assertEquals(configuration.getUrlLegacyPrefix(), urlPrefix);
    }

    static void assertLogicalEnvironment(EnvironmentConfiguration configuration, String logicalEnvironment,
                                         String environmentIndex) {
        assertEquals(configuration.getLogicalEnvironmentName(), logicalEnvironment);
        assertEquals(configuration.getEnvironmentIndex(), environmentIndex);
    }
}
