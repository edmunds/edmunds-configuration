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
package com.edmunds.common.configuration.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Spring Factory is used to generate a property based on a local and production values for the property.
 * DNS entries are looked up to decide which of the two values will be used.
 * This factory also supports token replacements for the environment and host names in the property values.
 * <p/>
 * The values set into this property by the spring xml configuration can also be overriden using the spring
 * PropertyOverrideConfigurer. Care should be taken however to override the local or prod value depending on which one
 * will be used.
 * <p/>
 * Copyright (C) 2009 Edmunds.com
 * <p/>
 * Date: Apr 24, 2009:9:41:46 AM
 *
 * @author Eric Gramond
 */
public class EnvironmentPropertyFactory implements FactoryBean {

    /**
     * Constant token that will be replaced by the environment name in the property values.
     */
    public static final String ENVIRONMENT_REPLACE_TOKEN = "_ENVIRONMENT_NAME_";

    /**
     * Token that will be replaced with the logical environment name.
     */
    public static final String TOKEN_LOGICAL_ENVIRONMENT_NAME = "[LOGICAL_ENVIRONMENT_NAME]";

    /**
     * Token that will be replaced with the environment index.
     */
    public static final String TOKEN_ENVIRONMENT_INDEX = "[ENVIRONMENT_INDEX]";

    /**
     * Constant token that will be replaced by the current host name in the property values.
     */
    public static final String HOST_REPLACE_TOKEN = "_HOST_NAME_";

    /**
     * Constant token that will be replaced by the current canonical host name in the property values.
     * (hostname + domain name)
     */
    public static final String CANONICAL_HOST_REPLACE_TOKEN = "_HOST_CANONICAL_NAME_";

    /**
     * Constant token that will be replaced by the legacy environment URL prefix in the property values.
     * <p/>
     * Note: This token is based on the urlLegacyPrefix property.
     */
    public static final String URL_PREFIX_REPLACE_TOKEN = "_URL_PREFIX_";

    /**
     * Token that will be replaced with the environment URL prefix.
     * <p/>
     * Note: This token is based on the urlPrefix property.
     */
    public static final String TOKEN_URL_PREFIX = "[URL_PREFIX]";

    /**
     * Token that will be replaced with the environment URL prefix, without its final dash.
     * <p/>
     * Note: This token is based on the urlPrefix property.
     */
    public static final String TOKEN_URL_PREFIX_NODASH = "[URL_PREFIX_NODASH]";

    /**
     * Token that will be replaced with the environment name of the local environment.
     */
    public static final String TOKEN_LOCAL_ENVIRONMENT_NAME = "[LOCAL_ENVIRONMENT_NAME]";

    /**
     * Token that will be replaced with the data center of the local environment.
     */
    public static final String TOKEN_LOCAL_ENVIRONMENT_DATA_CENTER = "[LOCAL_ENVIRONMENT_DATA_CENTER]";

    /**
     * Token that will be replaced with the site that the application is services.
     */
    public static final String TOKEN_LOCAL_ENVIRONMENT_SITE = "[LOCAL_ENVIRONMENT_SITE]";

    /**
     * Token that will be replaced with the environment name (DI/TI/PI) of the internal environment.
     */
    public static final String TOKEN_INTERNAL_ENVIRONMENT_NAME = "[INTERNAL_ENVIRONMENT_NAME]";

    /**
     * Token that will be replaced with the data center of the internal environment.
     */
    public static final String TOKEN_INTERNAL_ENVIRONMENT_DATA_CENTER = "[INTERNAL_ENVIRONMENT_DATA_CENTER]";

    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(EnvironmentPropertyFactory.class);

    /**
     * Delegate that is used for all business logic.
     */
    @Autowired
    private ConfigurationUtil configurationUtil;

    /**
     * The developer workstation property value.
     */
    private String local;

    /**
     * The production property value.
     */
    private String managed;

    /**
     * Default Constructor.
     */
    public EnvironmentPropertyFactory() {
    }

    /**
     * Unit test constructor.
     *
     * @param configurationUtil configuration util object to use.
     */
    public EnvironmentPropertyFactory(final ConfigurationUtil configurationUtil) {
        this.configurationUtil = configurationUtil;
    }

    /**
     * Gets the appropriate property depending on the current environment and performs any variable replacements
     * necessary on it before returning it.
     *
     * @return the processed property value.
     */
    public String getProperty() {
        log.debug("managed value: '" + managed + "'");
        return configurationUtil.replacePropertyTokens(local, managed);
    }

    /**
     * Parses the given value and replaces standard variables with their values. (environment name, host name, etc)
     *
     * @param value the value to parse
     * @return the parsed value.
     */
    public String parseValue(final String value) {
        return configurationUtil.replaceTokens(value);
    }

    /**
     * Spring factory method to return the Object being manufactured by this Factory.
     *
     * @return the security context.
     */
    public Object getObject() {
        String property = getProperty();
        log.debug("Using property value: '" + property + "'");
        return property;
    }

    /**
     * Spring factory method to determine the type of Object being manufactured by this Factory.
     *
     * @return DefaultSpringSecurityContextSource
     */
    public Class getObjectType() {
        return String.class;
    }

    /**
     * Makes this Factory not a singleton.
     *
     * @return false
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * Gets property value for local dev workstations.
     *
     * @return the dev environment property value.
     */
    public String getLocal() {
        return local;
    }

    /**
     * Sets the property value for local dev workstations.
     *
     * @param local the property value for dev workstations.
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * Gets the property value for production environments.
     *
     * @return the property value for production environments.
     */
    public String getManaged() {
        return managed;
    }

    /**
     * Sets the property value for production environments.
     *
     * @param managed the property value for production environments.
     */
    public void setManaged(String managed) {
        this.managed = managed;
    }
}
