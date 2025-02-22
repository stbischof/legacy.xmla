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
package org.eclipse.daanse.olap.action.api;

import org.eclipse.daanse.xmla.api.common.enums.ActionTypeEnum;
import org.eclipse.daanse.xmla.api.common.enums.CoordinateTypeEnum;

import java.util.Optional;

public interface XmlaAction {
    String content( String coordinate, String cubeName );
    Optional<String> catalogName();
    Optional<String> schemaName();
    String cubeName();
    Optional<String> actionName();
    Optional<String> actionCaption();
    Optional<String> description();
    String coordinate();
    CoordinateTypeEnum coordinateType();
    ActionTypeEnum actionType();
}
