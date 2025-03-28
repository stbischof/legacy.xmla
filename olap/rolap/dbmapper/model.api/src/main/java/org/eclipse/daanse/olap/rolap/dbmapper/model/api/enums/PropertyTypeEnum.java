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
package org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums;

public enum PropertyTypeEnum {

    STRING("String"),
    NUMERIC("Numeric"),
    INTEGER("Integer"),
    LONG("Long"),
    BOOLEAN("Boolean"),
    DATE("Date"),
    TIME("Time"),
    TIMESTAMP("Timestamp");

    private final String value;

    PropertyTypeEnum(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public static PropertyTypeEnum fromValue(String v) {
        for (PropertyTypeEnum c : PropertyTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
