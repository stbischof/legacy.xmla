/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation.
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
package org.eclipse.daanse.xmla.api.discover.mdschema.actions;

import java.util.Optional;

public interface MdSchemaActionsRestrictions {

    String RESTRICTIONS_CATALOG_NAME = "CATALOG_NAME";
    String RESTRICTIONS_SCHEMA_NAME = "SCHEMA_NAME";
    String RESTRICTIONS_CUBE_NAME = "CUBE_NAME";
    String RESTRICTIONS_ACTION_NAME = "ACTION_NAME";
    String RESTRICTIONS_ACTION_TYPE = "ACTION_TYPE";
    String RESTRICTIONS_COORDINATE = "COORDINATE";
    String RESTRICTIONS_COORDINATE_TYPE = "COORDINATE_TYPE";
    String RESTRICTIONS_INVOCATION = "INVOCATION";

    /**
     * @return The name of the database.
     */
    Optional<String> catalogName();

    /**
     * @return The name of the schema.
     */
    Optional<String> schemaName();

    /**
     * @return The name of the cube.
     */
    String cubeName();

    /**
     * @return The name of this action.
     */
    Optional<String> actionName();

    /**
     * @return A bitmask that is used to specify the action type.
     * 0x01 - Action type is URL.
     * 0x02 - Action type is HTML.
     * 0x04 - Action type is Statement.
     * 0x08 - Action type is Dataset.
     * 0x10 - Action type is Rowset.
     * 0x20 - Action type is Commandline.
     * 0x40 - Action type is Proprietary.
     * 0x80 - Action type is Report.
     * 0x100 - Action type is DrillThrough.
     * If the action is PROPRIETARY (0x40), then a value MUST be
     * provided in the APPLICATION column.
     */
    Optional<Integer> actionType();

    /**
     * @return An MDX expression that specifies an object or a coordinate in
     * the multidimensional space in which the action is performed.
     * The COORDINATE MUST resolve to the object specified in
     * COORDINATE_TYPE.
     */
    Optional<String> coordinate();

    /**
     * @return An enumeration that specifies how the COORDINATE restriction
     * column is interpreted. The possible values are as follows:
     * 1 - Action coordinate refers to the cube.
     * 2 - Action coordinate refers to a dimension.
     * 3 - Action coordinate refers to a level.
     * 4 - Action coordinate refers to a member.
     * 5 - Action coordinate refers to a set.
     * 6 - Action coordinate refers to a cell.
     */
    Integer coordinateType();

    /**
     * @return Information about how to invoke the action:
     * 1 - Indicates a regular action used during normal
     * operations. This is the default value for this column.
     * 2 - Indicates that the action is performed when the cube is
     * first opened.
     * 4 - Indicates that the action is performed as part of a batch
     * operation.
     */
    Integer invocation();
}