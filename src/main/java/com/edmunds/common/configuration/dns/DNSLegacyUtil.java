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

/**
 * Utility functions for dealing with legacy environment names.
 * <p/>
 * <ul>
 * <li> Legacy environment names are in uppercase.
 * <li> Current environment names are in lowercase.
 * <li> The legacy production environment name is LAX1-PROD,
 * the current system splits out the LAX1 into the data center field.
 * </ul>
 * <p/>
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
abstract class DNSLegacyUtil {

    private static final String LEGACY_PRODUCTION_ENVIRONMENT_NAME = "lax1-prod";
    private static final String PRODUCTION_ENVIRONMENT_NAME = "prod";

    /**
     * Takes a legacy environment name and converts it to the current environment name.
     *
     * @param environmentName the legacy environment name.
     * @return the new environment name.
     */
    static String getActualEnvironmentName(String environmentName) {
        if (environmentName == null) {
            return null;
        }

        if (StringUtils.isBlank(environmentName)) {
            return "";
        }

        environmentName = environmentName.toLowerCase();

        if (LEGACY_PRODUCTION_ENVIRONMENT_NAME.equals(environmentName)) {
            environmentName = PRODUCTION_ENVIRONMENT_NAME;
        }

        return environmentName;
    }

    /**
     * Takes a current environment name and converts it to the legacy environment name.
     *
     * @param configuration
     * @return the legacy environment name.
     */
    static String getLegacyEnvironmentName(EnvironmentConfiguration configuration) {
        if (configuration == null) {
            return null;
        }

        String environmentName = configuration.getEnvironmentName();

        if (environmentName == null) {
            return null;
        }

        if (StringUtils.isBlank(environmentName)) {
            return "";
        }

        // Handle it when the input is bad.
        final String lowerEnvironmentName = environmentName.toLowerCase();

        if (PRODUCTION_ENVIRONMENT_NAME.equals(lowerEnvironmentName)) {
            String dataCenter = configuration.getDataCenter();

            if (StringUtils.isBlank(dataCenter)) {
                dataCenter = "lax1";
            }

            environmentName = dataCenter + "-" + lowerEnvironmentName;
        }

        return environmentName.toUpperCase();
    }
}
