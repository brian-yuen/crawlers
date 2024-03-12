// Generated by delombok at Fri Mar 08 16:25:40 MST 2024
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
package com.norconex.importer.util.chunk;

public class TextChunk {
    /**
     * The field name where the text is coming from. <code>null</code>
     * if the text comes from the document content instead.
     */
    private final String field; // null if content
    /**
     * On multi-valued field, the index of the value currently processed.
     */
    private final int fieldValueIndex;
    /**
     * Index of the text portion currently processed.
     */
    private final int chunkIndex;
    /**
     * Full or partial text depending whether the maximum read size was
     * reached and it needed to be sent in text chunks.
     */
    private final String text;

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextChunk(final String field, final int fieldValueIndex, final int chunkIndex, final String text) {
        this.field = field;
        this.fieldValueIndex = fieldValueIndex;
        this.chunkIndex = chunkIndex;
        this.text = text;
    }

    /**
     * The field name where the text is coming from. <code>null</code>
     * if the text comes from the document content instead.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getField() {
        return this.field;
    }

    /**
     * On multi-valued field, the index of the value currently processed.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int getFieldValueIndex() {
        return this.fieldValueIndex;
    }

    /**
     * Index of the text portion currently processed.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int getChunkIndex() {
        return this.chunkIndex;
    }

    /**
     * Full or partial text depending whether the maximum read size was
     * reached and it needed to be sent in text chunks.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getText() {
        return this.text;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof TextChunk)) return false;
        final TextChunk other = (TextChunk) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.getFieldValueIndex() != other.getFieldValueIndex()) return false;
        if (this.getChunkIndex() != other.getChunkIndex()) return false;
        final java.lang.Object this$field = this.getField();
        final java.lang.Object other$field = other.getField();
        if (this$field == null ? other$field != null : !this$field.equals(other$field)) return false;
        final java.lang.Object this$text = this.getText();
        final java.lang.Object other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof TextChunk;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getFieldValueIndex();
        result = result * PRIME + this.getChunkIndex();
        final java.lang.Object $field = this.getField();
        result = result * PRIME + ($field == null ? 43 : $field.hashCode());
        final java.lang.Object $text = this.getText();
        result = result * PRIME + ($text == null ? 43 : $text.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "TextChunk(field=" + this.getField() + ", fieldValueIndex=" + this.getFieldValueIndex() + ", chunkIndex=" + this.getChunkIndex() + ", text=" + this.getText() + ")";
    }
}