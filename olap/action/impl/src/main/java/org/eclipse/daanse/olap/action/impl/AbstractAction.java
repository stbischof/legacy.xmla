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

import org.eclipse.daanse.olap.action.api.XmlaAction;
import org.eclipse.daanse.xmla.api.common.enums.ActionTypeEnum;
import org.eclipse.daanse.xmla.api.common.enums.CoordinateTypeEnum;

import java.util.Optional;

public abstract class AbstractAction implements XmlaAction {


    @Override
    public Optional<String> catalogName() {
        return Optional.ofNullable(getConfig().catalogName());
    }

    @Override
    public Optional<String> schemaName() {
        return Optional.ofNullable(getConfig().schemaName());
    }

    @Override
    public String cubeName() {
        return getConfig().cubeName();
    }

    @Override
    public Optional<String> actionName() {
        return Optional.ofNullable(getConfig().actionName());
    }

    @Override
    public Optional<String> actionCaption() {
        return Optional.ofNullable(getConfig().actionCaption());
    }

    @Override
    public Optional<String> description() {
        return Optional.ofNullable(getConfig().actionDescription());
    }

    @Override
    public String coordinate() {
        return getConfig().actionCoordinate();
    }

    @Override
    public CoordinateTypeEnum coordinateType() {
        return CoordinateTypeEnum.valueOf(getConfig().actionCoordinateType());
    }

    @Override
    public abstract String content(String coordinate, String cubeName);

    public abstract ActionTypeEnum actionType();

    protected abstract AbstractActionConfig getConfig();

}