/*
 * Copyright © 2009-2016 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.service.model;

import org.apromore.test.heuristic.JavaBeanHeuristic;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test the Shared Fragment Version POJO.
 * @author <a href="mailto:cam.james@gmail.com">Cameron James</a>
 */
public class SharedFragmentVersionUnitTest {

    @Test
    public void testLikeJavaBean() {
        JavaBeanHeuristic.assertLooksLikeJavaBean(SharedFragmentVersion.class);
    }


    @Test
    public void basicBean() {
        int fragmentId = 123;
        int numOfUses = 2;

        SharedFragmentVersion sfv = new SharedFragmentVersion(fragmentId, numOfUses);
        assertThat(sfv.getFragmentVersionid(), equalTo(fragmentId));
        assertThat(sfv.getNumberOfUses(), equalTo(numOfUses));

        sfv = new SharedFragmentVersion(String.valueOf(fragmentId), Long.valueOf(numOfUses));
        assertThat(sfv.getFragmentVersionid(), equalTo(fragmentId));
        assertThat(sfv.getNumberOfUses(), equalTo(numOfUses));
    }
}
