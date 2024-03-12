// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
/* Copyright 2015-2023 Norconex Inc.
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.norconex.commons.lang.bean.module.JsonXmlCollection;
import com.norconex.commons.lang.collection.CollectionUtil;
import com.norconex.commons.lang.map.PropertySetter;
import com.norconex.commons.lang.text.TextMatcher;
import com.norconex.importer.handler.CommonMatchers;
import com.norconex.importer.handler.CommonRestrictions;
import com.norconex.importer.util.DomUtil;

/**
 * <p>Extract the value of one or more elements or attributes into
 * a target field, or delete matching elements. Applies to
 * HTML, XHTML, or XML document.</p>
 * <p>
 * This class constructs a DOM tree from a document or field content.
 * That DOM tree is loaded entirely into memory. Use this tagger with caution
 * if you know you'll need to parse huge files. It may be preferable to use
 * {@link RegexTransformer} if this is a concern. Also, to help performance
 * and avoid re-creating DOM tree before every DOM extraction you want to
 * perform, try to combine multiple extractions in a single instance
 * of this Tagger.
 * </p>
 * <p>
 * The <a href="http://jsoup.org/">jsoup</a> parser library is used to load a
 * document content into a DOM tree. Elements are referenced using a
 * <a href="http://jsoup.org/cookbook/extracting-data/selector-syntax">
 * CSS or JQuery-like syntax</a>.
 * </p>
 * <p>Should be used as a pre-parse handler.</p>
 *
 * <h3>Storing values in an existing field</h3>
 * <p>
 * If a target field with the same name already exists for a document,
 * values will be added to the end of the existing value list.
 * It is possible to change this default behavior by supplying a
 * {@link PropertySetter}.
 * </p>
 *
 * <h3>Content-types</h3>
 * <p>
 * By default, this filter is restricted to (applies only to) documents matching
 * the restrictions returned by
 * {@link CommonRestrictions#domContentTypes(String)}.
 * You can specify your own content types if you know they represent a file
 * with HTML or XML-like markup tags.
 * </p>
 *
 * <p>When used as a pre-parse handler,
 * this class attempts to detect the content character
 * encoding unless the character encoding
 * was specified using {@link #setSourceCharset(String)}. Since document
 * parsing converts content to UTF-8, UTF-8 is always assumed when
 * used as a post-parse handler.
 * </p>
 *
 * <p>You can control what gets extracted
 * exactly thanks to the "extract" argument of the new method
 * {@link DOMExtractDetails#setExtract(String)}. Possible values are:</p>
 * <ul>
 *   <li><b>text</b>: Default option when extract is blank. The text of
 *       the element, including combined children.</li>
 *   <li><b>html</b>: Extracts an element inner
 *       HTML (including children).</li>
 *   <li><b>outerHtml</b>: Extracts an element outer
 *       HTML (like "html", but includes the "current" tag).</li>
 *   <li><b>ownText</b>: Extracts the text owned by this element only;
 *       does not get the combined text of all children.</li>
 *   <li><b>data</b>: Extracts the combined data of a data-element (e.g.
 *       &lt;script&gt;).</li>
 *   <li><b>id</b>: Extracts the ID attribute of the element (if any).</li>
 *   <li><b>tagName</b>: Extract the name of the tag of the element.</li>
 *   <li><b>val</b>: Extracts the value of a form element
 *       (input, textarea, etc).</li>
 *   <li><b>className</b>: Extracts the literal value of the element's
 *       "class" attribute, which may include multiple class names,
 *       space separated.</li>
 *   <li><b>cssSelector</b>: Extracts a CSS selector that will uniquely
 *       select (identify) this element.</li>
 *   <li><b>attr(attributeKey)</b>: Extracts the value of the element
 *       attribute matching your replacement for "attributeKey"
 *       (e.g. "attr(title)" will extract the "title" attribute).</li>
 * </ul>
 *
 * <p>You can specify a <code>fromField</code>
 * as the source of the HTML to parse instead of using the document content.
 * If multiple values are present for that source field, DOM extraction will be
 * applied to each value.
 * </p>
 *
 * <p>You can specify a <code>defaultValue</code>
 * on each DOM extraction details. When no match occurred for a given selector,
 * the default value will be stored in the <code>toField</code> (as opposed
 * to not storing anything).  When matching blanks (see below) you will get
 * an empty string as opposed to the default value.
 * Empty strings and spaces are supported as default values
 * (the default value is now taken literally).
 * </p>
 *
 * <p>You can set <code>matchBlanks</code> to
 * <code>true</code> to match elements that are present
 * but have blank values. Blank values are empty values or values containing
 * white spaces only. Because white spaces are normalized by the DOM parser,
 * such matches will always return an empty string (spaces will be trimmed).
 * By default elements with blank values are not matched and are ignored.
 * </p>
 *
 * <p>You can specify which parser to use when reading
 * documents. The default is "html" and will normalize the content
 * as HTML. This is generally a desired behavior, but this can sometimes
 * have your selector fail. If you encounter this
 * problem, try switching to "xml" parser, which does not attempt normalization
 * on the content. The drawback with "xml" is you may not get all HTML-specific
 * selector options to work.  If you know you are dealing with XML to begin
 * with, specifying "xml" should be a good option.
 * </p>
 *
 * <h3>Content deletion from fields</h3>
 * <p>
 * As of 3.0.0, you can specify whether to delete any elements
 * matched by the selector. You can use with a "toField" or on its own.
 * Some options are ignored by deletions, such as
 * "extract" or "defaultValue".  Because taggers cannot modify the document
 * content, deletion only applies to metadata fields. Use {@link DOMDeleteTransformer}
 * to modify the document content.
 * </p>
 *
 * {@nx.xml.usage
 * <handler class="com.norconex.importer.handler.tagger.impl.DOMTagger"
 *         fromField="(optional source field)"
 *         parser="[html|xml]"
 *         sourceCharset="(character encoding)">
 *
 *   {@nx.include com.norconex.importer.handler.AbstractImporterHandler#restrictTo}
 *
 *   <!-- multiple "dom" tags allowed -->
 *   <dom selector="(selector syntax)"
 *       toField="(target field)"
 *       extract="[text|html|outerHtml|ownText|data|tagName|val|className|cssSelector|attr(attributeKey)]"
 *       matchBlanks="[false|true]"
 *       defaultValue="(optional value to use when no match)"
 *       delete="[false|true]"
 *       {@nx.include com.norconex.commons.lang.map.PropertySetter#attributes}/>
 *
 * </handler>
 * }
 *
 * {@nx.xml.example
 * <handler class="DOMTagger">
 *   <dom selector="div.firstName" toField="firstName" />
 *   <dom selector="div.lastName"  toField="lastName" />
 * </handler>
 * }
 * <p>
 * Given this HTML snippet...
 * </p>
 * <pre>
 * &lt;div class="firstName"&gt;Joe&lt;/div&gt;
 * &lt;div class="lastName"&gt;Dalton&lt;/div&gt;
 * </pre>
 * <p>
 * ... the above example will store "Joe" in a "firstName" field and "Dalton"
 * in a "lastName" field.
 * </p>
 * @see DOMDeleteTransformer
 */
@SuppressWarnings("javadoc")
public class DomTransformerConfig {
    /**
     * Matcher of one or more fields to use as the source of content to
     * transforms, instead of the original document content.
     * @param fieldMatcher field matcher
     */
    private final TextMatcher fieldMatcher = new TextMatcher();
    /**
     * The matcher of content types to apply transformation on. No attempt to
     * transform documents of any other content types will be made. Default is
     * {@link CommonMatchers#DOM_CONTENT_TYPES}.
     * @param contentTypeMatcher content type matcher
     */
    private final TextMatcher contentTypeMatcher = CommonMatchers.domContentTypes();
    /**
     * The presumed source character set.
     */
    private Charset sourceCharset;
    /**
     * The type of parser to use when creating the DOM-tree.
     * Default is <code>html</code>.
     */
    private String parser = DomUtil.PARSER_HTML;
    /**
     * The list of operations to perform.
     */
    @JsonXmlCollection(entryName = "op")
    private final List<DomOperation> operations = new ArrayList<>();

    public DomTransformerConfig setFieldMatcher(TextMatcher fieldMatcher) {
        this.fieldMatcher.copyFrom(fieldMatcher);
        return this;
    }

    /**
     * The matcher of content types to apply transformation on. No attempt to
     * transform documents of any other content types will be made. Default is
     * {@link CommonMatchers#DOM_CONTENT_TYPES}.
     * @param contentTypeMatcher content type matcher
     * @return this
     */
    public DomTransformerConfig setContentTypeMatcher(TextMatcher matcher) {
        contentTypeMatcher.copyFrom(matcher);
        return this;
    }

    public DomTransformerConfig setOperations(List<DomOperation> operations) {
        CollectionUtil.setAll(this.operations, operations);
        return this;
    }

    public List<DomOperation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomTransformerConfig() {
    }

    /**
     * Matcher of one or more fields to use as the source of content to
     * transforms, instead of the original document content.
     * @return field matcher
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextMatcher getFieldMatcher() {
        return this.fieldMatcher;
    }

    /**
     * The matcher of content types to apply transformation on. No attempt to
     * transform documents of any other content types will be made. Default is
     * {@link CommonMatchers#DOM_CONTENT_TYPES}.
     * @return content type matcher
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextMatcher getContentTypeMatcher() {
        return this.contentTypeMatcher;
    }

    /**
     * The presumed source character set.
     * @return character set of the source to be transformed
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Charset getSourceCharset() {
        return this.sourceCharset;
    }

    /**
     * The type of parser to use when creating the DOM-tree.
     * Default is <code>html</code>.
     * @return <code>html</code> or <code>xml</code>.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getParser() {
        return this.parser;
    }

    /**
     * The presumed source character set.
     * @param sourceCharset character set of the source to be transformed
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomTransformerConfig setSourceCharset(final Charset sourceCharset) {
        this.sourceCharset = sourceCharset;
        return this;
    }

    /**
     * The type of parser to use when creating the DOM-tree.
     * Default is <code>html</code>.
     * @param parser <code>html</code> or <code>xml</code>.
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DomTransformerConfig setParser(final String parser) {
        this.parser = parser;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof DomTransformerConfig)) return false;
        final DomTransformerConfig other = (DomTransformerConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$fieldMatcher = this.getFieldMatcher();
        final java.lang.Object other$fieldMatcher = other.getFieldMatcher();
        if (this$fieldMatcher == null ? other$fieldMatcher != null : !this$fieldMatcher.equals(other$fieldMatcher)) return false;
        final java.lang.Object this$contentTypeMatcher = this.getContentTypeMatcher();
        final java.lang.Object other$contentTypeMatcher = other.getContentTypeMatcher();
        if (this$contentTypeMatcher == null ? other$contentTypeMatcher != null : !this$contentTypeMatcher.equals(other$contentTypeMatcher)) return false;
        final java.lang.Object this$sourceCharset = this.getSourceCharset();
        final java.lang.Object other$sourceCharset = other.getSourceCharset();
        if (this$sourceCharset == null ? other$sourceCharset != null : !this$sourceCharset.equals(other$sourceCharset)) return false;
        final java.lang.Object this$parser = this.getParser();
        final java.lang.Object other$parser = other.getParser();
        if (this$parser == null ? other$parser != null : !this$parser.equals(other$parser)) return false;
        final java.lang.Object this$operations = this.getOperations();
        final java.lang.Object other$operations = other.getOperations();
        if (this$operations == null ? other$operations != null : !this$operations.equals(other$operations)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof DomTransformerConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $fieldMatcher = this.getFieldMatcher();
        result = result * PRIME + ($fieldMatcher == null ? 43 : $fieldMatcher.hashCode());
        final java.lang.Object $contentTypeMatcher = this.getContentTypeMatcher();
        result = result * PRIME + ($contentTypeMatcher == null ? 43 : $contentTypeMatcher.hashCode());
        final java.lang.Object $sourceCharset = this.getSourceCharset();
        result = result * PRIME + ($sourceCharset == null ? 43 : $sourceCharset.hashCode());
        final java.lang.Object $parser = this.getParser();
        result = result * PRIME + ($parser == null ? 43 : $parser.hashCode());
        final java.lang.Object $operations = this.getOperations();
        result = result * PRIME + ($operations == null ? 43 : $operations.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "DomTransformerConfig(fieldMatcher=" + this.getFieldMatcher() + ", contentTypeMatcher=" + this.getContentTypeMatcher() + ", sourceCharset=" + this.getSourceCharset() + ", parser=" + this.getParser() + ", operations=" + this.getOperations() + ")";
    }
}
