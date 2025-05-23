/*
 *
 * This software is subject to the terms of the Eclipse Public License v1.0
 * Agreement, available at the following URL:
 * http://www.eclipse.org/legal/epl-v10.html.
 * You must accept the terms of that agreement to use this software.
 *
 * Copyright (C) 2001-2005 Julian Hyde
 * Copyright (C) 2005-2020 Hitachi Vantara and others
 * All Rights Reserved.
 *
 *
 */

package mondrian.olap.fun.sort;

import org.eclipse.daanse.olap.api.calc.Calc;

public class SortKeySpec {
  private final Calc key;
  private final Sorter.SorterFlag direction;

  public SortKeySpec( Calc key, Sorter.SorterFlag dir ) {
    this.key = key;
    this.direction = dir;
  }

  public Calc getKey() {
    return this.key;
  }

  public Sorter.SorterFlag getDirection() {
    return this.direction;
  }
}
