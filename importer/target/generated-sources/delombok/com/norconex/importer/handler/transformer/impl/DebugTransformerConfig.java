// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
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
package com.norconex.importer.handler.transformer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.norconex.commons.lang.collection.CollectionUtil;

/**
 * <p>A utility tagger to help with troubleshooting of document importing.
 * Place this tagger anywhere in your handler configuration to print to
 * the log stream the metadata fields or content so far when this handler
 * gets invoked.
 * This handler does not impact the data being imported at all
 * (it only reads it).</p>
 *
 * <p>The default behavior logs all metadata fields using the DEBUG log level.
 * You can optionally set which fields to log and whether to also log the
 * document content or not, as well as specifying a different log level.</p>
 *
 * <p><b>Be careful:</b> Logging the content when you deal with very large
 * content can result in memory exceptions.</p>
 *
 * <p>Can be used both as a pre-parse or post-parse handler.</p>
 *
 * {@nx.xml.usage
 *  <handler class="com.norconex.importer.handler.tagger.impl.DebugTagger"
 *          logFields="(CSV list of fields to log)"
 *          logContent="[false|true]"
 *          logLevel="[ERROR|WARN|INFO|DEBUG|TRACE]"
 *          prefix="(optional log prefix to further help you locate it)" >
 *
 *   {@nx.include com.norconex.importer.handler.AbstractImporterHandler#restrictTo}
 *
 *  </handler>
 * }
 * {@nx.xml.example
 *  <handler class="DebugTagger" logFields="title,author" logLevel="INFO" />
 * }
 * <p>
 * The above logs the value of any "title" and "author" document metadata
 * fields.
 * </p>
 */
@SuppressWarnings("javadoc")
public class DebugTransformerConfig {
    private final List<String> logFields = new ArrayList<>();
    private boolean logContent;
    private String logLevel;
    /**
     * The prefix to print before the actual log message.
     */
    private String prefix;

    public List<String> getLogFields() {
        return Collections.unmodifiableList(logFields);
    }

    public DebugTransformerConfig setLogFields(List<String> logFields) {
        CollectionUtil.setAll(this.logFields, logFields);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DebugTransformerConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isLogContent() {
        return this.logContent;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getLogLevel() {
        return this.logLevel;
    }

    /**
     * The prefix to print before the actual log message.
     * @return log entry prefix
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DebugTransformerConfig setLogContent(final boolean logContent) {
        this.logContent = logContent;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DebugTransformerConfig setLogLevel(final String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    /**
     * The prefix to print before the actual log message.
     * @param prefix log entry prefix
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DebugTransformerConfig setPrefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof DebugTransformerConfig)) return false;
        final DebugTransformerConfig other = (DebugTransformerConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.isLogContent() != other.isLogContent()) return false;
        final java.lang.Object this$logFields = this.getLogFields();
        final java.lang.Object other$logFields = other.getLogFields();
        if (this$logFields == null ? other$logFields != null : !this$logFields.equals(other$logFields)) return false;
        final java.lang.Object this$logLevel = this.getLogLevel();
        final java.lang.Object other$logLevel = other.getLogLevel();
        if (this$logLevel == null ? other$logLevel != null : !this$logLevel.equals(other$logLevel)) return false;
        final java.lang.Object this$prefix = this.getPrefix();
        final java.lang.Object other$prefix = other.getPrefix();
        if (this$prefix == null ? other$prefix != null : !this$prefix.equals(other$prefix)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof DebugTransformerConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isLogContent() ? 79 : 97);
        final java.lang.Object $logFields = this.getLogFields();
        result = result * PRIME + ($logFields == null ? 43 : $logFields.hashCode());
        final java.lang.Object $logLevel = this.getLogLevel();
        result = result * PRIME + ($logLevel == null ? 43 : $logLevel.hashCode());
        final java.lang.Object $prefix = this.getPrefix();
        result = result * PRIME + ($prefix == null ? 43 : $prefix.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "DebugTransformerConfig(logFields=" + this.getLogFields() + ", logContent=" + this.isLogContent() + ", logLevel=" + this.getLogLevel() + ", prefix=" + this.getPrefix() + ")";
    }
}
