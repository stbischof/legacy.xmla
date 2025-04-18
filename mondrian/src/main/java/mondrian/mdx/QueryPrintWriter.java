/*
* This software is subject to the terms of the Eclipse Public License v1.0
* Agreement, available at the following URL:
* http://www.eclipse.org/legal/epl-v10.html.
* You must accept the terms of that agreement to use this software.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package mondrian.mdx;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.daanse.olap.api.Parameter;

/**
 * PrintWriter used for unparsing queries. Remembers which parameters have
 * been printed. The first time, they print themselves as "Parameter";
 * subsequent times as "ParamRef".
 */
public class QueryPrintWriter extends PrintWriter {
    final Set<Parameter> parameters = new HashSet<>();

    public QueryPrintWriter(Writer writer) {
        super(writer);
    }
}
