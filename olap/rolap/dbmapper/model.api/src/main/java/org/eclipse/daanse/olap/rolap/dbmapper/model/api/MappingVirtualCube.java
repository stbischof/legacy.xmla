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
package org.eclipse.daanse.olap.rolap.dbmapper.model.api;

import java.util.List;


public interface MappingVirtualCube extends MappingBaseInterface{

    List<MappingCubeUsage> cubeUsages();

    List<MappingVirtualCubeDimension> virtualCubeDimensions();

    List<MappingVirtualCubeMeasure> virtualCubeMeasures();

    List<MappingCalculatedMember> calculatedMembers();

    List<MappingNamedSet> namedSets();

    List<MappingKpi> kpis();

    Boolean enabled();

    String defaultMeasure();

    Boolean visible();
}
