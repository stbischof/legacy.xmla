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
package org.eclipse.daanse.xmla.api.xmla;

import org.eclipse.daanse.xmla.api.engine300.CalculationPropertiesVisualizationProperties;

import java.math.BigInteger;
import java.util.List;

public interface CalculationProperty {

    String calculationReference();

    String calculationType();

    CalculationProperty.Translations translations();

    String description();

    Boolean visible();

    BigInteger solveOrder();

    String formatString();

    String foreColor();

    String backColor();

    String fontName();

    String fontSize();

    String fontFlags();

    String nonEmptyBehavior();

    String associatedMeasureGroupID();

    String displayFolder();

    BigInteger language();

    CalculationPropertiesVisualizationProperties visualizationProperties();

    public interface Translations {

        List<Translation> translation();

    }

}