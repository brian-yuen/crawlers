// Generated by delombok at Fri Mar 08 16:25:40 MST 2024
/* Copyright 2017-2023 Norconex Inc.
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
package com.norconex.importer.handler.transformer.impl;

import com.norconex.commons.lang.map.PropertySetter;
import com.norconex.commons.lang.text.TextMatcher;

/**
 * <p>
 * Truncates a <code>fromField</code> value(s) and optionally replace truncated
 * portion by a hash value to help ensure uniqueness (not 100% guaranteed to
 * be collision-free).  If the field to truncate has multiple values, all
 * values will be subject to truncation. You can store the value(s), truncated
 * or not, in another target field.
 * </p>
 * <h3>Storing values in an existing field</h3>
 * <p>
 * If a target field with the same name already exists for a document,
 * values will be added to the end of the existing value list.
 * It is possible to change this default behavior by supplying a
 * {@link PropertySetter}.
 * </p>
 * <p>
 * The <code>maxLength</code> is guaranteed to be respected. This means any
 * appended hash code and suffix will fit within the <code>maxLength</code>.
 * </p>
 * <p>
 * Can be used both as a pre-parse or post-parse handler.
 * </p>
 *
 * {@nx.xml.usage
 * <handler class="com.norconex.importer.handler.tagger.impl.TruncateTagger"
 *     maxLength="(maximum length)"
 *     toField="(optional target field where to store the truncated value)"
 *     {@nx.include com.norconex.commons.lang.map.PropertySetter#attributes}
 *     appendHash="[false|true]"
 *     suffix="(value to append after truncation. Goes before hash if one.)">
 *
 *   {@nx.include com.norconex.importer.handler.AbstractImporterHandler#restrictTo}
 *
 *   <fieldMatcher {@nx.include com.norconex.commons.lang.text.TextMatcher#matchAttributes}>
 *     (one or more matching fields to have their values truncated)
 *   </fieldMatcher>
 * </handler>
 * }
 *
 * {@nx.xml.example
 * <handler class="TruncateTagger"
 *     maxLength="50"
 *     appendHash="true"
 *     suffix="!">
 *   <fieldMatcher>myField</fieldMatcher>
 * </handler>
 * }
 *
 * <p>
 * Assuming this "myField" value...
 * </p>
 * <pre>    Please truncate me before you start thinking I am too long.</pre>
 * <p>
 * ...the above example will truncate it to...
 * </p>
 * <pre>    Please truncate me before you start thi!0996700004</pre>
 */
@SuppressWarnings("javadoc")
public class TruncateTransformerConfig {
    private final TextMatcher fieldMatcher = new TextMatcher();
    private int maxLength;
    private String toField;
    /**
     * The property setter to use when a value is set.
     */
    private PropertySetter onSet;
    /**
     * Whether to apply the original string hash code at the end of the
     * truncation to help ensure uniqueness (no guarantee).
     */
    private boolean appendHash;
    private String suffix;

    /**
     * Gets field matcher for fields to truncate.
     * @return field matcher
     */
    public TextMatcher getFieldMatcher() {
        return fieldMatcher;
    }

    /**
     * Sets the field matcher for fields to truncate.
     * @param fieldMatcher field matcher
     */
    public TruncateTransformerConfig setFieldMatcher(TextMatcher fieldMatcher) {
        this.fieldMatcher.copyFrom(fieldMatcher);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TruncateTransformerConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int getMaxLength() {
        return this.maxLength;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getToField() {
        return this.toField;
    }

    /**
     * The property setter to use when a value is set.
     * @return property setter
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public PropertySetter getOnSet() {
        return this.onSet;
    }

    /**
     * Whether to apply the original string hash code at the end of the
     * truncation to help ensure uniqueness (no guarantee).
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isAppendHash() {
        return this.appendHash;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getSuffix() {
        return this.suffix;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TruncateTransformerConfig setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TruncateTransformerConfig setToField(final String toField) {
        this.toField = toField;
        return this;
    }

    /**
     * The property setter to use when a value is set.
     * @param onSet property setter
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TruncateTransformerConfig setOnSet(final PropertySetter onSet) {
        this.onSet = onSet;
        return this;
    }

    /**
     * Whether to apply the original string hash code at the end of the
     * truncation to help ensure uniqueness (no guarantee).
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TruncateTransformerConfig setAppendHash(final boolean appendHash) {
        this.appendHash = appendHash;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TruncateTransformerConfig setSuffix(final String suffix) {
        this.suffix = suffix;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof TruncateTransformerConfig)) return false;
        final TruncateTransformerConfig other = (TruncateTransformerConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.getMaxLength() != other.getMaxLength()) return false;
        if (this.isAppendHash() != other.isAppendHash()) return false;
        final java.lang.Object this$fieldMatcher = this.getFieldMatcher();
        final java.lang.Object other$fieldMatcher = other.getFieldMatcher();
        if (this$fieldMatcher == null ? other$fieldMatcher != null : !this$fieldMatcher.equals(other$fieldMatcher)) return false;
        final java.lang.Object this$toField = this.getToField();
        final java.lang.Object other$toField = other.getToField();
        if (this$toField == null ? other$toField != null : !this$toField.equals(other$toField)) return false;
        final java.lang.Object this$onSet = this.getOnSet();
        final java.lang.Object other$onSet = other.getOnSet();
        if (this$onSet == null ? other$onSet != null : !this$onSet.equals(other$onSet)) return false;
        final java.lang.Object this$suffix = this.getSuffix();
        final java.lang.Object other$suffix = other.getSuffix();
        if (this$suffix == null ? other$suffix != null : !this$suffix.equals(other$suffix)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof TruncateTransformerConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getMaxLength();
        result = result * PRIME + (this.isAppendHash() ? 79 : 97);
        final java.lang.Object $fieldMatcher = this.getFieldMatcher();
        result = result * PRIME + ($fieldMatcher == null ? 43 : $fieldMatcher.hashCode());
        final java.lang.Object $toField = this.getToField();
        result = result * PRIME + ($toField == null ? 43 : $toField.hashCode());
        final java.lang.Object $onSet = this.getOnSet();
        result = result * PRIME + ($onSet == null ? 43 : $onSet.hashCode());
        final java.lang.Object $suffix = this.getSuffix();
        result = result * PRIME + ($suffix == null ? 43 : $suffix.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "TruncateTransformerConfig(fieldMatcher=" + this.getFieldMatcher() + ", maxLength=" + this.getMaxLength() + ", toField=" + this.getToField() + ", onSet=" + this.getOnSet() + ", appendHash=" + this.isAppendHash() + ", suffix=" + this.getSuffix() + ")";
    }
}