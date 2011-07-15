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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edmunds.common.configuration.api.EnvironmentConfiguration;
import com.edmunds.common.configuration.api.EnvironmentConnection;

/**
 *
 */
@Component("dnsConnectionFactory")
public class DNSConnectionFactory implements InitializingBean {

    /**
     * Environment configuration object initialized by the constructor.
     */
    private final EnvironmentConfiguration environmentConfiguration;

    private EnvironmentConnection environmentConnection;

    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(DNSConnectionFactory.class);

    /**
     * Auto-wired constructor.
     *
     * @param environmentConfiguration
     *            the environment configuration.
     */
    @Autowired
    public DNSConnectionFactory(EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
    }

    /**
     * Creates the environment connection object.
     *
     * @throws Exception
     *             not thrown.
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        final String environmentName = environmentConfiguration.getEnvironmentName();
        log.debug("DNSConnectionFactory environmentName: " + environmentName);
        final String internalEnvironmentName;

        if ("di".equalsIgnoreCase(environmentName) || "ti".equalsIgnoreCase(environmentName)) {
            internalEnvironmentName = environmentName.toLowerCase();
        } else {
            internalEnvironmentName = "pi";
        }

        environmentConnection = new EnvironmentConnection();
        log.debug("DNSConnectionFactory internalEnvironmentName: " + internalEnvironmentName);
        environmentConnection.setInternalEnvironmentName(internalEnvironmentName);
        environmentConnection.setInternalDataCenter(environmentConfiguration.getDataCenter());
    }

    /**
     * Gets the singleton environmentConnection bean.
     *
     * @return the environment connection.
     */
    public EnvironmentConnection getEnvironmentConnection() {
        return environmentConnection;
    }

    /**
     * Sets the environment connection object.
     *
     * @param environmentConnection
     *            the environment connection object.
     */
    public void setEnvironmentConnection(EnvironmentConnection environmentConnection) {
        this.environmentConnection = environmentConnection;
    }
}
