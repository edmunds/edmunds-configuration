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

import com.edmunds.common.configuration.api.ConfigurationUtil;
import com.edmunds.common.configuration.api.EnvironmentConfiguration;
import com.edmunds.common.configuration.api.EnvironmentConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.CANONICAL_HOST_REPLACE_TOKEN;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.ENVIRONMENT_REPLACE_TOKEN;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.HOST_REPLACE_TOKEN;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_INTERNAL_ENVIRONMENT_DATA_CENTER;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_INTERNAL_ENVIRONMENT_NAME;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_LOCAL_ENVIRONMENT_DATA_CENTER;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_LOCAL_ENVIRONMENT_NAME;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_LOCAL_ENVIRONMENT_SITE;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_URL_PREFIX;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_URL_PREFIX_NODASH;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.URL_PREFIX_REPLACE_TOKEN;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_LOGICAL_ENVIRONMENT_NAME;
import static com.edmunds.common.configuration.api.EnvironmentPropertyFactory.TOKEN_ENVIRONMENT_INDEX;

/**
 * Implementation of configuration functions.
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
@Component
class ConfigurationUtilImpl implements ConfigurationUtil {

    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(ConfigurationUtilImpl.class);

    /**
     * The configuration.
     */
    @Autowired
    private EnvironmentConfiguration configuration;

    /**
     * The connections to other environments.
     */
    @Autowired
    private EnvironmentConnection connection;

    /**
     * Default constructor (no-op).
     */
    public ConfigurationUtilImpl() {
    }

    /**
     * Unit test constructor.
     *
     * @param configuration the configuration.
     */
    public ConfigurationUtilImpl(
            final EnvironmentConfiguration configuration, final EnvironmentConnection connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    @Override
    public String getLegacyEnvironmentName() {
        return DNSLegacyUtil.getLegacyEnvironmentName(configuration);
    }

    /**
     * Parses the given value and replaces standard variables with their values. (environment name, host name, etc)
     *
     * @param value the value to parse
     * @return the parsed value.
     */
    @Override
    public String replaceTokens(String value) {
        if (value == null) {
            return null;
        }

        Map<String, String> tokenMap = buildTokenMap();

        return replaceTokens(value, tokenMap);
    }

    private Map<String, String> buildTokenMap() {
        Validate.notNull(configuration, "ConfigurationUtilImpl.configuration is null");
        Validate.notNull(connection, "ConfigurationUtilImpl.connection is null");

        Map<String, String> tokenMap = new HashMap<String, String>();

        tokenMap.put(ENVIRONMENT_REPLACE_TOKEN, getLegacyEnvironmentName());
        tokenMap.put(URL_PREFIX_REPLACE_TOKEN, configuration.getUrlLegacyPrefix());
        tokenMap.put(TOKEN_LOGICAL_ENVIRONMENT_NAME, configuration.getLogicalEnvironmentName());
        tokenMap.put(TOKEN_ENVIRONMENT_INDEX, configuration.getEnvironmentIndex());

        final String urlPrefix = configuration.getUrlPrefix();
        tokenMap.put(TOKEN_URL_PREFIX, urlPrefix);
        tokenMap.put(TOKEN_LOCAL_ENVIRONMENT_NAME, configuration.getEnvironmentName());
        tokenMap.put(TOKEN_LOCAL_ENVIRONMENT_DATA_CENTER, configuration.getDataCenter());
        tokenMap.put(TOKEN_LOCAL_ENVIRONMENT_SITE, configuration.getSite());
        tokenMap.put(TOKEN_INTERNAL_ENVIRONMENT_NAME, connection.getInternalEnvironmentName());
        tokenMap.put(TOKEN_INTERNAL_ENVIRONMENT_DATA_CENTER, connection.getInternalDataCenter());
        String prefixNoDash = null;
        if(urlPrefix != null) {
            prefixNoDash = urlPrefix.endsWith("-") ? urlPrefix.substring(0, urlPrefix.length()-1) : urlPrefix;
        }
        tokenMap.put(TOKEN_URL_PREFIX_NODASH, prefixNoDash);

        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            tokenMap.put(HOST_REPLACE_TOKEN, hostName);
        } catch (UnknownHostException exc) {
            log.warn("Error looking up host name. No substitution will be performed: " + exc.getMessage(), exc);
        }

        try {
            String canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
            tokenMap.put(CANONICAL_HOST_REPLACE_TOKEN, canonicalHostName);
        } catch (UnknownHostException exc) {
            log.warn("Error looking up host name. No substitution will be performed: " + exc.getMessage(), exc);
        }

        return tokenMap;
    }

    private String replaceTokens(String value, Map<String, String> tokenMap) {
        for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
            String token = entry.getKey();
            String replace = entry.getValue();

            if (StringUtils.isBlank(replace)) {
                replace = "";
            }

            value = StringUtils.replace(value, token, replace);
        }
        return value;
    }

    /**
     * Similar to {@code replaceTokens}, however takes two parameters and returns the active version.
     *
     * @param local   string to be replaced when in local mode.
     * @param managed string to be replaced when running in managed mode.
     * @return the current string with the tokens replaced.
     */
    @Override
    public String replacePropertyTokens(String local, String managed) {
        if (local == null || managed == null) {
            log.warn("Missing dual property value. local='" + local + "' managed='" + managed + "'");
        }

        return replaceTokens(configuration.isLocalEnvironment() ? local : managed);
    }
}

