/*
* This software is subject to the terms of the Eclipse Public License v1.0
* Agreement, available at the following URL:
* http://www.eclipse.org/legal/epl-v10.html.
* You must accept the terms of that agreement to use this software.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package org.eclipse.daanse.db.dialect.db.postgresql;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.daanse.db.dialect.api.BestFitColumnType;
import org.eclipse.daanse.db.dialect.api.Dialect;
import org.eclipse.daanse.db.dialect.db.common.DialectUtil;
import org.eclipse.daanse.db.dialect.db.common.JdbcDialectImpl;

/**
 * Implementation of {@link Dialect} for the PostgreSQL database.
 *
 * @author jhyde
 * @since Nov 23, 2008
 */
public class PostgreSqlDialect extends JdbcDialectImpl {

    public PostgreSqlDialect(Connection connection) {
        super(connection);
    }

    @Override
    public boolean requiresAliasForFromQuery() {
        return true;
    }

    @Override
    protected StringBuilder generateOrderByNulls(CharSequence expr, boolean ascending, boolean collateNullsLast) {
        // Support for "ORDER BY ... NULLS LAST" was introduced in Postgres 8.3.
        if (productVersion.compareTo("8.3") >= 0) {
            return generateOrderByNullsAnsi(expr, ascending, collateNullsLast);
        } else {
            return super.generateOrderByNulls(expr, ascending, collateNullsLast);
        }
    }

    @Override
    public boolean allowsRegularExpressionInWhereClause() {
        return true;
    }

    @Override
	public StringBuilder generateRegularExpression(String source, String javaRegex) {
        try {
            Pattern.compile(javaRegex);
        } catch (PatternSyntaxException e) {
            // Not a valid Java regex. Too risky to continue.
            return null;
        }
        javaRegex = DialectUtil.cleanUnicodeAwareCaseFlag(javaRegex);
        javaRegex = javaRegex.replace("\\Q", "");
        javaRegex = javaRegex.replace("\\E", "");
        final StringBuilder sb = new StringBuilder();
        sb.append("cast(");
        sb.append(source);
        sb.append(" as text) is not null and ");
        sb.append("cast(");
        sb.append(source);
        sb.append(" as text) ~ ");
        quoteStringLiteral(sb, javaRegex);
        return sb;
    }

    @Override
    public BestFitColumnType getType(ResultSetMetaData metaData, int columnIndex) throws SQLException {
        final int precision = metaData.getPrecision(columnIndex + 1);
        final int scale = metaData.getScale(columnIndex + 1);
        final int columnType = metaData.getColumnType(columnIndex + 1);
        final String columnName = metaData.getColumnName(columnIndex + 1);

        // TODO - Do we need the check for "m"??
        if (columnType == Types.NUMERIC && scale == 0 && precision == 0 && columnName.startsWith("m")) {
            // In Greenplum NUMBER/NUMERIC w/ no precision or
            // scale means floating point.
            logTypeInfo(metaData, columnIndex, BestFitColumnType.OBJECT);
            return BestFitColumnType.OBJECT; // TODO - can this be DOUBLE?
        }
        return super.getType(metaData, columnIndex);
    }

    @Override
    public String getDialectName() {
        return "postgres";
    }

}
