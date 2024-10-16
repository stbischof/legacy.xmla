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
 *   SmartCity Jena - initial
 *   Stefan Bischof (bipolis.org) - initial
 */
package org.eclipse.daanse.olap.action.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

public interface AbstractActionConfig {

    @AttributeDefinition(name = "%CATALOG_NAME", required = false)
    default String catalogName() {
        return null;
    }

    @AttributeDefinition(name = "%SCHEMA_NAME", required = false)
    default String schemaName() {
        return null;
    }

    @AttributeDefinition(name = "%CUBE_NAME", required = false)
    default String cubeName() {
        return null;
    }

    @AttributeDefinition(name = "%ACTION_NAME", required = false)
    default String actionName() {
        return null;
    }

    @AttributeDefinition(name = "%ACTION_CAPTION", required = false)
    default String actionCaption() {
        return null;
    }

    @AttributeDefinition(name = "%ACTION_DESCRIPTION", required = false)
    default String actionDescription() {
        return null;
    }

    @AttributeDefinition(name = "%ACTION_COORDINATE", required = false)
    default String actionCoordinate() {
        return null;
    }

    @AttributeDefinition(name = "%ACTION_COORDINATE_TYPE", required = false)
    default String actionCoordinateType() {
        return null;
    }

}
