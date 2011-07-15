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

/**
 * Provides properties used to connect to other environments and services.
 */
public class EnvironmentConnection {
    /**
     * Default internal environment (pi).
     */
    public static final String DEFAULT_INTERNAL_ENVIRONMENT_NAME = "pi";

    /**
     * Default data center where PI runs (lax1).
     * <p/>
     * NOTE: Implementations will typically override this property using DNS.
     */
    public static final String DEFAULT_INTERNAL_DATA_CENTER = "lax1";

    /**
     * The environment name of the internal environment.
     * <p/>
     * This property is typically only valid/useful in media environments.
     */
    private String internalEnvironmentName;

    /**
     * The data center of the internal environment.
     * <p/>
     * This property is typically only valid/useful in media environments.
     */
    private String internalDataCenter;

    /**
     * Initializes defaults.
     */
    public EnvironmentConnection() {
        this.internalEnvironmentName = DEFAULT_INTERNAL_ENVIRONMENT_NAME;
        this.internalDataCenter = DEFAULT_INTERNAL_DATA_CENTER;
    }

    /**
     * Gets the environment name of the internal environment.
     * <p/>
     * This property is typically only valid/useful in media environments.
     *
     * @return the environment name of the internal environment.
     */
    public String getInternalEnvironmentName() {
        return internalEnvironmentName;
    }

    /**
     * Sets the environment name of the internal environment.
     * <p/>
     * This property is typically only valid/useful in media environments.
     *
     * @param internalEnvironmentName the environment name of the internal environment.
     */
    public void setInternalEnvironmentName(String internalEnvironmentName) {
        this.internalEnvironmentName = (internalEnvironmentName == null) ? null : internalEnvironmentName.toLowerCase();
    }

    /**
     * Gets the data center of the internal environment.
     * <p/>
     * This property is typically only valid/useful in media environments.
     *
     * @return the data center containing the internal environment.
     */
    public String getInternalDataCenter() {
        return internalDataCenter;
    }

    /**
     * Sets the data center of the internal environment.
     * <p/>
     * This property is typically only valid/useful in media environments.
     *
     * @param internalDataCenter the data center containing the internal environment.
     */
    public void setInternalDataCenter(String internalDataCenter) {
        this.internalDataCenter = (internalDataCenter == null) ? null : internalDataCenter.toLowerCase();
    }
}
