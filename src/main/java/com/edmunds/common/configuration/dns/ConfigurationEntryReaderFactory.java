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

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Factory that creates an appropriate {@link ConfigurationEntryReader}. A {@link FileConfigurationEntryReader}
 * will be used if its properties file exists. Otherwise, we fall back to DNS configuration.
 *
 * @author Ryan Holmes
 */
@Component("configurationEntryReaderFactory")
public class ConfigurationEntryReaderFactory extends AbstractFactoryBean {

    @Override
    public Class getObjectType() {
        return ConfigurationEntryReader.class;
    }

    @Override
    protected Object createInstance() throws Exception {
        // Use the file reader if it's file is present.
        if(FileConfigurationEntryReader.propertiesFileExists()) {
            return new FileConfigurationEntryReader();
        } else {
            return new DNSConfigurationEntryReader();
        }

    }

}
