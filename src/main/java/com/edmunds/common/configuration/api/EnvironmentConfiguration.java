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
 * Contains the raw values of the environmental properties typically read from DNS.
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
public class EnvironmentConfiguration {

    /**
     * Is the process is running locally on a developers machine.
     */
    private boolean localEnvironment;

    /**
     * The name of the environment.
     * <p/>
     * Typical values: di, ti, pi, dev-epe3, proda, prodb
     */
    private String environmentName;

    /**
     * The name of the logical environment.
     * <p/>
     * Typical values: prod, dev, qa, stage
     */
    private String logicalEnvironmentName;

    /**
     * The index of environment.
     * <p/>
     * Typical values: A, B, C
     */
    private String environmentIndex;

    /**
     * The data center that this application/process is running in.
     * <p/>
     * Typical values: smdc, lax1, ord
     */
    private String dataCenter;

    /**
     * The Edmunds property.
     * <p/>
     * Typical Values: insideline, edmunds, internal
     */
    private String site;

    /**
     * Url prefix used when building URL's for customer facing web applications.
     * <p/>
     * Typical values: DEV-EPE3
     */
    private String urlPrefix;

    /**
     * Url legacy prefix used when building URL's for customer facing web applications.
     * <p/>
     * Typical values: DEV-EPE3
     */
    private String urlLegacyPrefix;

    /**
     * Constructor initializes with default values for unit testing.
     */
    public EnvironmentConfiguration() {
        this.localEnvironment = true;
        this.environmentName = "development";
        this.dataCenter = "development";
        this.site = "development";
        this.urlPrefix = "";
        this.urlLegacyPrefix = "";
        this.logicalEnvironmentName = "dev";
        this.environmentIndex = "";
    }

    /**
     * Is the process is running locally on a developers machine.
     *
     * @return true if this process is running on a developers machine (default: true).
     */
    public boolean isLocalEnvironment() {
        return localEnvironment;
    }

    public void setLocalEnvironment(boolean localEnvironment) {
        this.localEnvironment = localEnvironment;
    }

    /**
     * The name of the environment.
     * <p/>
     * Typical values: di, ti, pi, dev-epe3, prod
     *
     * @return the name of the environment (default: development).
     */
    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * Sets the environment name.
     *
     * @param environmentName the environment name.
     */
    public void setEnvironmentName(String environmentName) {
        this.environmentName = (environmentName == null) ? null : environmentName.toLowerCase();
    }

    /**
     * The data center that this application/process is running in.
     * <p/>
     * Typical values: smdc, lax1, ord
     *
     * @return the name of the data center (default: development).
     */
    public String getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(String dataCenter) {
        this.dataCenter = (dataCenter == null) ? null : dataCenter.toLowerCase();
    }

    /**
     * The Edmunds property.
     * <p/>
     * Typical Values: insideline, edmunds, internal
     *
     * @return the Edmunds property  (default: development).
     */
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = (site == null) ? null : site.toLowerCase();
    }

    /**
     * Url prefix used when building URL's for customer facing web applications.
     * <p/>
     * Typical values: DEV-EPE3
     *
     * @return the URL prefix (default: "").
     */
    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = (urlPrefix == null) ? null : urlPrefix.toLowerCase();
    }

    /**
     * Url legacy prefix used when building URL's for customer facing web applications.
     * <p/>
     * This version of the prefix does not include the data center except in the case of production.
     * <p/>
     * Typical values: DEV-EPE3
     *
     * @return the URL prefix (default: "").
     */
    public String getUrlLegacyPrefix() {
        return urlLegacyPrefix;
    }

    public void setUrlLegacyPrefix(String urlLegacyPrefix) {
        this.urlLegacyPrefix = (urlLegacyPrefix == null) ? null : urlLegacyPrefix.toLowerCase();
    }

    /**
     * Returns logical environment name.
     * <p/>
     * Typical values: prod, dev, qa.
     *
     * @return logical environment name.
     */
    public String getLogicalEnvironmentName() {
        return logicalEnvironmentName;
    }

    /**
     * Sets logical environment name.
     * Typical values: prod, dev, qa.
     *
     * @param logicalEnvironmentName the logical environment name.
     */
    public void setLogicalEnvironmentName(String logicalEnvironmentName) {
        this.logicalEnvironmentName = (logicalEnvironmentName == null) ? null : logicalEnvironmentName.toLowerCase();
    }

    /**
     * Returns environment index.
     * Typical values: a, b, c.
     *
     * @return environment index.
     */
    public String getEnvironmentIndex() {
        return environmentIndex;
    }

    /**
     * Sets environment index.
     * Typical values: a, b, c.
     *
     * @param environmentIndex environment index.
     */
    public void setEnvironmentIndex(String environmentIndex) {
        this.environmentIndex = (environmentIndex == null) ? null : environmentIndex.toLowerCase();
    }
}
