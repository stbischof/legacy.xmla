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
package org.eclipse.daanse.xmla.model.record.engine300;

import java.math.BigInteger;

import org.eclipse.daanse.xmla.api.engine300.DimensionAttributeVisualizationProperties;

public record DimensionAttributeVisualizationPropertiesR(
    BigInteger folderPosition,
    String contextualNameRule,
    String alignment,
    Boolean isFolderDefault,
    Boolean isRightToLeft,
    String sortDirection,
    String units,
    BigInteger width,
    BigInteger defaultDetailsPosition,
    BigInteger commonIdentifierPosition,
    BigInteger sortPropertiesPosition,
    BigInteger displayKeyPosition,
    Boolean isDefaultImage,
    String defaultAggregateFunction
) implements DimensionAttributeVisualizationProperties {

}