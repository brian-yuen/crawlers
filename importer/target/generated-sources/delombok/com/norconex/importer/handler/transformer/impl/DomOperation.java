// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
/* Copyright 2023 Norconex Inc.
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

/**
 * DOM operation details.
 */
@SuppressWarnings("javadoc")
public class DomOperation {
    private String selector;
    /**
     * The target field for extracted content.
     * Not applicable if delete is <code>true</code>.
     */
    private String toField;
    /**
     * The gets the property setter to use when a value is set.
     */
    private PropertySetter onSet;
    private String extract;
    /**
     * The gets whether elements with blank values should be considered a
     * match and have an empty string returned as opposed to nothing at all.
     */
    private boolean matchBlanks;
    /**
     * The gets whether to delete DOM attributes/elements matching the
     * specified selector.
     */
    private boolean delete;
    /**
     * The value to store in the target field if there are no matches.
     * Not applicable if delete is <code>true</code>.
     */
    private String defaultValue;

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getSelector() {
        return this.selector;
    }

    /**
     * The target field for extracted content.
     * Not applicable if delete is <code>true</code>.
     * @return target field
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getToField() {
        return this.toField;
    }

    /**
     * The gets the property setter to use when a value is set.
     * @return property setter
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public PropertySetter getOnSet() {
        return this.onSet;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getExtract() {
        return this.extract;
    }

    /**
     * The gets whether elements with blank values should be considered a
     * match and have an empty string returned as opposed to nothing at all.
     * @return <code>true</code> if elements with blank values are supported
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isMatchBlanks() {
        return this.matchBlanks;
    }

    /**
     * The gets whether to delete DOM attributes/elements matching the
     * specified selector.
     * @return <code>true</code> if deleting
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isDelete() {
        return this.delete;
    }

    /**
     * The value to store in the target field if there are no matches.
     * Not applicable if delete is <code>true</code>.
     * @return default value
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setSelector(final String selector) {
        this.selector = selector;
        return this;
    }

    /**
     * The target field for extracted content.
     * Not applicable if delete is <code>true</code>.
     * @param toField target field.
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setToField(final String toField) {
        this.toField = toField;
        return this;
    }

    /**
     * The gets the property setter to use when a value is set.
     * @param onSet property setter
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setOnSet(final PropertySetter onSet) {
        this.onSet = onSet;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setExtract(final String extract) {
        this.extract = extract;
        return this;
    }

    /**
     * The gets whether elements with blank values should be considered a
     * match and have an empty string returned as opposed to nothing at all.
     * @param matchBlanks <code>true</code> to support elements with
     *                    blank values
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setMatchBlanks(final boolean matchBlanks) {
        this.matchBlanks = matchBlanks;
        return this;
    }

    /**
     * The gets whether to delete DOM attributes/elements matching the
     * specified selector.
     * @param delete <code>true</code> if deleting
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setDelete(final boolean delete) {
        this.delete = delete;
        return this;
    }

    /**
     * The value to store in the target field if there are no matches.
     * Not applicable if delete is <code>true</code>.
     * @param defaultValue default value
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomOperation setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof DomOperation)) return false;
        final DomOperation other = (DomOperation) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.isMatchBlanks() != other.isMatchBlanks()) return false;
        if (this.isDelete() != other.isDelete()) return false;
        final java.lang.Object this$selector = this.getSelector();
        final java.lang.Object other$selector = other.getSelector();
        if (this$selector == null ? other$selector != null : !this$selector.equals(other$selector)) return false;
        final java.lang.Object this$toField = this.getToField();
        final java.lang.Object other$toField = other.getToField();
        if (this$toField == null ? other$toField != null : !this$toField.equals(other$toField)) return false;
        final java.lang.Object this$onSet = this.getOnSet();
        final java.lang.Object other$onSet = other.getOnSet();
        if (this$onSet == null ? other$onSet != null : !this$onSet.equals(other$onSet)) return false;
        final java.lang.Object this$extract = this.getExtract();
        final java.lang.Object other$extract = other.getExtract();
        if (this$extract == null ? other$extract != null : !this$extract.equals(other$extract)) return false;
        final java.lang.Object this$defaultValue = this.getDefaultValue();
        final java.lang.Object other$defaultValue = other.getDefaultValue();
        if (this$defaultValue == null ? other$defaultValue != null : !this$defaultValue.equals(other$defaultValue)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof DomOperation;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isMatchBlanks() ? 79 : 97);
        result = result * PRIME + (this.isDelete() ? 79 : 97);
        final java.lang.Object $selector = this.getSelector();
        result = result * PRIME + ($selector == null ? 43 : $selector.hashCode());
        final java.lang.Object $toField = this.getToField();
        result = result * PRIME + ($toField == null ? 43 : $toField.hashCode());
        final java.lang.Object $onSet = this.getOnSet();
        result = result * PRIME + ($onSet == null ? 43 : $onSet.hashCode());
        final java.lang.Object $extract = this.getExtract();
        result = result * PRIME + ($extract == null ? 43 : $extract.hashCode());
        final java.lang.Object $defaultValue = this.getDefaultValue();
        result = result * PRIME + ($defaultValue == null ? 43 : $defaultValue.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "DomOperation(selector=" + this.getSelector() + ", toField=" + this.getToField() + ", onSet=" + this.getOnSet() + ", extract=" + this.getExtract() + ", matchBlanks=" + this.isMatchBlanks() + ", delete=" + this.isDelete() + ", defaultValue=" + this.getDefaultValue() + ")";
    }
}
