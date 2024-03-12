// Generated by delombok at Fri Mar 08 16:23:51 MST 2024
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
package com.norconex.committer.core.impl;

import com.norconex.committer.core.BaseCommitterConfig;
import com.norconex.commons.lang.text.TextMatcher;

public class LogCommitterConfig extends BaseCommitterConfig {

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR, STDOUT, STDERR;
    }

    private boolean ignoreContent;
    private final TextMatcher fieldMatcher = new TextMatcher();
    private LogLevel logLevel;

    public LogCommitterConfig setFieldMatcher(TextMatcher fieldMatcher) {
        this.fieldMatcher.copyFrom(fieldMatcher);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public LogCommitterConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isIgnoreContent() {
        return this.ignoreContent;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextMatcher getFieldMatcher() {
        return this.fieldMatcher;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public LogCommitterConfig setIgnoreContent(final boolean ignoreContent) {
        this.ignoreContent = ignoreContent;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public LogCommitterConfig setLogLevel(final LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof LogCommitterConfig)) return false;
        final LogCommitterConfig other = (LogCommitterConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (!super.equals(o)) return false;
        if (this.isIgnoreContent() != other.isIgnoreContent()) return false;
        final java.lang.Object this$fieldMatcher = this.getFieldMatcher();
        final java.lang.Object other$fieldMatcher = other.getFieldMatcher();
        if (this$fieldMatcher == null ? other$fieldMatcher != null : !this$fieldMatcher.equals(other$fieldMatcher)) return false;
        final java.lang.Object this$logLevel = this.getLogLevel();
        final java.lang.Object other$logLevel = other.getLogLevel();
        if (this$logLevel == null ? other$logLevel != null : !this$logLevel.equals(other$logLevel)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof LogCommitterConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        result = result * PRIME + (this.isIgnoreContent() ? 79 : 97);
        final java.lang.Object $fieldMatcher = this.getFieldMatcher();
        result = result * PRIME + ($fieldMatcher == null ? 43 : $fieldMatcher.hashCode());
        final java.lang.Object $logLevel = this.getLogLevel();
        result = result * PRIME + ($logLevel == null ? 43 : $logLevel.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "LogCommitterConfig(super=" + super.toString() + ", ignoreContent=" + this.isIgnoreContent() + ", fieldMatcher=" + this.getFieldMatcher() + ", logLevel=" + this.getLogLevel() + ")";
    }
}
