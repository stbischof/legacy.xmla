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
package org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb.util;

import java.util.List;

import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingAnnotation;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingFormula;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingNamedSet;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingParameter;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingSchema;
import org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb.AnnotationImpl;
import org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb.FormulaImpl;
import org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb.NamedSetImpl;
import org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb.ParameterImpl;
import org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb.SchemaImpl;

public class CopyUtil {

	private CopyUtil() {
	}

    public static SchemaImpl copy(MappingSchema schemaApi) {
        SchemaImpl schemaImpl = new SchemaImpl();
        schemaImpl.setAnnotations(copyAnnotation(schemaApi.annotations()));
//        schemaImpl.setDefaultRole(schemaApi.defaultRole());
        schemaImpl.setDescription(schemaApi.description());
        schemaImpl.setMeasuresCaption(schemaApi.measuresCaption());
        schemaImpl.setName(schemaApi.name());
        schemaImpl.setNamedSets(copyNamedSet(schemaApi.namedSets()));
        schemaImpl.setParameters(copyParameter(schemaApi.parameters()));
        schemaImpl.setRoles(null);
        schemaImpl.setUserDefinedFunctions(null);
        schemaImpl.setVirtualCubes(null);
        return schemaImpl;

    }

    private static List<MappingAnnotation> copyAnnotation(List<MappingAnnotation> annotations) {

        // Please no null checks in List. getList must provide minimal a empty List.
        return annotations.stream()
            .map(CopyUtil::copy)
            .toList();

    }

    private static List<MappingNamedSet> copyNamedSet(List<? extends MappingNamedSet> namedSet) {
        return namedSet.stream()
            .map(CopyUtil::copy)
            .toList();
    }

    private static List<MappingParameter> copyParameter(List<? extends MappingParameter> parameter) {
        return parameter.stream()
            .map(CopyUtil::copy)
            .toList();
    }

    //I'm not sure about this one, added it, because it fits the pattern; Daniel
    private static FormulaImpl copyFormula(MappingFormula formulaApi) {
        FormulaImpl impl = new FormulaImpl();
        impl.setCdata(formulaApi.cdata());
        return impl;
    }

    private static MappingAnnotation copy(MappingAnnotation annotationApi) {
        AnnotationImpl impl = new AnnotationImpl();
        impl.setContent(annotationApi.content());
        impl.setName(annotationApi.name());
        return impl;
    }

    private static MappingNamedSet copy(MappingNamedSet namedSetApi) {
        NamedSetImpl impl = new NamedSetImpl();
        impl.setAnnotations(copyAnnotation(namedSetApi.annotations()));
        impl.setCaption(namedSetApi.caption());
        impl.setDescription(namedSetApi.description());
        impl.setDisplayFolder(namedSetApi.displayFolder());
        impl.setFormula(namedSetApi.formula());
        impl.setFormulaElement(copyFormula(namedSetApi.formulaElement()));
        impl.setName(namedSetApi.name());
        return impl;
    }

    private static MappingParameter copy(MappingParameter parameterApi) {
        ParameterImpl impl = new ParameterImpl();
        impl.setDefaultValue(parameterApi.defaultValue());
        impl.setDescription(parameterApi.description());
        impl.setModifiable(parameterApi.modifiable());
        impl.setName(parameterApi.name());
        impl.setType(parameterApi.type());
        return impl;
    }

}
