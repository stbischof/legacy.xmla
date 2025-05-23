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

public enum MemberGrantAccessEnum {

    ALL("all"), NONE("none");

    private final String value;

    MemberGrantAccessEnum(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public static MemberGrantAccessEnum fromValue(String v) {
        for (MemberGrantAccessEnum c : MemberGrantAccessEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
