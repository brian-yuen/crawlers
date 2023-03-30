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
package com.norconex.crawler.core.crawler;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.norconex.committer.core.Committer;
import com.norconex.commons.lang.collection.CollectionUtil;
import com.norconex.commons.lang.event.EventListener;
import com.norconex.commons.lang.xml.XML;
import com.norconex.commons.lang.xml.XMLConfigurable;
import com.norconex.commons.lang.xml.XPathUtil;
import com.norconex.crawler.core.checksum.DocumentChecksummer;
import com.norconex.crawler.core.checksum.MetadataChecksummer;
import com.norconex.crawler.core.checksum.impl.MD5DocumentChecksummer;
import com.norconex.crawler.core.fetch.FetchDirectiveSupport;
import com.norconex.crawler.core.fetch.Fetcher;
import com.norconex.crawler.core.filter.DocumentFilter;
import com.norconex.crawler.core.filter.MetadataFilter;
import com.norconex.crawler.core.filter.ReferenceFilter;
import com.norconex.crawler.core.processor.DocumentProcessor;
import com.norconex.crawler.core.spoil.SpoiledReferenceStrategizer;
import com.norconex.crawler.core.spoil.impl.GenericSpoiledReferenceStrategizer;
import com.norconex.crawler.core.store.DataStoreEngine;
import com.norconex.crawler.core.store.impl.mvstore.MVStoreDataStoreEngine;
import com.norconex.importer.ImporterConfig;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * <p>
 * Base Crawler configuration. Crawlers usually read this configuration upon
 * starting up.  Once execution has started, it should not be changed
 * to avoid unexpected behaviors.
 * </p>
 *
 * <p>
 * Concrete implementations inherit the following XML configuration
 * options (typically within a <code>&lt;crawler&gt;</code> tag):
 * </p>
 *
 * {@nx.xml #init
 *
 *   <numThreads>(maximum number of threads)</numThreads>
 *   <maxDocuments>
 *     (maximum number of documents to crawl per session, resuming on next
 *      sessions where it last ended, if crawling was not complete)
 *   </maxDocuments>
 *   <maxDepth>(maximum depth the crawler should go)</maxDepth>
 *   <idleTimeout>(thread inactivity timeout)</idleTimeout>
 *   <minProgressLoggingInterval>
 *     (minimum frequency at which progress is logged)
 *   </minProgressLoggingInterval>
 *   <orphansStrategy>[PROCESS|IGNORE|DELETE]</orphansStrategy>
 *
 *   <stopOnExceptions>
 *     <!-- Repeatable -->
 *     <exception>(fully qualified class name of a an exception)</exception>
 *   </stopOnExceptions>
 *
 *   <eventListeners>
 *     <!-- Repeatable -->
 *     <listener class="(EventListener implementation)"/>
 *   </eventListeners>
 *
 *   <dataStoreEngine class="(DataStoreEngine implementation)" />
 * }
 *
 * {@nx.xml #pipeline-queue
 *   <referenceFilters>
 *     <!-- Repeatable -->
 *     <filter
 *         class="(ReferenceFilter implementation)"
 *         onMatch="[include|exclude]" />
 *   </referenceFilters>
 * }
 *
 * {@nx.xml #pipeline-import
 *   <metadataFilters>
 *     <!-- Repeatable -->
 *     <filter
 *         class="(MetadataFilter implementation)"
 *         onMatch="[include|exclude]" />
 *   </metadataFilters>
 *
 *   <documentFilters>
 *     <!-- Repeatable -->
 *     <filter class="(DocumentFilter implementation)" />
 *   </documentFilters>
 * }
 *
 * {@nx.xml #import
 *   <importer>
 *     <preParseHandlers>
 *       <!-- Repeatable -->
 *       <handler class="(an handler class from the Importer module)"/>
 *     </preParseHandlers>
 *     <documentParserFactory class="(DocumentParser implementation)" />
 *     <postParseHandlers>
 *       <!-- Repeatable -->
 *       <handler class="(an handler class from the Importer module)"/>
 *     </postParseHandlers>
 *     <responseProcessors>
 *       <!-- Repeatable -->
 *       <responseProcessor
 *              class="(ImporterResponseProcessor implementation)" />
 *     </responseProcessors>
 *   </importer>
 * }
 *
 * {@nx.xml #directive-meta
 *   <metadataFetchSupport>[DISABLED|REQUIRED|OPTIONAL]</metadataFetchSupport>
 * }
 * {@nx.xml #directive-doc
 *   <documentFetchSupport>[REQUIRED|DISABLED|OPTIONAL]</documentFetchSupport>
 * }
 *
 * {@nx.xml #checksum-meta
 *   <metadataChecksummer class="(MetadataChecksummer implementation)" />
 * }
 *
 * {@nx.xml #dedup-meta
 *   <metadataDeduplicate>[false|true]</metadataDeduplicate>
 * }
 *
 * {@nx.xml #checksum-doc
 *   <documentChecksummer class="(DocumentChecksummer implementation)" />
 * }
 *
 * {@nx.xml #dedup-doc
 *   <documentDeduplicate>[false|true]</documentDeduplicate>
 * }
 *
 * {@nx.xml #pipeline-committer
 *   <spoiledReferenceStrategizer
 *       class="(SpoiledReferenceStrategizer implementation)" />
 *
 *   <committers>
 *     <committer class="(Committer implementation)" />
 *   </committers>
 * }
 */
@Data
@FieldNameConstants
public class CrawlerConfig implements XMLConfigurable {

    public enum OrphansStrategy {
        /**
         * Processing orphans tries to obtain and process them again,
         * normally.
         */
        PROCESS,
        /**
         * Deleting orphans sends them to the Committer for deletions and
         * they are removed from the internal reference cache.
         */
        DELETE,
        /**
         * Ignoring orphans effectively does nothing with them
         * (not deleted, not processed).
         */
        IGNORE
    }

    public static final Duration DEFAULT_IDLE_PROCESSING_TIMEOUT =
            Duration.ofMinutes(10);
    public static final Duration DEFAULT_MIN_PROGRESS_LOGGING_INTERVAL =
            Duration.ofSeconds(30);

    //--- Properties -----------------------------------------------------------

    /**
     * The crawler unique identifier.
     * Using usual names is perfectly fine (non-alphanumeric characters are OK).
     * It is important for this crawler ID to be unique amongst your
     * crawlers in the same crawl session. On top of avoiding conflicts,
     * it facilitates integration with different systems and facilitates
     * tracking.
     * @param id unique identifier
     * @return unique identifier
     */
    @SuppressWarnings("javadoc")
    private String id;

    /**
     * The maximum number of threads a crawler can use. Default is two.
     * @param numThreads number of threads
     * @return number of threads
     */
    @SuppressWarnings("javadoc")
    private int numThreads = 2;

    /**
     * <p>
     * The maximum number of documents that can be processed before stopping.
     * Not all processed documents make it to your Committers
     * as some can be rejected.
     * </p>
     * <p>
     * In multi-threaded or clustered environments, the actual number
     * of documents processed may be a bit higher than the specified
     * maximum due to concurrency.
     * Upon reaching the configured maximum, the crawler will finish with
     * its documents actively being processed before stopping.
     * </p>
     * <p>
     * Reaching the maximum value does not terminate the crawl session but
     * rather pauses it.  On next run, the crawler will resume the same session,
     * processing an additional number of documents up to the maximum
     * specified.
     * This maximum allows crawling one or more sources
     * in chunks, processing a maximum number of documents each time.
     * When the session fully completes, the next run will start a new
     * crawl session. To prevent resuming an partial crawl session,
     * explicitly clean the crawl session.
     * </p>
     * <p>
     * Default is -1 (unlimited).
     * </p>
     * @param maxDocuments maximum number of documents that can be processed
     * @return maximum number of documents that can be processed
     */
    @SuppressWarnings("javadoc")
    private int maxDocuments = -1;

    /**
     * The maximum depth the crawler should go. The exact definition of depth
     * is crawler-specific. Examples: levels of sub-directories,
     * number of URL clicks to reach a page, etc. Refer to specific crawler
     * implementation for details. Default is -1 (unlimited).
     * @param maxDepth maximum depth or -1 for unlimited depth
     * @return maximum depth or -1 for unlimited depth
     */
    @SuppressWarnings("javadoc")
    private int maxDepth = -1;

    /**
     * The maximum amount of time to wait before shutting down an inactive
     * crawler thread.
     * A document taking longer to process than the specified timeout
     * when no other thread are available to process remaining documents
     * is also considered "inactive". Default is
     * {@value #DEFAULT_IDLE_PROCESSING_TIMEOUT}. A <code>null</code>
     * value means no timeouts.
     * @param idleTimeout time to wait for a document to be processed
     * @return time to wait for a document to be processed
     */
    @SuppressWarnings("javadoc")
    private Duration idleTimeout;

    /**
     * Minimum amount of time to wait between each logging of crawling
     * progress.
     * Default value is {@value #DEFAULT_MIN_PROGRESS_LOGGING_INTERVAL}.
     * A <code>null</code> value disables progress logging. Minimum value
     * is 1 second.
     * @param minProgressLoggingInterval time to wait between each logging
     *     of crawling progress
     * @return time to wait between each logging of crawling progress
     */
    @SuppressWarnings("javadoc")
    private Duration minProgressLoggingInterval;

    /**
     * <p>The strategy to adopt when there are orphans.  Orphans are
     * references that were processed in a previous run, but were not in the
     * current run.  In other words, they are leftovers from a previous run
     * that were not re-encountered in the current.
     * </p><p>
     * Unless explicitly stated otherwise by an implementing class, the default
     * strategy is to <code>PROCESS</code> orphans.
     * Setting a <code>null</code> value is the same as setting
     * <code>IGNORE</code>.
     * </p><p>
     * <b>Be careful:</b> Setting the orphan strategy to <code>DELETE</code>
     * is NOT recommended in most cases. With some collectors, a temporary
     * failure such as a network outage or a web page timing out, may cause
     * some documents not to be crawled. When this happens, unreachable
     * documents would be considered "orphans" and be deleted while under
     * normal circumstances, they should be kept.  Re-processing them
     * (default), is usually the safest approach to confirm they still
     * exist before deleting or updating them.
     * </p>
     * @param orphansStrategy orphans strategy
     * @return orphans strategy
     */
    @SuppressWarnings("javadoc")
    private OrphansStrategy orphansStrategy = OrphansStrategy.PROCESS;

    private final List<Class<? extends Exception>> stopOnExceptions =
            new ArrayList<>();

    /**
     * The crawl data store factory.
     * @param dataStoreEngine crawl data store factory.
     * @return crawl data store factory.
     */
    @SuppressWarnings("javadoc")
    private DataStoreEngine dataStoreEngine = new MVStoreDataStoreEngine();

    private final List<ReferenceFilter> referenceFilters = new ArrayList<>();
    private final List<MetadataFilter> metadataFilters = new ArrayList<>();
    private final List<DocumentFilter> documentFilters = new ArrayList<>();
    private final List<DocumentProcessor> preImportProcessors =
            new ArrayList<>();
    private final List<DocumentProcessor> postImportProcessors =
            new ArrayList<>();

    /**
     * The metadata checksummer.
     * @param metadataChecksummer metadata checksummer
     * @return metadata checksummer
     */
    @SuppressWarnings("javadoc")
    private MetadataChecksummer metadataChecksummer;

    /**
     * The Importer module configuration.
     * @param importerConfig Importer module configuration
     * @return Importer module configuration
     */
    @SuppressWarnings("javadoc")
    private ImporterConfig importerConfig = new ImporterConfig();

    private final List<Committer> committers = new ArrayList<>();

    /**
     * Whether to turn on deduplication based on metadata checksum.
     * Ignored if {@link #getMetadataChecksummer()} returns <code>null</code>.
     * Not recommended unless you know for sure your metadata
     * checksum is acceptably unique.
     * @param metadataDeduplicate <code>true</code> to turn on
     *        metadata-based deduplication
     * @return whether to turn on metadata-based deduplication
     */
    @SuppressWarnings("javadoc")
    private boolean metadataDeduplicate;

    /**
     * Whether to turn on deduplication based on document checksum.
     * Ignored if {@link #getDocumentChecksummer()} returns <code>null</code>.
     * Not recommended unless you know for sure your document
     * checksum is acceptably unique.
     * @param documentDeduplicate <code>true</code> to turn on
     *        document-based deduplication
     * @return whether to turn on document-based deduplication
     */
    @SuppressWarnings("javadoc")
    private boolean documentDeduplicate;

    /**
     * The document checksummer.
     * @param documentChecksummer document checksummer
     * @return document checksummer
     */
    @SuppressWarnings("javadoc")
    private DocumentChecksummer documentChecksummer =
            new MD5DocumentChecksummer();

    /**
     * The spoiled state strategy resolver.
     * @param spoiledReferenceStrategizer spoiled state strategy resolver
     * @return spoiled state strategy resolver
     */
    @SuppressWarnings("javadoc")
    private SpoiledReferenceStrategizer spoiledReferenceStrategizer =
            new GenericSpoiledReferenceStrategizer();

    private final List<EventListener<?>> eventListeners = new ArrayList<>();


    private FetchDirectiveSupport metadataFetchSupport =
            FetchDirectiveSupport.DISABLED;
    private FetchDirectiveSupport documentFetchSupport =
            FetchDirectiveSupport.REQUIRED;

    private final List<Fetcher<?, ?>> fetchers = new ArrayList<>();

    /**
     * The maximum number of times a fetcher will re-attempt fetching
     * a resource in case of failures.  Default is zero (won't retry).
     * @param fetchersMaxRetries maximum number of retries
     * @return maximum number of retries
     */
    @SuppressWarnings("javadoc")
    private int fetchersMaxRetries;

    /**
     * How long to wait before a failing fetcher re-attempts fetching
     * a resource in case of failures (in milliseconds).
     * Default is zero (no delay).
     * @param fetchersRetryDelay retry delay
     * @return retry delay
     */
    @SuppressWarnings("javadoc")
    private long fetchersRetryDelay;

    //--- List Accessors -------------------------------------------------------

    /**
     * The exceptions we want to stop the crawler on.
     * By default the crawler will log exceptions from processing
     * a document and try to move on to the next without stopping.
     * Even if no exceptions are returned by this method,
     * the crawler can sometimes stop regardless if it cannot recover
     * safely from an exception.
     * To capture more exceptions, use a parent class (e.g., Exception
     * should catch them all).
     * @return exceptions that will stop the crawler when encountered
     */
    public List<Class<? extends Exception>> getStopOnExceptions() {
        return Collections.unmodifiableList(stopOnExceptions);
    }
    /**
     * Sets the exceptions we want to stop the crawler on.
     * By default the crawler will log exceptions from processing
     * a document and try to move on to the next without stopping.
     * Even if no exceptions are returned by this method,
     * the crawler can sometimes stop regardless if it cannot recover
     * safely from an exception.
     * To capture more exceptions, use a parent class (e.g., Exception
     * should catch them all).
     * @param stopOnExceptions exceptions that will stop the crawler when
     *         encountered
     */
    public void setStopOnExceptions(
            List<Class<? extends Exception>> stopOnExceptions) {
        CollectionUtil.setAll(this.stopOnExceptions, stopOnExceptions);
    }

    /**
     * Gets reference filters
     * @return reference filters
     */
    public List<ReferenceFilter> getReferenceFilters() {
        return Collections.unmodifiableList(referenceFilters);
    }
    /**
     * Sets reference filters.
     * @param referenceFilters the referenceFilters to set
     */
    public void setReferenceFilters(List<ReferenceFilter> referenceFilters) {
        CollectionUtil.setAll(this.referenceFilters, referenceFilters);
    }

    /**
     * Gets the document filters.
     * @return document filters
     */
    public List<DocumentFilter> getDocumentFilters() {
        return Collections.unmodifiableList(documentFilters);
    }
    /**
     * Sets document filters.
     * @param documentFilters document filters
     */
    public void setDocumentFilters(List<DocumentFilter> documentFilters) {
        CollectionUtil.setAll(this.documentFilters, documentFilters);
    }

    /**
     * Gets metadata filters.
     * @return metadata filters
     */
    public List<MetadataFilter> getMetadataFilters() {
        return Collections.unmodifiableList(metadataFilters);
    }
    /**
     * Sets metadata filters.
     * @param metadataFilters metadata filters
     */
    public void setMetadataFilters(List<MetadataFilter> metadataFilters) {
        CollectionUtil.setAll(this.metadataFilters, metadataFilters);
    }

    /**
     * Gets Committers responsible for persisting information
     * to a target location/repository.
     * @return list of Committers (never <code>null</code>)
     */
    public List<Committer> getCommitters() {
        return Collections.unmodifiableList(committers);
    }
    /**
     * Sets Committers responsible for persisting information
     * to a target location/repository.
     * @param committers list of Committers
     */
    public void setCommitters(List<Committer> committers) {
        CollectionUtil.setAll(this.committers, committers);
    }

    /**
     * Gets event listeners.
     * Those are considered additions to automatically
     * detected configuration objects implementing {@link EventListener}.
     * @return event listeners.
     */
    public List<EventListener<?>> getEventListeners() {
        return Collections.unmodifiableList(eventListeners);
    }
    /**
     * Sets event listeners.
     * Those are considered additions to automatically
     * detected configuration objects implementing {@link EventListener}.
     * @param eventListeners event listeners.
     */
    public void setEventListeners(List<EventListener<?>> eventListeners) {
        CollectionUtil.setAll(this.eventListeners, eventListeners);
    }
    /**
     * Adds event listeners.
     * Those are considered additions to automatically
     * detected configuration objects implementing {@link EventListener}.
     * @param eventListeners event listeners.
     */
    public void addEventListeners(List<EventListener<?>> eventListeners) {
        this.eventListeners.addAll(eventListeners);
    }
    /**
     * Adds an event listener.
     * Those are considered additions to automatically
     * detected configuration objects implementing {@link EventListener}.
     * @param eventListener event listener.
     */
    public void addEventListener(EventListener<?> eventListener) {
        eventListeners.add(eventListener);
    }
    /**
     * Clears all event listeners. The automatically
     * detected configuration objects implementing {@link EventListener}
     * are not cleared.
     */
    public void clearEventListeners() {
        eventListeners.clear();
    }

    /**
     * Gets pre-import processors.
     * @return pre-import processors
     */
    public List<DocumentProcessor> getPreImportProcessors() {
        return Collections.unmodifiableList(preImportProcessors);
    }
    /**
     * Sets pre-import processors.
     * @param preImportProcessors pre-import processors
     */
    public void setPreImportProcessors(
            DocumentProcessor... preImportProcessors) {
        setPreImportProcessors(Arrays.asList(preImportProcessors));
    }
    /**
     * Sets pre-import processors.
     * @param preImportProcessors pre-import processors
     */
    public void setPreImportProcessors(
            List<DocumentProcessor> preImportProcessors) {
        CollectionUtil.setAll(this.preImportProcessors, preImportProcessors);
        CollectionUtil.removeNulls(this.preImportProcessors);
    }

    /**
     * Gets post-import processors.
     * @return post-import processors
     */
    public List<DocumentProcessor> getPostImportProcessors() {
        return Collections.unmodifiableList(postImportProcessors);
    }
    /**
     * Sets post-import processors.
     * @param postImportProcessors post-import processors
     */
    public void setPostImportProcessors(
            DocumentProcessor... postImportProcessors) {
        setPostImportProcessors(Arrays.asList(postImportProcessors));
    }
    /**
     * Sets post-import processors.
     * @param postImportProcessors post-import processors
     */
    public void setPostImportProcessors(
            List<DocumentProcessor> postImportProcessors) {
        CollectionUtil.setAll(this.postImportProcessors, postImportProcessors);
        CollectionUtil.removeNulls(this.postImportProcessors);
    }

    /**
     * One or more fetchers responsible for pulling documents and document
     * metadata associated with a reference from a source.
     * When more than one are configured and for each documents, fetchers will
     * be invoked in their defined order, until the first one that accepts and
     * successfully process a reference (others are not invoked).
     * @return one or more fetchers
     */
    /**
     * Gets reference filters
     * @return reference filters
     */
    public List<Fetcher<?, ?>> getFetchers() {
        return Collections.unmodifiableList(fetchers);
    }
    /**
     * One or more fetchers responsible for pulling documents and document
     * metadata associated with a reference from a source.
     * When more than one are configured and for each documents, fetchers will
     * be invoked in their defined order, until the first one that accepts and
     * successfully process a reference (others are not invoked).
     * @param fetchers one or more fetchers
     */
    public void setFetchers(List<Fetcher<?, ?>> fetchers) {
        CollectionUtil.setAll(this.fetchers, fetchers);
    }
    /**
     * One or more fetchers responsible for pulling documents and document
     * metadata associated with a reference from a source.
     * When more than one are configured and for each documents, fetchers will
     * be invoked in their defined order, until the first one that accepts and
     * successfully process a reference (others are not invoked).
     * @param fetchers one or more fetchers
     */
    public void setFetchers(Fetcher<?, ?>... fetchers) {
        CollectionUtil.setAll(this.fetchers, fetchers);
    }

    //--- XML Persist ----------------------------------------------------------

    @Override
    public void saveToXML(XML xml) {
        xml.setAttribute(Fields.id, id);
        xml.addElement(Fields.numThreads, numThreads);
        xml.addElement(Fields.maxDocuments, maxDocuments);
        xml.addElement(Fields.maxDepth, maxDepth);
        xml.addElement(Fields.idleTimeout, idleTimeout);
        xml.addElement(Fields.minProgressLoggingInterval,
                minProgressLoggingInterval);
        xml.addElementList(
                Fields.stopOnExceptions, "exception", stopOnExceptions);
        xml.addElement(Fields.orphansStrategy, orphansStrategy);
        xml.addElement(Fields.dataStoreEngine, dataStoreEngine);
        xml.addElementList(Fields.referenceFilters, "filter", referenceFilters);
        xml.addElementList(Fields.metadataFilters, "filter", metadataFilters);
        xml.addElementList(Fields.documentFilters, "filter", documentFilters);
        if (importerConfig != null) {
            xml.addElement("importer", importerConfig);
        }
        xml.addElementList(Fields.committers, "committer", committers);
        xml.addElement(Fields.metadataChecksummer, metadataChecksummer);
        xml.addElement(Fields.metadataDeduplicate, metadataDeduplicate);
        xml.addElement(Fields.documentChecksummer, documentChecksummer);
        xml.addElement(Fields.documentDeduplicate, documentDeduplicate);
        xml.addElement(Fields.spoiledReferenceStrategizer,
                spoiledReferenceStrategizer);

        xml.addElementList(Fields.eventListeners, "listener", eventListeners);

        xml.addElement(Fields.metadataFetchSupport, metadataFetchSupport);
        xml.addElement(Fields.documentFetchSupport, documentFetchSupport);

        xml.addElementList(
                Fields.preImportProcessors, "processor", preImportProcessors);
        xml.addElementList(
                Fields.postImportProcessors, "processor", postImportProcessors);

        xml.addElement(Fields.fetchers)
            .setAttribute("maxRetries", fetchersMaxRetries)
            .setAttribute("retryDelay", fetchersRetryDelay)
            .addElementList("fetcher", fetchers);
    }

    @Override
    public void loadFromXML(XML xml) {
        setId(xml.getString(XPathUtil.attr(Fields.id), id));
        setNumThreads(xml.getInteger(Fields.numThreads, numThreads));
        setMaxDocuments(xml.getInteger(Fields.maxDocuments, maxDocuments));
        setMaxDepth(xml.getInteger(Fields.maxDepth, maxDepth));
        setOrphansStrategy(xml.getEnum(Fields.orphansStrategy,
                OrphansStrategy.class, orphansStrategy));
        setIdleTimeout(xml.getDuration(Fields.idleTimeout, idleTimeout));
        setMinProgressLoggingInterval(xml.getDuration(
                Fields.minProgressLoggingInterval, minProgressLoggingInterval));
        setStopOnExceptions(xml.getClassList(
                "stopOnExceptions/exception", stopOnExceptions));
        setReferenceFilters(xml.getObjectListImpl(ReferenceFilter.class,
                "referenceFilters/filter", referenceFilters));
        setMetadataFilters(xml.getObjectListImpl(MetadataFilter.class,
                "metadataFilters/filter", metadataFilters));
        setDocumentFilters(xml.getObjectListImpl(DocumentFilter.class,
                "documentFilters/filter", documentFilters));

        var importerXML = xml.getXML("importer");
        if (importerXML != null) {
            var cfg = new ImporterConfig();
            importerXML.populate(cfg);
            setImporterConfig(cfg);
            //MAYBE handle ignore errors
        } else if (getImporterConfig() == null) {
            setImporterConfig(new ImporterConfig());
        }

        setDataStoreEngine(xml.getObjectImpl(
                DataStoreEngine.class, "dataStoreEngine", dataStoreEngine));
        setCommitters(xml.getObjectListImpl(Committer.class,
                "committers/committer", committers));
        setMetadataChecksummer(xml.getObjectImpl(MetadataChecksummer.class,
                Fields.metadataChecksummer, metadataChecksummer));
        setMetadataDeduplicate(xml.getBoolean("metadataDeduplicate",
                metadataDeduplicate));
        setDocumentChecksummer(xml.getObjectImpl(DocumentChecksummer.class,
                Fields.documentChecksummer, documentChecksummer));
        setDocumentDeduplicate(xml.getBoolean("documentDeduplicate",
                documentDeduplicate));
        setSpoiledReferenceStrategizer(xml.getObjectImpl(
                SpoiledReferenceStrategizer.class,
                Fields.spoiledReferenceStrategizer,
                spoiledReferenceStrategizer));

        setEventListeners(xml.getObjectListImpl(EventListener.class,
                "eventListeners/listener", eventListeners));

        setMetadataFetchSupport(xml.getEnum(
                Fields.metadataFetchSupport,
                FetchDirectiveSupport.class,
                metadataFetchSupport));
        setDocumentFetchSupport(xml.getEnum(
                Fields.documentFetchSupport,
                FetchDirectiveSupport.class,
                documentFetchSupport));

        setPreImportProcessors(xml.getObjectListImpl(DocumentProcessor.class,
                "preImportProcessors/processor", preImportProcessors));
        setPostImportProcessors(xml.getObjectListImpl(DocumentProcessor.class,
                "postImportProcessors/processor", postImportProcessors));

        setFetchers(xml.getObjectListImpl(
                Fetcher.class, "fetchers/fetcher", fetchers));
        setFetchersMaxRetries(xml.getInteger(
                "fetchers/@maxRetries", fetchersMaxRetries));
        setFetchersRetryDelay(xml.getDurationMillis(
                "fetchers/@retryDelay", fetchersRetryDelay));
    }
}