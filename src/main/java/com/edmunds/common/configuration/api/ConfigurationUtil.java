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
 * Utility configuration functions.
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
public interface ConfigurationUtil {

    /**
     * Fetches the legacy environment name.
     * <p/>
     * Legacy names are automatically upper-cased.
     * Normally the environment is not prefixed however this method applies the prefix for PROD.
     *
     * @return the environment name in upper case and LAX1-PROD for production.
     */
    public String getLegacyEnvironmentName();

    /**
     * Parses the given value and replaces standard variables with their values. (environment name, host name, etc)
     *
     * @param value the value to parse
     * @return the parsed value.
     */
    public String replaceTokens(String value);

    /**
     * Similar to {@code replaceTokens}, however takes two parameters and returns the active version.
     *
     * @param local   string to be replaced when in local mode.
     * @param managed string to be replaced when running in managed mode.
     * @return the current string with the tokens replaced.
     */
    public String replacePropertyTokens(String local, String managed);
}
