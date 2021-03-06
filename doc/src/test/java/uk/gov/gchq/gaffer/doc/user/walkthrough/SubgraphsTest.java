/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.gaffer.doc.user.walkthrough;

import com.google.common.collect.Lists;
import org.apache.commons.lang.time.DateUtils;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.operation.OperationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SubgraphsTest {
    @Test
    public void shouldReturnExpectedEdges() throws OperationException, IOException {
        // Given
        final Subgraphs query = new Subgraphs();

        // When
        final Iterable<? extends Element> results = query.run();

        // Then
        verifyResults(results);
    }

    private void verifyResults(final Iterable<? extends Element> resultsItr) {
        final Edge[] expectedResults = {
                new Edge.Builder()
                        .group("RoadHasJunction")
                        .source("M5")
                        .dest("10")
                        .directed(true)
                        .build(),
                new Edge.Builder()
                        .group("RoadHasJunction")
                        .source("M5")
                        .dest("11")
                        .directed(true)
                        .build(),
                new Edge.Builder()
                        .group("RoadHasJunction")
                        .source("M5")
                        .dest("23")
                        .directed(true)
                        .build(),
                new Edge.Builder()
                        .group("RoadHasJunction")
                        .source("M5")
                        .dest("24")
                        .directed(true)
                        .build(),
                new Edge.Builder()
                        .group("RoadHasJunction")
                        .source("M5")
                        .dest("27")
                        .directed(true)
                        .build(),
                new Edge.Builder()
                        .group("RoadHasJunction")
                        .source("M5")
                        .dest("28")
                        .directed(true)
                        .build(),

                new Edge.Builder()
                        .group("RoadUse")
                        .source("10")
                        .dest("11")
                        .directed(true)
                        .property("startDate", getDate("2000-05-01"))
                        .property("endDate", DateUtils.addMilliseconds(getDate("2000-05-02"), -1))
                        .property("count", 2L)
                        .build(),
                new Edge.Builder()
                        .group("RoadUse")
                        .source("23")
                        .dest("24")
                        .directed(true)
                        .property("startDate", getDate("2000-05-04"))
                        .property("endDate", DateUtils.addMilliseconds(getDate("2000-05-05"), -1))
                        .property("count", 2L)
                        .build()
        };

        final List<Element> results = Lists.newArrayList(resultsItr);
        assertEquals(expectedResults.length, results.size());
        assertThat(results, IsCollectionContaining.hasItems(expectedResults));
    }

    private static Date getDate(final String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Unable to parse date", e);
        }
    }
}