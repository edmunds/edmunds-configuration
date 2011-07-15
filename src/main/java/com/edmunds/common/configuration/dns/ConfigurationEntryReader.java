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

/**
 * Defines the low level function for accessing configuration entries.
 * <p/>
 * Since the implementation of this method typically reads its values from DNS it is difficult to unit test the
 * configuration code. This interface exists to allow the rest of the code to be tested without needing a DNS
 * infrastructure.
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
public interface ConfigurationEntryReader {
    /**
     * Fetches the configuration entry.
     * <p/>
     * This method should not perform any caching (the higher level logic will provide caching).
     *
     * @param entryName the name of the configuration entry to retrieve.
     * @return the current value or null if the configuration entry is not set.
     */
    String getEntry(String entryName);
}
