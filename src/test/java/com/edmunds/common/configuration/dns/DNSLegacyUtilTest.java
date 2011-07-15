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
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = {"DNSConfiguration"})
public class DNSLegacyUtilTest {

    @Test
    public void getActualEnvironmentNameTestNull() {
        assertEquals(DNSLegacyUtil.getActualEnvironmentName(null), null);
    }

    @Test
    public void getActualEnvironmentNameTestEmpty() {
        assertEquals(DNSLegacyUtil.getActualEnvironmentName(""), "");
    }

    @Test
    public void getActualEnvironmentNameTestBlank() {
        assertEquals(DNSLegacyUtil.getActualEnvironmentName("  "), "");
    }

    @Test
    public void getActualEnvironmentNameTestUpperCase() {
        assertEquals(DNSLegacyUtil.getActualEnvironmentName("DEV-EPE3"), "dev-epe3");
    }

    @Test
    public void getActualEnvironmentNameTestProd() {
        assertEquals(DNSLegacyUtil.getActualEnvironmentName("LAX1-PROD"), "prod");
    }

    @Test
    public void getActualEnvironmentNameTestProdMixed() {
        assertEquals(DNSLegacyUtil.getActualEnvironmentName("lax1-PROD"), "prod");
    }

    @Test
    public void getLegacyEnvironmentNameTestNull() {
        assertEquals(getLegacyEnvironmentName(null, "lax1"), null);
    }

    @Test
    public void getLegacyEnvironmentNameTestEmpty() {
        assertEquals(getLegacyEnvironmentName("", "lax1"), "");
    }

    @Test
    public void getLegacyEnvironmentNameTestBlank() {
        assertEquals(getLegacyEnvironmentName("  ", "lax1"), "");
    }

    @Test
    public void getLegacyEnvironmentNameTestLowerCase() {
        assertEquals(getLegacyEnvironmentName("dev-epe3", "lax1"), "DEV-EPE3");
    }

    @Test
    public void getLegacyEnvironmentNameTestProd() {
        assertEquals(getLegacyEnvironmentName("prod", "lax1"), "LAX1-PROD");
    }

    @Test
    public void getLegacyEnvironmentNameTestProdMixed() {
        assertEquals(getLegacyEnvironmentName("Prod", "lax1"), "LAX1-PROD");
    }

    @Test
    public void getLegacyEnvironmentNameTestProdOrd() {
        assertEquals(getLegacyEnvironmentName("prod", "ord"), "ORD-PROD");
    }

    private String getLegacyEnvironmentName(String environmentName, String dataCenter) {
        return DNSLegacyUtil.getLegacyEnvironmentName(createConfiguration(environmentName, dataCenter));
    }

    private EnvironmentConfiguration createConfiguration(String environmentName, String dataCenter) {
        EnvironmentConfiguration configuration = new EnvironmentConfiguration();

        configuration.setLocalEnvironment(false);
        configuration.setEnvironmentName(environmentName);
        configuration.setDataCenter(dataCenter);
        configuration.setSite("insideline");
        configuration.setUrlPrefix(environmentName + "-");
        configuration.setUrlLegacyPrefix(environmentName + "-");
        return configuration;
    }
}
