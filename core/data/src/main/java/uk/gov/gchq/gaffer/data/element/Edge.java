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


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Edge.Builder;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * An <code>Edge</code> in an {@link uk.gov.gchq.gaffer.data.element.Element} containing a source, destination and a directed flag.
 * The source and destination vertices can be any type of {@link java.lang.Object}.
 * There is no requirement for these vertices to connect to an {@link uk.gov.gchq.gaffer.data.element.Entity} vertex -
 * for example you could have a 'graph' of just edges.
 * Edges are designed so that multiple edges can share the same identifiers but are distinguished via their
 * group.
 *
 * @see uk.gov.gchq.gaffer.data.element.Edge.Builder
 */
@JsonDeserialize(builder = Builder.class)
public class Edge extends Element {
    private static final Logger LOGGER = LoggerFactory.getLogger(Edge.class);
    private static final long serialVersionUID = -5596452468277807842L;
    private Object source;
    private Object destination;
    private boolean directed;

    public Edge(final String group, final Object source, final Object destination, final boolean directed) {
        super(group);
        this.source = source;
        this.destination = destination;
        this.directed = directed;
        standardise();
    }

    private Edge(final Builder builder) {
        super(builder.group);
        this.source = builder.source;
        this.destination = builder.destination;
        this.directed = builder.directed;

        if (null != builder.properties) {
            builder.properties.forEach((k, v) -> putProperty(k, v));
        }

        standardise();
    }

    @JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_OBJECT)
    public Object getSource() {
        return source;
    }

    @JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_OBJECT)
    public Object getDestination() {
        return destination;
    }

    public boolean isDirected() {
        return directed;
    }

    @Override
    public Object getIdentifier(final IdentifierType identifierType) {
        switch (identifierType) {
            case SOURCE:
                return getSource();
            case DESTINATION:
                return getDestination();
            case DIRECTED:
                return isDirected();
            default:
                LOGGER.error("Unknown identifier type: " + identifierType + " detected.");
                return null;
        }
    }

    @Override
    public void putIdentifier(final IdentifierType identifierType, final Object propertyToBeSet) {
        switch (identifierType) {
            case SOURCE:
                this.source = propertyToBeSet;
                break;
            case DESTINATION:
                this.destination = propertyToBeSet;
                break;
            case DIRECTED:
                this.directed = (boolean) propertyToBeSet;
                break;
            default:
                LOGGER.error("Unknown identifier type: " + identifierType + " detected.");
        }
    }

    private void standardise() {
        if (!directed) {
            if (null != destination && null != source) {
                if ((source instanceof Comparable) && (destination instanceof Comparable)) {
                    if (((Comparable) destination).compareTo((Comparable) source) < 0) {
                        final Object tmp = destination;
                        destination = source;
                        source = tmp;
                    } else {
                        if (destination.toString()
                                       .compareTo(source.toString()) < 0) {
                            final Object tmp = destination;
                            destination = source;
                            source = tmp;
                        }
                    }
                }
            }
        }
    }

    public int hashCode() {
        int hash;
        if (directed) {
            hash = new HashCodeBuilder(21, 3)
                    .appendSuper(super.hashCode())
                    .append(source)
                    .append(destination)
                    .append(directed)
                    .toHashCode();
        } else {
            hash = super.hashCode();
            hash ^= source.hashCode();
            hash ^= destination.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        return null != obj
                && (obj instanceof Edge)
                && equals((Edge) obj);
    }

    public boolean equals(final Edge edge) {
        return null != edge
                && (new EqualsBuilder()
                .appendSuper(super.equals(edge))
                .append(source, edge.getSource())
                .append(destination, edge.getDestination())
                .append(directed, edge.isDirected())
                .isEquals()
                || new EqualsBuilder()
                .appendSuper(super.equals(edge))
                .append(source, edge.getDestination())
                .append(destination, edge.getSource())
                .append(directed, false)
                .isEquals()
        );
    }

    @Override
    public Edge emptyClone() {
        return new Edge(
                this.getGroup(),
                this.getSource(),
                this.getDestination(),
                this.isDirected()
        );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("source", source)
                .append("destination", destination)
                .append("directed", directed)
                .appendSuper(super.toString())
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {

        private Properties properties;
        private String group;
        private Object source;
        private Object destination;
        private boolean directed;

        public Builder() {
            // Empty
        }

        @JsonProperty("class")
        public Builder clazz(final String clazz) {
            // Required to consume "class" field added by JsonTypeInfo annotation
            return this;
        }

        public Builder group(final String group) {
            this.group = group;
            return this;
        }

        @JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_OBJECT)
        public Builder source(final Object source) {
            this.source = source;
            return this;
        }

        @JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_OBJECT)
        public Builder destination(final Object destination) {
            this.destination = destination;
            return this;
        }

        public Builder directed(final boolean directed) {
            this.directed = directed;
            return this;
        }

        public Builder property(final String name, final Object value) {
            if (null == properties) {
                this.properties = new Properties();
            }
            this.properties.put(name, value);
            return this;
        }

        @JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_OBJECT)
        public Builder properties(final Properties properties) {
            this.properties = properties;
            return this;
        }

        public Edge build() {
            return new Edge(this);
        }
    }
}

