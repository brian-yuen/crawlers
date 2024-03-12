// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
/* Copyright 2018-2023 Norconex Inc.
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
package com.norconex.importer.handler.splitter.impl;

import com.norconex.commons.lang.text.TextMatcher;
import com.norconex.importer.handler.splitter.BaseDocumentSplitterConfig;

/**
 * <p>
 * Split PDFs pages so each pages are treated as individual documents. May not
 * work on all PDFs (e.g., encrypted PDFs).
 * </p>
 *
 * <p>
 * The original PDF is kept intact. If you want to eliminate it to keep only
 * the split pages, make sure to filter it.  You can do so by filtering
 * out PDFs without one of these two fields added to each pages:
 * <code>document.pdf.pageNumber</code> or
 * <code>document.pdf.numberOfPages</code>.  A filtering example:
 * </p>
 *
 * {@nx.xml.example
 * <filter class="com.norconex.importer.handler.filter.impl.EmptyFilter"
 *         onMatch="exclude">
 *   <fieldMatcher matchWhole="true">document.pdf.pageNumber</fieldMatcher>
 * </filter>
 * }
 *
 * <p>
 * By default this splitter restricts its use to
 * <code>document.contentType</code> matching <code>application/pdf</code>.
 * </p>
 *
 * <p>Should be used as a pre-parse handler.</p>
 *
 * {@nx.xml.usage
 *  <handler class="com.norconex.importer.handler.splitter.impl.PDFPageSplitter">
 *    {@nx.include com.norconex.importer.handler.AbstractImporterHandler#restrictTo}
 *    <referencePagePrefix>
 *      (String to put before the page number is appended to the document
 *      reference. Default is "#".)
 *    </referencePagePrefix>
 *
 *  </handler>
 * }
 *
 * {@nx.xml.example
 * <handler class="PDFPageSplitter">
 *   <referencePagePrefix>#page</referencePagePrefix>
 * </handler>
 * }
 * <p>The above example will split PDFs and will append the page number
 * to the original PDF reference as "#page1", "#page2", etc.
 * </p>
 */
@SuppressWarnings("javadoc")
public class PDFPageSplitterConfig extends BaseDocumentSplitterConfig {
    private String referencePagePrefix = PDFPageSplitter.DEFAULT_REFERENCE_PAGE_PREFIX;
    /**
     * The matcher of content types to apply splitting on. No attempt to
     * split documents of any other content types will be made. Default is
     * <code>application/pdf</code>.
     * @param contentTypeMatcher content type matcher
     */
    private final TextMatcher contentTypeMatcher = TextMatcher.basic("application/pdf");

    /**
     * The matcher of content types to apply splitting on. No attempt to
     * split documents of any other content types will be made. Default is
     * <code>application/pdf</code>.
     * @param contentTypeMatcher content type matcher
     * @return this
     */
    public PDFPageSplitterConfig setContentTypeMatcher(TextMatcher matcher) {
        contentTypeMatcher.copyFrom(matcher);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public PDFPageSplitterConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getReferencePagePrefix() {
        return this.referencePagePrefix;
    }

    /**
     * The matcher of content types to apply splitting on. No attempt to
     * split documents of any other content types will be made. Default is
     * <code>application/pdf</code>.
     * @return content type matcher
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextMatcher getContentTypeMatcher() {
        return this.contentTypeMatcher;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public PDFPageSplitterConfig setReferencePagePrefix(final String referencePagePrefix) {
        this.referencePagePrefix = referencePagePrefix;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof PDFPageSplitterConfig)) return false;
        final PDFPageSplitterConfig other = (PDFPageSplitterConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (!super.equals(o)) return false;
        final java.lang.Object this$referencePagePrefix = this.getReferencePagePrefix();
        final java.lang.Object other$referencePagePrefix = other.getReferencePagePrefix();
        if (this$referencePagePrefix == null ? other$referencePagePrefix != null : !this$referencePagePrefix.equals(other$referencePagePrefix)) return false;
        final java.lang.Object this$contentTypeMatcher = this.getContentTypeMatcher();
        final java.lang.Object other$contentTypeMatcher = other.getContentTypeMatcher();
        if (this$contentTypeMatcher == null ? other$contentTypeMatcher != null : !this$contentTypeMatcher.equals(other$contentTypeMatcher)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof PDFPageSplitterConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final java.lang.Object $referencePagePrefix = this.getReferencePagePrefix();
        result = result * PRIME + ($referencePagePrefix == null ? 43 : $referencePagePrefix.hashCode());
        final java.lang.Object $contentTypeMatcher = this.getContentTypeMatcher();
        result = result * PRIME + ($contentTypeMatcher == null ? 43 : $contentTypeMatcher.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "PDFPageSplitterConfig(super=" + super.toString() + ", referencePagePrefix=" + this.getReferencePagePrefix() + ", contentTypeMatcher=" + this.getContentTypeMatcher() + ")";
    }
}
