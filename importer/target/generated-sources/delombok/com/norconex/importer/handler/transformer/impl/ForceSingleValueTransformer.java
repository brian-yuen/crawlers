// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
/* Copyright 2010-2023 Norconex Inc.
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

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import com.norconex.commons.lang.config.Configurable;
import com.norconex.importer.handler.BaseDocumentHandler;
import com.norconex.importer.handler.DocContext;

/**
 * <p>
 * Forces a metadata field to be single-value.  The action can be one of the
 * following:
 * </p>
 * <p>Can be used both as a pre-parse or post-parse handler.</p>
 * <pre>
 *    keepFirst          Keeps the first occurrence found.
 *    keepLast           Keeps the first occurrence found.
 *    mergeWith:&lt;sep&gt;    Merges all occurrences, joining them with the
 *                       specified separator (&lt;sep&gt;).
 * </pre>
 * <p>
 * If you do not specify any action, the default behavior is to merge all
 * occurrences, joining values with a comma.
 * </p>
 * {@nx.xml.usage
 * <handler class="com.norconex.importer.handler.tagger.impl.ForceSingleValueTagger"
 *     action="[keepFirst|keepLast|mergeWith:separator]">
 *
 *   {@nx.include com.norconex.importer.handler.AbstractImporterHandler#restrictTo}
 *
 *   <fieldMatcher {@nx.include com.norconex.commons.lang.text.TextMatcher#matchAttributes}>
 *       (one or more matching fields to force having a single value)
 *   </fieldMatcher>
 *
 * </handler>
 * }
 *
 * {@nx.xml.example
 * <handler class="ForceSingleValueTagger" action="keepFirst">
 *   <fieldMatcher>title</fieldMatcher>
 * </handler>
 * }
 * <p>
 * For documents where multiple title fields are found, the above only
 * keeps the first title value captured.
 * </p>
 */
@SuppressWarnings("javadoc")
public class ForceSingleValueTransformer extends BaseDocumentHandler implements Configurable<ForceSingleValueTransformerConfig> {
    private final ForceSingleValueTransformerConfig configuration = new ForceSingleValueTransformerConfig();

    @Override
    public void handle(DocContext docCtx) throws IOException {
        var action = configuration.getAction();
        for (Entry<String, List<String>> en : docCtx.metadata().matchKeys(configuration.getFieldMatcher()).entrySet()) {
            var field = en.getKey();
            var values = en.getValue();
            if (values != null && !values.isEmpty() && StringUtils.isNotBlank(action)) {
                String singleValue = null;
                if ("keepFirst".equalsIgnoreCase(action)) {
                    singleValue = values.get(0);
                } else if ("keepLast".equalsIgnoreCase(action)) {
                    singleValue = values.get(values.size() - 1);
                } else if (StringUtils.startsWithIgnoreCase(action, "mergeWith")) {
                    var sep = StringUtils.substringAfter(action, ":");
                    singleValue = StringUtils.join(values, sep);
                } else {
                    singleValue = StringUtils.join(values, ",");
                }
                docCtx.metadata().set(field, singleValue);
            }
        }
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public ForceSingleValueTransformer() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public ForceSingleValueTransformerConfig getConfiguration() {
        return this.configuration;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof ForceSingleValueTransformer)) return false;
        final ForceSingleValueTransformer other = (ForceSingleValueTransformer) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (!super.equals(o)) return false;
        final java.lang.Object this$configuration = this.getConfiguration();
        final java.lang.Object other$configuration = other.getConfiguration();
        if (this$configuration == null ? other$configuration != null : !this$configuration.equals(other$configuration)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof ForceSingleValueTransformer;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final java.lang.Object $configuration = this.getConfiguration();
        result = result * PRIME + ($configuration == null ? 43 : $configuration.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "ForceSingleValueTransformer(super=" + super.toString() + ", configuration=" + this.getConfiguration() + ")";
    }
}
