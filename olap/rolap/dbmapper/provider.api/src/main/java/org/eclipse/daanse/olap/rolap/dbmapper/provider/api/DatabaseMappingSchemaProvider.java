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
package org.eclipse.daanse.olap.rolap.dbmapper.provider.api;

import java.util.function.Supplier;

import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingSchema;

/*
 * Provides a Schema
 */
public interface DatabaseMappingSchemaProvider extends Supplier<MappingSchema> {

	/*
	 * Provides a Schema.
	 */
	@Override
	MappingSchema get();

}