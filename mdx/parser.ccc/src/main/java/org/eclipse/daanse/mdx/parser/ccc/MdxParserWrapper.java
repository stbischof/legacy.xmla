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
package org.eclipse.daanse.mdx.parser.ccc;

import org.eclipse.daanse.mdx.model.MdxStatement;
import org.eclipse.daanse.mdx.model.SelectStatement;
import org.eclipse.daanse.mdx.model.select.SelectQueryAsteriskClause;
import org.eclipse.daanse.mdx.model.select.SelectQueryAxesClause;
import org.eclipse.daanse.mdx.parser.api.MdxParser;
import org.eclipse.daanse.mdx.parser.api.MdxParserException;

public class MdxParserWrapper implements MdxParser {
    private MDXParser delegate;

    public MdxParserWrapper(CharSequence mdx) throws MdxParserException {

        if (mdx == null) {

            throw new MdxParserException("statement must not be null");
        } else if (mdx.length() == 0) {

            throw new MdxParserException("statement must not be empty");
        }
        try {
            delegate = new MDXParser(mdx);
        } catch (Exception e) {

            throw new MdxParserException("statement must not be empty");
        }
    }

    @Override
    public MdxStatement parseMdxStatement() throws MdxParserException {
        try {
            return delegate.parseMdxStatement();

        } catch (Throwable e) {
            throw new MdxParserException(e);
        } finally {
            dump();
        }

    }

    private void dump() {
        Node root = delegate.rootNode();
        if (root != null) {
            System.out.println("Dumping the AST...");
            root.dump();
        }
    }

    @Override
    public SelectQueryAsteriskClause parseSelectQueryAsteriskClause() throws MdxParserException {
        try {
            return delegate.parseSelectQueryAsteriskClause();

        } catch (Throwable e) {
            throw new MdxParserException(e);
        } finally {
            dump();
        }

    }

    @Override
    public SelectStatement parseSelectStatement() throws MdxParserException {
        try {
            return delegate.parseSelectStatement();

        } catch (Throwable e) {
            throw new MdxParserException(e);
        } finally {
            dump();
        }
    }

    @Override
    public SelectQueryAxesClause parseSelectQueryAxesClause() throws MdxParserException {
        try {
            return delegate.parseSelectQueryAxesClause();

        } catch (Throwable e) {
            throw new MdxParserException(e);
        } finally {
            dump();
        }
    }

}