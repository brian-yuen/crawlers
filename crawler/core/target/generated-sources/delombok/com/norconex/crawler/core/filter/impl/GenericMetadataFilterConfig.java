// Generated by delombok at Fri Mar 08 16:24:33 MST 2024
/* Copyright 2021-2023 Norconex Inc.
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

import com.norconex.commons.lang.text.TextMatcher;
import com.norconex.crawler.core.filter.OnMatch;

/**
 * <p>
 * Accepts or rejects a reference based on whether one or more
 * metadata field values are matching.
 * </p>
 *
 * {@nx.xml.usage
 * <filter class="com.norconex.crawler.core.filter.impl.GenericMetadataFilter"
 *     onMatch="[include|exclude]">
 *   <fieldMatcher {@nx.include com.norconex.commons.lang.text.TextMatcher#matchAttributes}>
 *     (Expression matching one or more fields to evaluate.)
 *   </fieldMatcher>
 *   <valueMatcher {@nx.include com.norconex.commons.lang.text.TextMatcher#matchAttributes}>
 *     (Expression matching one or more values from matching fields.)
 *   </valueMatcher>
 * </filter>
 * }
 *
 * {@nx.xml.example
 * <filter class="GenericMetadataFilter" onMatch="exclude">
 *   <fieldMatcher>Content-Type</fieldMatcher>
 *   <valueMatcher>application/zip</valueMatcher>
 * </filter>
 * }
 * <p>
 * Used in a web context, the above example filters out Zip documents base
 * on a "Content-Type" metadata field.
 * </p>
 */
@SuppressWarnings("javadoc")
public class GenericMetadataFilterConfig {
    private OnMatch onMatch;
    private final TextMatcher fieldMatcher = new TextMatcher();
    private final TextMatcher valueMatcher = new TextMatcher();

    /**
     * Gets the field matcher.
     * @return field matcher
     */
    public TextMatcher getFieldMatcher() {
        return fieldMatcher;
    }

    /**
     * Sets the field matcher.
     * @param fieldMatcher field matcher
     * @return this instance
     */
    public GenericMetadataFilterConfig setFieldMatcher(TextMatcher fieldMatcher) {
        this.fieldMatcher.copyFrom(fieldMatcher);
        return this;
    }

    /**
     * Gets the value matcher.
     * @return value matcher
     */
    public TextMatcher getValueMatcher() {
        return valueMatcher;
    }

    /**
     * Sets the value matcher.
     * @param valueMatcher value matcher
     * @return this instance
     */
    public GenericMetadataFilterConfig setValueMatcher(TextMatcher valueMatcher) {
        this.valueMatcher.copyFrom(valueMatcher);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public GenericMetadataFilterConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public OnMatch getOnMatch() {
        return this.onMatch;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public GenericMetadataFilterConfig setOnMatch(final OnMatch onMatch) {
        this.onMatch = onMatch;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof GenericMetadataFilterConfig)) return false;
        final GenericMetadataFilterConfig other = (GenericMetadataFilterConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$onMatch = this.getOnMatch();
        final java.lang.Object other$onMatch = other.getOnMatch();
        if (this$onMatch == null ? other$onMatch != null : !this$onMatch.equals(other$onMatch)) return false;
        final java.lang.Object this$fieldMatcher = this.getFieldMatcher();
        final java.lang.Object other$fieldMatcher = other.getFieldMatcher();
        if (this$fieldMatcher == null ? other$fieldMatcher != null : !this$fieldMatcher.equals(other$fieldMatcher)) return false;
        final java.lang.Object this$valueMatcher = this.getValueMatcher();
        final java.lang.Object other$valueMatcher = other.getValueMatcher();
        if (this$valueMatcher == null ? other$valueMatcher != null : !this$valueMatcher.equals(other$valueMatcher)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof GenericMetadataFilterConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $onMatch = this.getOnMatch();
        result = result * PRIME + ($onMatch == null ? 43 : $onMatch.hashCode());
        final java.lang.Object $fieldMatcher = this.getFieldMatcher();
        result = result * PRIME + ($fieldMatcher == null ? 43 : $fieldMatcher.hashCode());
        final java.lang.Object $valueMatcher = this.getValueMatcher();
        result = result * PRIME + ($valueMatcher == null ? 43 : $valueMatcher.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "GenericMetadataFilterConfig(onMatch=" + this.getOnMatch() + ", fieldMatcher=" + this.getFieldMatcher() + ", valueMatcher=" + this.getValueMatcher() + ")";
    }
}
