// Generated by delombok at Fri Mar 08 16:24:33 MST 2024
/* Copyright 2014-2023 Norconex Inc.
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
package com.norconex.crawler.core.filter.impl;

import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.norconex.commons.lang.config.Configurable;
import com.norconex.commons.lang.map.Properties;
import com.norconex.crawler.core.filter.DocumentFilter;
import com.norconex.crawler.core.filter.MetadataFilter;
import com.norconex.crawler.core.filter.OnMatch;
import com.norconex.crawler.core.filter.OnMatchFilter;
import com.norconex.crawler.core.filter.ReferenceFilter;
import com.norconex.importer.doc.Doc;

/**
 * <p>
 * Filters URL based on a matching expression.
 * </p>
 *
 * {@nx.xml.usage
 * <filter class="com.norconex.crawler.core.filter.impl.GenericReferenceFilter"
 *     onMatch="[include|exclude]">
 *   <valueMatcher {@nx.include com.norconex.commons.lang.text.TextMatcher#matchAttributes}>
 *     (Expression matching the document reference.)
 *   </valueMatcher>
 * </filter>
 * }
 *
 * {@nx.xml.example
 * <filter class="GenericReferenceFilter" onMatch="exclude">
 *   <valueMatcher method="regex">.*&#47;login/.*</valueMatcher>
 * </filter>
 * }
 * <p>
 * The above will reject documents having "/login/" in their reference.
 * </p>
 * @see Pattern
 */
@SuppressWarnings("javadoc")
public class GenericReferenceFilter implements OnMatchFilter, ReferenceFilter, DocumentFilter, MetadataFilter, Configurable<GenericReferenceFilterConfig> {
    private final GenericReferenceFilterConfig configuration = new GenericReferenceFilterConfig();

    @Override
    public OnMatch getOnMatch() {
        return OnMatch.includeIfNull(configuration.getOnMatch());
    }

    @Override
    public boolean acceptReference(String reference) {
        var isInclude = getOnMatch() == OnMatch.INCLUDE;
        if (StringUtils.isBlank(configuration.getValueMatcher().getPattern())) {
            return isInclude;
        }
        var matches = configuration.getValueMatcher().matches(reference);
        return matches == isInclude;
    }

    @Override
    public boolean acceptDocument(Doc document) {
        return acceptReference(document.getReference());
    }

    @Override
    public boolean acceptMetadata(String reference, Properties metadata) {
        return acceptReference(reference);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof GenericReferenceFilter)) return false;
        final GenericReferenceFilter other = (GenericReferenceFilter) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$configuration = this.getConfiguration();
        final java.lang.Object other$configuration = other.getConfiguration();
        if (this$configuration == null ? other$configuration != null : !this$configuration.equals(other$configuration)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof GenericReferenceFilter;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $configuration = this.getConfiguration();
        result = result * PRIME + ($configuration == null ? 43 : $configuration.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "GenericReferenceFilter(configuration=" + this.getConfiguration() + ")";
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public GenericReferenceFilterConfig getConfiguration() {
        return this.configuration;
    }
}
