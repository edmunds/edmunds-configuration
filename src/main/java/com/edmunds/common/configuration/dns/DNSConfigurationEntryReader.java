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
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * Fetches the configuration entries from DNS via the sun JNDI adapter.
 * <p/>
 * Copyright (C) 2010 Edmunds.com
 */
@Component
class DNSConfigurationEntryReader implements ConfigurationEntryReader {
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(DNSConfigurationEntryReader.class);

    /**
     * Fetches the configuration entry.
     *
     * @param entryName the name of the configuration entry to fetch.
     * @return the current value of the configuration entry or null if it is not set.
     */
    @Override
    public String getEntry(String entryName) {
        if (entryName == null) {
            return null;
        }

        String returnValue = null;
        try {
            Hashtable<String, String> env = new Hashtable<String, String>(1);
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext initialContext = new InitialDirContext(env);
            Attributes attributes = initialContext.getAttributes(entryName, new String[]{"TXT"});
            Attribute attribute = attributes.get("TXT");
            if (attribute == null || attribute.size() == 0) {
                log.warn("No TXT attribute found for DNS entry: '" + entryName + "'.");
            } else {
                returnValue = (String) attribute.get(0);
            }
        } catch (NamingException exc) {
            log.warn("NamingException looking up TXT entry '" + entryName + "' from DNS: " + exc.getMessage());
        }

        return returnValue;
    }
}
