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
package com.norconex.crawler.core.checksum.impl;

import com.norconex.commons.lang.text.TextMatcher;
import com.norconex.crawler.core.checksum.BaseChecksummerConfig;
import com.norconex.crawler.core.checksum.MetadataChecksummer;
import com.norconex.crawler.core.doc.CrawlDocMetadata;

/**
 * <p>
 * Generic implementation of {@link MetadataChecksummer} that uses
 * specified field names and their values to create a checksum. The name
 * and values are simply returned as is, joined using this format:
 * <code>fieldName=fieldValue;fieldName=fieldValue;...</code>.
 * </p>
 * <p>
 * You have the option to keep the checksum as a document metadata field.
 * When {@link #setKeep(boolean)} is <code>true</code>, the checksum will be
 * stored in the target field name specified. If you do not specify any,
 * it stores it under the metadata field name
 * {@link CrawlDocMetadata#CHECKSUM_METADATA}.
 * </p>
 * {@nx.xml.usage
 * <metadataChecksummer
 *     class="com.norconex.crawler.core.checksum.impl.GenericMetadataChecksummer"
 *     keep="[false|true]"
 *     toField="(optional field to store the checksum)">
 *
 *   <fieldMatcher {@nx.include com.norconex.commons.lang.text.TextMatcher#matchAttributes}>
 *     (expression matching fields used to create the checksum)
 *   </fieldMatcher>
 * </metadataChecksummer>
 * }
 *
 * <p>
 * <code>toField</code> is ignored unless the <code>keep</code>
 * attribute is set to <code>true</code>.
 * </p>
 *
 * {@nx.xml.example
 * <metadataChecksummer class="GenericMetadataChecksummer">
 *   <fieldMatcher method="csv">docLastModified,docSize</fieldMatcher>
 * </metadataChecksummer>
 * }
 * <p>
 * The above example uses a combination of two (fictitious) fields called
 * "docLastModified" and "docSize" to make the checksum.
 * </p>
 *
 * <p>
 * A self-closing <code>&lt;metadataChecksummer/&gt;</code> tag without
 * any attributes is used to disable checksum generation.
 * </p>
 */
@SuppressWarnings("javadoc")
public class GenericMetadataChecksummerConfig extends BaseChecksummerConfig {
    /**
     * The field matcher.
     * @param fieldMatcher field matcher
     */
    private final TextMatcher fieldMatcher = new TextMatcher();

    public GenericMetadataChecksummerConfig setFieldMatcher(TextMatcher fieldMatcher) {
        this.fieldMatcher.copyFrom(fieldMatcher);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public GenericMetadataChecksummerConfig() {
    }

    /**
     * The field matcher.
     * @return field matcher
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextMatcher getFieldMatcher() {
        return this.fieldMatcher;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof GenericMetadataChecksummerConfig)) return false;
        final GenericMetadataChecksummerConfig other = (GenericMetadataChecksummerConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (!super.equals(o)) return false;
        final java.lang.Object this$fieldMatcher = this.getFieldMatcher();
        final java.lang.Object other$fieldMatcher = other.getFieldMatcher();
        if (this$fieldMatcher == null ? other$fieldMatcher != null : !this$fieldMatcher.equals(other$fieldMatcher)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof GenericMetadataChecksummerConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final java.lang.Object $fieldMatcher = this.getFieldMatcher();
        result = result * PRIME + ($fieldMatcher == null ? 43 : $fieldMatcher.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "GenericMetadataChecksummerConfig(super=" + super.toString() + ", fieldMatcher=" + this.getFieldMatcher() + ")";
    }
}
