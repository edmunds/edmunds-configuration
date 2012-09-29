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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A FileConfigurationEntryReader reads environment configuration values from a properties file. The keys in the
 * properties file are equivalent to the "host name" part of the DNS text records expected by
 * {@link DNSConfigurationEntryReader}.
 *
 * Example properties file:
 * <pre>
 *  environment-name=prod-a
 *  logical-environment-name=prod
 *  environment-index=a
 *  environment-datacenter=lax1
 *  environment-site=edmunds
 *  url-prefix=prod-a
 * </pre>
 *
 * This class is used in place of {@link DNSConfigurationEntryReader} when the expected properties file exists
 * (See {@link ConfigurationEntryReaderFactory for details}. The properties file would typically be deployed and
 * maintained by a configuration management tool (e.g. Chef).
 *
 * @author Ryan Holmes
 */
public class FileConfigurationEntryReader implements ConfigurationEntryReader {

    private static final Logger log = Logger.getLogger(FileConfigurationEntryReader.class);
    private static final String PROPERTY_FILE_PATH
        = "/deployments/edmunds/properties/common/configuration-dns.properties";

    private Properties properties;

    /**
     * Returns true if the configuration-dns properties file exists.
     * @return true if properties file exists, false otherwise
     */
    public static boolean propertiesFileExists() {
        return new File(PROPERTY_FILE_PATH).exists();
    }

    /**
     * Returns the value of the specified configuration entry.
     * @param entryName the name of the configuration entry to retrieve.
     * @return value of the configuration entry
     */
    @Override
    public String getEntry(String entryName) {
        // Strip domain name from entry
        String name;
        int dotIndex = entryName.indexOf(".");
        if(dotIndex > 0) {
            name = entryName.substring(0, dotIndex);
        } else {
            name = entryName;
        }
        Properties props = getProperties();
        String value = props.getProperty(name);
        if(StringUtils.isBlank(value)) {
            String msg = String.format("No property found for environment attribute: %s", name);
            log.warn(msg);
        } else {
            String msg = String.format("Found property %s with value %s", name, value);
            log.debug(msg);
        }

        return value;
    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();

            if (propertiesFileExists()) {
                try {
                    properties.load(new FileReader(PROPERTY_FILE_PATH));
                } catch (IOException e) {
                    log.error("Cannot read environment properties file", e);
                }
            } else {
                log.error("Environment properties file does not exist: " + PROPERTY_FILE_PATH);
            }
        }
        return properties;
    }
}
