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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory that builds {@code EnvironmentConfiguration} objects using values read from DNS.
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
@Component("dnsConfigurationFactory")
class DNSConfigurationFactory implements InitializingBean {
    private static final String LEGACY_DNS_ENVIRONMENT_NAME = "environment.edmunds.com";

    private static final String LOGICAL_DNS_ENVIRONMENT_NAME = "logical-environment-name.edmunds.com";
    private static final String DNS_ENVIRONMENT_INDEX = "environment-index.edmunds.com";
    private static final String DEFAULT_ENVIRONMENT_INDEX = "a";

    private static final String DNS_ENVIRONMENT_NAME = "environment-name.edmunds.com";
    private static final String DNS_DATA_CENTER = "environment-datacenter.edmunds.com";
    private static final String DNS_SITE = "environment-site.edmunds.com";
    private static final String DNS_URL_PREFIX = "url-prefix.edmunds.com";

    private static final String DEFAULT_DATA_CENTER = "lax1";
    private static final String DEFAULT_SITE = "edmunds";

    private static final String LOCAL_ENVIRONMENT_NAME = "local";
    private static final String PROD_PREFIX_VALUE = "prod";

    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(DNSConfigurationFactory.class);

    /**
     * Configuration reader that implements the low level logic to actually read the DNS entry.
     */
    @Autowired
    private ConfigurationEntryReader configurationEntryReader;

    /**
     * The configuration object build by this factory.
     */
    private volatile EnvironmentConfiguration environmentConfiguration;

    /**
     * Default Constructor (No-op).
     */
    public DNSConfigurationFactory() {
    }

    /**
     * Constructor allowing configurationEntryReader to be overridden.
     * <p/>
     * Use this constructor in your unit tests.
     *
     * @param configurationEntryReader the customer configuration reader.
     */
    public DNSConfigurationFactory(final ConfigurationEntryReader configurationEntryReader) {
        this.configurationEntryReader = configurationEntryReader;
    }

    /**
     * Returns the configuration.
     *
     * @return the configuration.
     */
    public EnvironmentConfiguration getEnvironmentConfiguration() {
        return environmentConfiguration;
    }

    /**
     * Sets the configuration.
     *
     * @param environmentConfiguration the configuration.
     */
    public synchronized void setEnvironmentConfiguration(EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // Only set the configuration if it is null.
        if (getEnvironmentConfiguration() == null) {
            setEnvironmentConfiguration(buildConfiguration());
        }
    }

    private EnvironmentConfiguration buildConfiguration() {
        final String legacyEnvironmentName =
                DNSLegacyUtil.getActualEnvironmentName(getEntry(LEGACY_DNS_ENVIRONMENT_NAME));

        String environmentName = getEntry(DNS_ENVIRONMENT_NAME);
        String urlPrefix = getEntry(DNS_URL_PREFIX);

        boolean localMode = false;

        if (StringUtils.isBlank(environmentName)) {
            log.warn("The environment name DNS entry was not found. Checking legacy setting");

            if (StringUtils.isBlank(legacyEnvironmentName)) {
                log.warn("The environment name DNS entry was not found. Defaulting to local environment value");
                environmentName = LOCAL_ENVIRONMENT_NAME;
                localMode = true;
            } else {
                environmentName = legacyEnvironmentName;
            }
        }

        if (localMode) {
            environmentName = LOCAL_ENVIRONMENT_NAME;
            urlPrefix = "";
        } else {
            if (StringUtils.isBlank(urlPrefix)) {
                //a blank environment name outside of dev is very bad. abort everything.
                throw new IllegalStateException("Cannot have a blank url prefix outside of dev!!!!");
            }

            if (PROD_PREFIX_VALUE.equalsIgnoreCase(urlPrefix)) {
                urlPrefix = "";
            }
        }

        return newEnvironmentConfiguration(localMode, environmentName, urlPrefix);
    }

    private EnvironmentConfiguration newEnvironmentConfiguration(
            boolean localMode, String environmentName, String urlPrefix) {

        final String urlLegacyPrefix = formatLegacyPrefix(urlPrefix);
        final EnvironmentConfiguration config = new EnvironmentConfiguration();

        config.setLocalEnvironment(localMode);
        config.setEnvironmentName(environmentName);
        config.setUrlPrefix(urlLegacyPrefix);
        config.setUrlLegacyPrefix(urlLegacyPrefix);
        config.setDataCenter(getEntry(DNS_DATA_CENTER, DEFAULT_DATA_CENTER));
        config.setSite(getEntry(DNS_SITE, DEFAULT_SITE));

        config.setLogicalEnvironmentName(getEntry(LOGICAL_DNS_ENVIRONMENT_NAME, environmentName));
        config.setEnvironmentIndex(getEntry(DNS_ENVIRONMENT_INDEX, DEFAULT_ENVIRONMENT_INDEX));

        return config;
    }

    /**
     * Formats the url prefix per legacy requirements.
     * <ul>
     * <li>Upper cased.
     * <li>Ending in a dash if not blank.
     * </ul>
     *
     * @param urlPrefix the original url prefix.
     * @return the legacy URL prefix
     */
    private String formatLegacyPrefix(String urlPrefix) {
        if (StringUtils.isNotBlank(urlPrefix)) {
            // Apply dash to end of url prefix.
            if (!urlPrefix.endsWith("-")) {
                urlPrefix += "-";
            }

            // Maintain backward compatibility and upper-case all url prefixes.
            urlPrefix = urlPrefix.toUpperCase();
        }
        return urlPrefix;
    }

    private String getEntry(String entryName) {
        return getEntry(entryName, null);
    }

    private String getEntry(final String entryName, final String defaultValue) {
        final String value = configurationEntryReader.getEntry(entryName);
        return StringUtils.isNotBlank(value) ? value.toLowerCase() : defaultValue;
    }
}
