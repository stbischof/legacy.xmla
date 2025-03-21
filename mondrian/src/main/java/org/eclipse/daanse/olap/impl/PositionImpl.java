package org.eclipse.daanse.olap.impl;

import java.util.AbstractList;
import java.util.List;

import org.eclipse.daanse.olap.api.element.Member;
import org.eclipse.daanse.olap.api.result.Position;
import org.eclipse.daanse.olap.calc.api.todo.TupleList;

public class PositionImpl extends AbstractList<Member> implements Position {

    private final TupleList tupleList;
    private final int offset;

    PositionImpl(TupleList tupleList, int offset) {
        this.tupleList = tupleList;
        this.offset = offset;
    }

    @Override
    public Member get(int index) {
        return tupleList.get(index, offset);
    }

    @Override
    public int size() {
        return tupleList.getArity();
    }

    @Override
    public List<Member> getMembers() {
        return new AbstractList<>() {
            @Override
            public Member get(int slice) {
                return tupleList.get(slice, offset);
            }

            @Override
            public int size() {
                return tupleList.getArity();
            }
        };

    }
}
