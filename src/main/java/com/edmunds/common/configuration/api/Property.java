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
 * This class can be used as a Spring Bean with a single property that can be defined in the XML and be overriden via
 * our standard configuration mechanisms.
 * <p/>
 * Copyright (C) 2009 Edmunds.com
 * <p/>
 * Date: May 13, 2009:10:28:33 AM
 *
 * @author Eric Gramond
 */
public class Property {

    /**
     * The property value being held by this Object.
     */
    private String property;

    /**
     * Gets the property value being held by this Object.
     *
     * @return the property value being held by this Object.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the property value being held by this Object.
     *
     * @param property the property value being held by this Object.
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Outputs the property value.
     */
    public String toString() {
        return property;
    }
}
