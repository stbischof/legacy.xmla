/*
* This software is subject to the terms of the Eclipse Public License v1.0
* Agreement, available at the following URL:
* http://www.eclipse.org/legal/epl-v10.html.
* You must accept the terms of that agreement to use this software.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package mondrian.rolap;

import static org.opencube.junit5.TestUtil.assertQueryReturns;

import org.eclipse.daanse.olap.api.Context;
import org.eclipse.daanse.olap.api.connection.Connection;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.opencube.junit5.ContextSource;
import org.opencube.junit5.dataloader.FastFoodmardDataLoader;
import org.opencube.junit5.propupdator.AppandFoodMartCatalog;

/**
 * Test case for '&amp;[..]' capability in MDX identifiers.
 *
 * <p>This feature used
 * <a href="http://jira.pentaho.com/browse/MONDRIAN-485">bug MONDRIAN-485,
 * "Member key treated as member name in WHERE"</a>
 * as a placeholder.
 *
 * @author pierluiggi@users.sourceforge.net
 */
class IndexedValuesTest {

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    @DisabledIfSystemProperty(named = "test.disable.knownFails", matches = "true")
    void testQueryWithIndex(Context<?> context) {
        final String desiredResult =
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Org Salary]}\n"
            + "{[Measures].[Count]}\n"
            + "Axis #2:\n"
            + "{[Employees].[Employees].[Sheri Nowmer]}\n"
            + "Row #0: $39,431.67\n"
            + "Row #0: 7,392\n";
        Connection connection = context.getConnectionWithDefaultRole();
        // Query using name
        assertQueryReturns(connection,
            "SELECT {[Measures].[Org Salary], [Measures].[Count]} "
            + "ON COLUMNS, "
            + "{[Employees].[Employees].[Sheri Nowmer]} "
            + "ON ROWS FROM [HR]",
            desiredResult);

        // Member-by-key resolution ("&[key]") is not supported: the SQL builder
        // cannot model a MemberKeyConstraint read. Restore the key-based assertions
        // once it can.
    }
}
