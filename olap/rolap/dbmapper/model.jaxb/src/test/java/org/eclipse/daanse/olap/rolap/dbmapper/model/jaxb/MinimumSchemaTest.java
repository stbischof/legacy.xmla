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
package org.eclipse.daanse.olap.rolap.dbmapper.model.jaxb;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

class MinimumSchemaTest {

	public static String MINIMUM_SCHEMA = """
			<Schema name="MinimumSchema">
			  <Cube name="OnlyCube">
			    <Table name="OnlyFactTable"></Table>
			    <Measure name="OnlyMeaseure" column="onlyColumn" aggregator="sum"></Measure>
			  </Cube>
			</Schema>
			""";

	@org.junit.jupiter.api.Test
	void testMinimumXml() throws Exception {

		SchemaImpl schema = extracted(MINIMUM_SCHEMA);
		assertThat(schema).isNotNull();
	}

	static SchemaImpl extracted(String xml) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(SchemaImpl.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (SchemaImpl) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

	}
}
