/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
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
package org.eclipse.daanse.olap.function.def.nativizeset;

import java.util.List;

import org.eclipse.daanse.mdx.model.api.expression.operation.FunctionOperationAtom;
import org.eclipse.daanse.olap.api.DataType;
import org.eclipse.daanse.olap.api.function.FunctionDefinition;
import org.eclipse.daanse.olap.api.function.FunctionMetaData;
import org.eclipse.daanse.olap.api.function.FunctionResolver;
import org.eclipse.daanse.olap.api.query.component.Expression;
import org.eclipse.daanse.olap.function.core.FunctionMetaDataR;
import org.eclipse.daanse.olap.function.core.FunctionParameterR;
import org.eclipse.daanse.olap.function.core.resolver.AbstractFunctionDefinitionMultiResolver;
import org.osgi.service.component.annotations.Component;

@Component(service = FunctionResolver.class)
public class NativizeSetResolver  extends AbstractFunctionDefinitionMultiResolver {
    private static FunctionOperationAtom atom = new FunctionOperationAtom("NativizeSet");
    private static String SIGNATURE = "NativizeSet(<Set>)";
    private static String DESCRIPTION = "Tries to natively evaluate <Set>.";
    private static FunctionParameterR[] x = { new FunctionParameterR(DataType.SET) };
    // {"fxx"}

    private static FunctionMetaData functionMetaData = new FunctionMetaDataR(atom, DESCRIPTION, SIGNATURE,
            DataType.SET, x);

    public NativizeSetResolver() {
        super(List.of(new NativizeSetFunDef(functionMetaData)));
    }
    
    @Override
    protected FunctionDefinition createFunDef(Expression[] args, FunctionMetaData fmd,
            FunctionMetaData fmdTarget) {
        if (functionMetaData.equals(fmd)) {
            return new NativizeSetFunDef(functionMetaData);
        }
        return null;
    }


}