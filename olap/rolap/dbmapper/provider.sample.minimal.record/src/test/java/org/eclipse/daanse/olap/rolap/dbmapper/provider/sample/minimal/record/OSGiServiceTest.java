package org.eclipse.daanse.olap.rolap.dbmapper.provider.sample.minimal.record;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingSchema;
import org.eclipse.daanse.olap.rolap.dbmapper.provider.api.DatabaseMappingSchemaProvider;
import org.osgi.service.cm.annotations.RequireConfigurationAdmin;
import org.osgi.service.component.annotations.RequireServiceComponentRuntime;
import org.osgi.test.common.annotation.InjectService;
@RequireConfigurationAdmin
@RequireServiceComponentRuntime
class OSGiServiceTest {

    @org.junit.jupiter.api.Test
    void testDbMappingSchemaProvider(@InjectService(timeout = 1000) DatabaseMappingSchemaProvider provider) throws Exception {
        MappingSchema schema = provider.get();

        assertThat(schema.name()).isNotNull()
                .isEqualTo("Minimal");
    }

}
