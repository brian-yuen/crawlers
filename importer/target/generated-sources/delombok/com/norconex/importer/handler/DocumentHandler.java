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
package com.norconex.importer.handler;

import java.io.IOException;
import java.util.function.Consumer;
import org.apache.commons.lang3.function.FailableConsumer;
import lombok.NonNull;
//REMOVE to make it a regular handler, but how to we keep the init around?
// and how about shut down?  We should probably add that to all handlers
// maybe to an interface as default methods? Or shall we use existing
// mechanism that scans and initiate classes? Yeah, probably best.
// Maybe rename RawTextExtractor?
/**
 * Implementations are responsible for interacting with a document to
 * either parse it, transform it, decorate it, filter it, etc.
 */
public interface DocumentHandler extends Consumer<DocContext> {
    // THIS SHOULD BE DETECTABLE and be the base of doc consumers for
    // the importer.
    //TODO move this out of .parser.
    //TODO maybe pass Importer to method?
    default void init() throws IOException {
    }

    default void destroy() throws IOException {
    }

    //default void destroy(Importer importer) {}
    //--- Decorators -----------------------------------------------------------
    static DocumentHandler decorate(@NonNull FailableConsumer<DocContext, IOException> consumer) {
        java.util.Objects.requireNonNull(consumer, "consumer is marked non-null but is null");
        return new FailableConsumerWrapper(consumer);
    }

    static BaseDocumentHandler decorate(@NonNull Consumer<DocContext> consumer) {
        java.util.Objects.requireNonNull(consumer, "consumer is marked non-null but is null");
        return new ConsumerWrapper(consumer);
    }


    class FailableConsumerWrapper extends BaseDocumentHandler {
        private final FailableConsumer<DocContext, IOException> original;

        @Override
        public void handle(DocContext d) throws IOException {
            original.accept(d);
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public FailableConsumerWrapper(final FailableConsumer<DocContext, IOException> original) {
            this.original = original;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public FailableConsumer<DocContext, IOException> getOriginal() {
            return this.original;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof DocumentHandler.FailableConsumerWrapper)) return false;
            final DocumentHandler.FailableConsumerWrapper other = (DocumentHandler.FailableConsumerWrapper) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            if (!super.equals(o)) return false;
            final java.lang.Object this$original = this.getOriginal();
            final java.lang.Object other$original = other.getOriginal();
            if (this$original == null ? other$original != null : !this$original.equals(other$original)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof DocumentHandler.FailableConsumerWrapper;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final java.lang.Object $original = this.getOriginal();
            result = result * PRIME + ($original == null ? 43 : $original.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "DocumentHandler.FailableConsumerWrapper(super=" + super.toString() + ", original=" + this.getOriginal() + ")";
        }
    }


    class ConsumerWrapper extends BaseDocumentHandler {
        private final Consumer<DocContext> original;

        @Override
        public void handle(DocContext d) throws IOException {
            original.accept(d);
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ConsumerWrapper(final Consumer<DocContext> original) {
            this.original = original;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Consumer<DocContext> getOriginal() {
            return this.original;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof DocumentHandler.ConsumerWrapper)) return false;
            final DocumentHandler.ConsumerWrapper other = (DocumentHandler.ConsumerWrapper) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            if (!super.equals(o)) return false;
            final java.lang.Object this$original = this.getOriginal();
            final java.lang.Object other$original = other.getOriginal();
            if (this$original == null ? other$original != null : !this$original.equals(other$original)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof DocumentHandler.ConsumerWrapper;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int hashCode() {
            final int PRIME = 59;
            int result = super.hashCode();
            final java.lang.Object $original = this.getOriginal();
            result = result * PRIME + ($original == null ? 43 : $original.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "DocumentHandler.ConsumerWrapper(super=" + super.toString() + ", original=" + this.getOriginal() + ")";
        }
    }
//    /**
//     * Initializes this parser, allowing caching of elements to improve re-use.
//     * Not all parsers support all parse options and it is possible calling
//     * this method on specific parsers to have no effect.
//     * @param parseOptions parse options (never <code>null</code>)
//     * @throws DocumentParserException problem initializing parser
//     */
//    void init(@NonNull ParseOptions parseOptions)
//            throws DocumentParserException;
//
//    /**
//     * Parses a document.
//     * @param doc importer document to parse
//     * @param output where to store extracted or modified content of the
//     *        supplied document
//     * @return a list of first-level embedded documents, if any
//     * @throws DocumentParserException problem parsing document
//     */
//    List<Doc> parseDocument(Doc doc, Writer output)
//            throws DocumentParserException;
}
