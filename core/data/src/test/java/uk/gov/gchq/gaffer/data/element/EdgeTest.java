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

package uk.gov.gchq.gaffer.data.element;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.gchq.gaffer.commonutil.TestGroups;
import uk.gov.gchq.gaffer.commonutil.TestPropertyNames;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import java.util.Map.Entry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class EdgeTest extends ElementTest {

    @Test
    public void shouldSetAndGetFields() {
        // Given
        final Edge edge = TestElements.getDefaultEdge();

        // Then
        assertEquals("group", edge.getGroup());
        assertEquals("source vertex", edge.getSource());
        assertEquals("destination vertex", edge.getDestination());
        assertTrue(edge.isDirected());
    }

    @Test
    public void shouldBuildEdge() {
        // Given
        final String source = "source vertex";
        final String destination = "destination vertex";
        final boolean directed = true;
        final String propValue = "propValue";

        // When
        final Edge edge = new Edge.Builder()
                .group(TestGroups.EDGE)
                .source(source)
                .destination(destination)
                .directed(directed)
                .property(TestPropertyNames.STRING, propValue)
                .build();

        // Then
        assertEquals(TestGroups.EDGE, edge.getGroup());
        assertEquals(source, edge.getSource());
        assertEquals(destination, edge.getDestination());
        assertTrue(edge.isDirected());
        assertEquals(propValue, edge.getProperty(TestPropertyNames.STRING));
    }

    @Test
    public void shouldConstructEdge() {
        // Given
        final String source = "source vertex";
        final String destination = "destination vertex";
        final boolean directed = true;
        final String propValue = "propValue";

        // When
        final Edge edge = new Edge(TestGroups.EDGE, source, destination, directed);
        edge.putProperty(TestPropertyNames.STRING, propValue);

        // Then
        assertEquals(TestGroups.EDGE, edge.getGroup());
        assertEquals(source, edge.getSource());
        assertEquals(destination, edge.getDestination());
        assertTrue(edge.isDirected());
        assertEquals(propValue, edge.getProperty(TestPropertyNames.STRING));
    }

    @Test
    public void shouldCloneEdge() {
        // Given
        final String source = "source vertex";
        final String destination = "destination vertex";
        final boolean directed = true;
        final String propValue = "propValue";

        // When
        final Edge edge = new Edge(TestGroups.EDGE, source, destination, directed);
        final Edge clone = edge.emptyClone();

        // Then
        assertEquals(edge, clone);
    }

    @Test
    public void shouldReturnTrueForEqualsWithTheSameInstance() {
        // Given
        final Edge edge = TestElements.getDefaultEdge();

        // When
        boolean isEqual = edge.equals(edge);

        // Then
        assertTrue(isEqual);
        assertEquals(edge.hashCode(), edge.hashCode());
    }

    @Test
    public void shouldReturnTrueForEqualsWhenAllCoreFieldsAreEqual() {
        // Given
        final Edge edge1 = TestElements.getDefaultEdge();
        edge1.putProperty("some property", "some value");

        final Edge edge2 = cloneCoreFields(edge1);
        edge2.putProperty("some different property", "some other value");

        // When
        boolean isEqual = edge1.shallowEquals((Object) edge2);

        // Then
        assertTrue(isEqual);
        assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void shouldReturnTrueForEqualsWhenAllFieldsAreEqual() {
        // Given
        final Edge edge1 = TestElements.getDefaultEdge();
        edge1.putProperty("some property", "some value");

        final Edge edge2 = cloneCoreFields(edge1);
        edge2.putProperty("some property", "some value");

        // When
        boolean isEqual = edge1.shallowEquals((Object) edge2);

        // Then
        assertTrue(isEqual);
        assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void shouldReturnFalseForEqualsWhenGroupIsDifferent() {
        // Given
        final Edge edge1 = TestElements.getDefaultEdge();

        final Edge edge2 = new Edge("a different group", edge1.getSource(), edge1
                .getDestination(), edge1.isDirected());

        // When
        boolean isEqual = edge1.equals((Object) edge2);

        // Then
        assertFalse(isEqual);
        assertFalse(edge1.hashCode() == edge2.hashCode());
    }

    @Test
    public void shouldReturnFalseForEqualsWhenDirectedIsDifferent() {
        // Given
        final Edge edge1 = TestElements.getDefaultEdge();

        Edge edge2 = cloneCoreFields(edge1);
        edge2 = setDirected(edge2, !edge1.isDirected());

        // When
        boolean isEqual = edge1.equals((Object) edge2);

        // Then
        assertFalse(isEqual);
        assertFalse(edge1.hashCode() == edge2.hashCode());
    }

    @Test
    public void shouldReturnFalseForEqualsWhenSourceIsDifferent() {
        // Given
        final Edge edge1 = TestElements.getDefaultEdge();

        Edge edge2 = cloneCoreFields(edge1);
        edge2 = setSource(edge2, "different source");

        // When
        boolean isEqual = edge1.equals((Object) edge2);

        // Then
        assertFalse(isEqual);
        assertFalse(edge1.hashCode() == edge2.hashCode());
    }

    @Test
    public void shouldReturnFalseForEqualsWhenDestinationIsDifferent() {
        // Given
        final Edge edge1 = TestElements.getDefaultEdge();

        Edge edge2 = cloneCoreFields(edge1);
        edge2 = setDestination(edge2, "different destination vertex");

        // When
        boolean isEqual = edge1.equals((Object) edge2);

        // Then
        assertFalse(isEqual);
        assertFalse(edge1.hashCode() == edge2.hashCode());
    }

    @Test
    public void shouldReturnTrueForEqualsWhenUndirectedIdentifiersFlipped() {
        // Given
        final Edge edge1 = new Edge("group", "source vertex", "destination vertex", false);

        // Given
        final Edge edge2 = new Edge("group", "destination vertex", "source vertex", false);

        // When
        boolean isEqual = edge1.equals((Object) edge2);

        // Then
        assertTrue(isEqual);
        assertTrue(edge1.hashCode() == edge2.hashCode());
    }

    @Test
    public void shouldReturnFalseForEqualsWhenDirectedIdentifiersFlipped() {
        // Given
        final Edge edge1 = new Edge("group", "source vertex", "destination vertex", true);

        // Given
        final Edge edge2 = new Edge("group", "destination vertex", "source vertex", true);

        // When
        boolean isEqual = edge1.equals((Object) edge2);

        // Then
        assertFalse(isEqual);
        assertFalse(edge1.hashCode() == edge2.hashCode());
    }

    @Test
    public void shouldCreateUndirectedEdgesUsingNaturalOrdering() {
        // Given
        final Edge edge1 = new Edge("group", "source vertex", "destination vertex", false);

        // Given
        final Edge edge2 = new Edge("group", "destination vertex", "source vertex", false);

        // Then
        assertEquals(edge1, edge2);
        assertEquals(edge1.toString(), edge2.toString());
    }

    @Test
    public void shouldSerialiseAndDeserialiseIdentifiers() throws SerialisationException {
        // Given
        final Edge edge = new Edge("group", 1L, 2L, true);

        final JSONSerialiser serialiser = new JSONSerialiser();

        // When
        final byte[] serialisedElement = serialiser.serialise(edge);
        final Edge deserialisedElement = serialiser.deserialise(serialisedElement, edge.getClass());

        // Then
        assertEquals(edge, deserialisedElement);
    }

    @Test
    public void shouldCreateUndirectedEdgesWithNaturalOrdering() {
        // Given
        final Edge edge1 = new Edge("group", "B", "A", false);
        final Edge edge2 = new Edge("group", 2, 1, false);

        // Then
        assertThat(edge1.getSource(), is("A"));
        assertThat(edge2.getSource(), is(1));

        assertThat(edge1.getDestination(), is("B"));
        assertThat(edge2.getDestination(), is(2));
    }

    @Override
    protected Edge newElement(final String group) {
        return new Edge.Builder().group(group)
                                 .source("source vertex")
                                 .destination("destination vertex")
                                 .directed(true)
                                 .build();
    }

    @Override
    protected Edge newElement() {
        return new Edge.Builder().source("source vertex")
                                 .destination("destination vertex")
                                 .directed(true)
                                 .build();
    }

    private Edge cloneCoreFields(final Edge edge) {
        return new Edge(edge.getGroup(), edge.getSource(), edge.getDestination(), edge
                .isDirected());
    }

    private Edge cloneAllFields(final Edge edge) {
        final Edge newEdge = cloneCoreFields(edge);

        final Properties properties = edge.getProperties();
        for (final Entry<String, Object> entry : properties.entrySet()) {
            newEdge.putProperty(entry.getKey(), entry.getValue());
        }

        return newEdge;
    }

    private Edge setSource(final Edge edge, final Object source) {
        return new Edge.Builder().group(edge.getGroup())
                                 .source(source)
                                 .destination(edge.getDestination())
                                 .directed(edge.isDirected())
                                 .properties(edge.getProperties())
                                 .build();
    }

    private Edge setDestination(final Edge edge, final Object destination) {
        return new Edge.Builder().group(edge.getGroup())
                                 .source(edge.getDestination())
                                 .destination(destination)
                                 .directed(edge.isDirected())
                                 .properties(edge.getProperties())
                                 .build();
    }

    private Edge setDirected(final Edge edge, final boolean directed) {
        return new Edge.Builder().group(edge.getGroup())
                                 .source(edge.getDestination())
                                 .destination(edge.getDestination())
                                 .directed(directed)
                                 .properties(edge.getProperties())
                                 .build();
    }
}
