/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   SmartCity Jena, Stefan Bischof - initial
 *
 */
package org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb;

import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingScript;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class ScriptImpl implements MappingScript {

    @XmlAttribute(name = "language")
    private String language = "JavaScript";  // attribute default: JavaScript

    @XmlValue
    private String cdata;  // All text goes here

    @Override
    public String language() {
        return language;
    }

    @Override
    public String cdata() {
        return cdata;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCdata(String cdata) {
        this.cdata = cdata;
    }
}
