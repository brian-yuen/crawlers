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
package com.norconex.crawler.core.crawler;

import static java.util.Optional.ofNullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import com.norconex.committer.core.CommitterContext;
import com.norconex.committer.core.DeleteRequest;
import com.norconex.committer.core.UpsertRequest;
import com.norconex.committer.core.service.CommitterService;
import com.norconex.commons.lang.event.Event;
import com.norconex.commons.lang.event.EventManager;
import com.norconex.commons.lang.file.FileUtil;
import com.norconex.commons.lang.io.CachedStreamFactory;
import com.norconex.crawler.core.crawler.CrawlerConfig.OrphansStrategy;
import com.norconex.crawler.core.doc.CrawlDoc;
import com.norconex.crawler.core.doc.CrawlDocRecord;
import com.norconex.crawler.core.doc.CrawlDocRecordFactory;
import com.norconex.crawler.core.doc.CrawlDocRecordService;
import com.norconex.crawler.core.fetch.FetchRequest;
import com.norconex.crawler.core.fetch.FetchResponse;
import com.norconex.crawler.core.fetch.Fetcher;
import com.norconex.crawler.core.monitor.CrawlerMonitor;
import com.norconex.crawler.core.monitor.CrawlerMonitorJMX;
import com.norconex.crawler.core.monitor.MdcUtil;
import com.norconex.crawler.core.pipeline.DocRecordPipelineContext;
import com.norconex.crawler.core.pipeline.DocumentPipelineContext;
import com.norconex.crawler.core.pipeline.importer.ImporterPipelineContext;
import com.norconex.crawler.core.session.CrawlSession;
import com.norconex.crawler.core.session.CrawlSessionException;
import com.norconex.crawler.core.store.DataStore;
import com.norconex.crawler.core.store.DataStoreEngine;
import com.norconex.crawler.core.store.DataStoreExporter;
import com.norconex.crawler.core.store.DataStoreImporter;
import com.norconex.importer.Importer;
import com.norconex.importer.response.ImporterResponse;
import lombok.NonNull;

/**
 * <p>Abstract crawler implementation providing a common base to building
 * crawlers.</p>
 *
 * <h3>JMX Support</h3>
 * <p>JMX support is disabled by default.  To enable it,
 * set the system property "enableJMX" to <code>true</code>.  You can do so
 * by adding this to your Java launch command:
 * </p>
 * <pre>
 *     -DenableJMX=true
 * </pre>
 *
 * @see CrawlerConfig
 */
@SuppressWarnings("javadoc")
public class Crawler {
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Crawler.class);
    public static final String SYS_PROP_ENABLE_JMX = "enableJMX";
    //--- Properties set in Constructor ----------------------------------------
    /**
     * Gets the crawl session this crawler is part of.
     */
    private final CrawlSession crawlSession;
    /**
     * Gets the crawler configuration.
     */
    private final CrawlerConfig configuration;
    private final CrawlerImpl crawlerImpl;
    /**
     * Gets the directory where files needing to be persisted between
     * crawling sessions are kept.
     */
    private Path workDir;
    /**
     * Gets the directory where most temporary files are created for the
     * duration of a crawling session. Those files are typically deleted
     * after a crawling session.
     */
    private Path tempDir;
    private CommitterService<CrawlDoc> committerService;
    //--- Properties set on Init -----------------------------------------------
    /**
     * Crawler implementation-specific context holding state-data not relevant
     * to Crawler Core.
     */
    private Object crawlerContext;
    private DataStoreEngine dataStoreEngine;
    private CrawlDocRecordService docRecordService;
    //--- Properties set on Start ----------------------------------------------
    /**
     * Gets the crawler Importer module.
     */
    private Importer importer;
    private CrawlerMonitor monitor;
    private CrawlProgressLogger progressLogger;
    private Fetcher<? extends FetchRequest, ? extends FetchResponse> fetcher;
    private DataStore<String> dedupMetadataStore;
    private DataStore<String> dedupDocumentStore;
    private MutableBoolean queueInitialized;
    // Actual maximum number of docs after which to stop, which starts
    // at initial "processed count" + configured max. On clean runs or
    // after a session fully completed (whether it was resumed a few times
    // or not), this value will be the same as the max configured.
    //
    private final MutableInt resumableMaxDocuments = new MutableInt();
    //--- Properties set on Stop -----------------------------------------------
    private boolean stopped;

    protected Crawler(@NonNull CrawlSession crawlSession, @NonNull CrawlerConfig crawlerConfig, @NonNull CrawlerImpl crawlerImpl) {
        java.util.Objects.requireNonNull(crawlSession, "crawlSession is marked non-null but is null");
        java.util.Objects.requireNonNull(crawlerConfig, "crawlerConfig is marked non-null but is null");
        java.util.Objects.requireNonNull(crawlerImpl, "crawlerImpl is marked non-null but is null");
        this.crawlSession = crawlSession;
        configuration = crawlerConfig;
        this.crawlerImpl = crawlerImpl;
        if (StringUtils.isBlank(getId())) {
            throw new CrawlerException("Crawler must be given " + "a unique identifier (id).");
        }
        committerService = CommitterService.<CrawlDoc>builder().committers(crawlerConfig.getCommitters()).eventManager(crawlSession.getEventManager()).upsertRequestBuilder(doc -> new UpsertRequest(doc.getReference(), doc.getMetadata(), doc.getInputStream())).deleteRequestBuilder(doc -> new DeleteRequest(doc.getReference(), doc.getMetadata())).build();
        workDir = crawlSession.getWorkDir().resolve(FileUtil.toSafeFileName(getId()));
        tempDir = workDir.resolve("temp");
    }

    //--- Set at construction --------------------------------------------------
    public String getId() {
        return configuration.getId();
    }

    /**
     * Gets the event manager.
     * @return event manager
     */
    public EventManager getEventManager() {
        return crawlSession.getEventManager();
    }

    public CachedStreamFactory getStreamFactory() {
        return crawlSession.getStreamFactory();
    }

    public void queueDocRecord(CrawlDocRecord rec) {
        crawlerImpl.queuePipeline().accept(new DocRecordPipelineContext(this, rec));
    }

    //MAYBE: Keep this one or always force to pass context?
    public ImporterResponse importDoc(CrawlDoc doc) {
        return importDoc(new ImporterPipelineContext(this, doc));
    }

    public ImporterResponse importDoc(ImporterPipelineContext ctx) {
        return crawlerImpl.importerPipeline().apply(ctx);
    }

    public void commitDoc(CrawlDoc doc) {
        crawlerImpl.committerPipeline().accept(new DocumentPipelineContext(this, doc));
    }

    CrawlDocRecordFactory getDocRecordFactory() {
        return crawlerImpl.docRecordFactory();
    }

    void logProgress() {
        if (progressLogger != null) {
            progressLogger.logProgress();
        } else {
            LOG.debug("ProgressLogger is null.");
        }
    }

    // invoked as the first thing for every commands.
    boolean initCrawler(Runnable initAction) {
        // Ensure clean slate by either replacing or clearing and adding back
        Thread.currentThread().setName(getId());
        MdcUtil.setCrawlerId(getId());
        //--- Directories ---
        createDirectory(workDir);
        createDirectory(tempDir);
        //--- Crawler implementation-specific context ---
        crawlerContext = crawlerImpl.crawlerImplContext().get();
        fire(CrawlerEvent.CRAWLER_INIT_BEGIN);
        //--- Store engine ---
        dataStoreEngine = configuration.getDataStoreEngine();
        dataStoreEngine.init(this);
        docRecordService = new CrawlDocRecordService(this, crawlerImpl.crawlDocRecordType());
        //--- Committers ---
        //TODO come up with committer-specific ID instead of index
        // index will be appended to committer workdir for each one
        var committerContext = CommitterContext.builder().setEventManager(getEventManager()).setWorkDir(getWorkDir().resolve("committer")).setStreamFactory(getStreamFactory()).build();
        committerService.init(committerContext);
        var resuming = docRecordService.open();
        if (initAction != null) {
            initAction.run();
        }
        fire(CrawlerEvent.CRAWLER_INIT_END);
        return resuming;
    }

    /**
     * Starts crawling.
     */
    public void start() {
        var resume = new MutableBoolean();
        try {
            initCrawler(() -> {
                resume.setValue(docRecordService.prepareForCrawlerStart());
                importer = new Importer(getConfiguration().getImporterConfig(), getEventManager());
                monitor = new CrawlerMonitor(this);
                //TODO make general logging messages verbosity configurable
                progressLogger = new CrawlProgressLogger(monitor, getConfiguration().getMinProgressLoggingInterval());
                progressLogger.startTracking();
                if (Boolean.getBoolean(SYS_PROP_ENABLE_JMX)) {
                    CrawlerMonitorJMX.register(this);
                }
                logContextInfo();
                fetcher = crawlerImpl.fetcherProvider().apply(this);
                dedupMetadataStore = resolveMetaDedupStore();
                dedupDocumentStore = resolveDocumentDedupStore();
                Optional.ofNullable(crawlerImpl.beforeCrawlerExecution).ifPresent(c -> c.accept(this, resume.getValue()));
                // max documents
                var cfgMaxDocs = getConfiguration().getMaxDocuments();
                var resumeMaxDocs = cfgMaxDocs;
                if (cfgMaxDocs > -1 && resume.booleanValue()) {
                    resumeMaxDocs += monitor.getProcessedCount();
                    LOG.info("""
                        Adding configured maximum documents ({}) to this resumed session. The combined maximum documents for this run before stopping: {}
                        """, cfgMaxDocs, resumeMaxDocs);
                }
                resumableMaxDocuments.setValue(resumeMaxDocs);
            });
            fire(CrawlerEvent.CRAWLER_RUN_BEGIN);
            //--- Queue initial references ---------------------------------
            //TODO if we resume, shall we not queue again? What if it stopped
            // in the middle of initial queuing, then to be safe we have to
            // queue again and expect that those that were already processed
            // will simply be ignored (default behavior).
            // Consider persisting a flag that would tell us if we are resuming
            // with an incomplete queue initialization, or make initialization
            // more sophisticated so we can resume in the middle of it
            // (this last option would likely be very impractical).
            LOG.info("Queueing initial references...");
            queueInitialized = ofNullable(crawlerImpl.queueInitializer()).map(qizer -> qizer.apply(new CrawlerImpl.QueueInitContext(Crawler.this, resume.getValue(), rec -> crawlerImpl.queuePipeline().accept(new DocRecordPipelineContext(Crawler.this, rec))))).orElse(new MutableBoolean(true));
            //--- Process start/queued references ------------------------------
            LOG.info("Crawling references...");
            processReferences(new ProcessFlags());
            if (!isStopped()) {
                handleOrphans();
            }
            fire((isStopped() ? CrawlerEvent.CRAWLER_STOP_END : CrawlerEvent.CRAWLER_RUN_END));
            LOG.info("Crawler {}", (isStopped() ? "stopped." : "completed."));
        } finally {
            try {
                Optional.ofNullable(crawlerImpl.afterCrawlerExecution).ifPresent(c -> c.accept(this));
            } finally {
                if (progressLogger != null) {
                    progressLogger.stopTracking();
                    try {
                        LOG.info("Execution Summary:{}", progressLogger.getExecutionSummary());
                    } finally {
                        destroyCrawler();
                    }
                }
            }
        }
        // Note: unregistering of JMX monitor bean is done in CrawlSession.
    }

    void processReferences(final ProcessFlags flags) {
        var numThreads = getConfiguration().getNumThreads();
        final var latch = new CountDownLatch(numThreads);
        var execService = Executors.newFixedThreadPool(numThreads);
        try {
            for (var i = 0; i < numThreads; i++) {
                final var threadIndex = i + 1;
                LOG.debug("Crawler thread #{} starting...", threadIndex);
                execService.execute(CrawlerThread.builder().crawler(this).latch(latch).threadIndex(threadIndex).deleting(flags.delete).orphan(flags.orphan).build());
            }
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CrawlerException(e);
        } finally {
            execService.shutdown();
            // necessary to ensure thread end event is not sometimes fired
            // after crawler run end.
            try {
                execService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.error("Failed to wait for crawler termination.", e);
            }
        }
    }

    /**
     * Whether the crawler job was stopped.
     * @return <code>true</code> if stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Stops a running crawler.
     */
    public void stop() {
        fire(CrawlerEvent.CRAWLER_STOP_BEGIN);
        stopped = true;
        LOG.info("Stopping the crawler.");
    }

    /**
     * Cleans the crawler cache information, leading to the next run
     * being as if the crawler was run for the first time.
     */
    public void clean() {
        initCrawler(null);
        getEventManager().fire(CrawlerEvent.builder().name(CrawlerEvent.CRAWLER_CLEAN_BEGIN).source(this).message("Cleaning cached crawler \"" + getId() + "\" data...").build());
        try {
            committerService.clean();
            dataStoreEngine.clean();
            destroyCrawler();
            FileUtils.deleteDirectory(getWorkDir().toFile());
            getEventManager().fire(CrawlerEvent.builder().name(CrawlerEvent.CRAWLER_CLEAN_END).source(this).message("Done cleaning crawler \"" + getId() + "\".").build());
        } catch (IOException e) {
            throw new CrawlerException("Could not clean \"" + getId() + "\" crawler directory.", e);
        }
    }

    void handleOrphans() {
        var strategy = configuration.getOrphansStrategy();
        if (strategy == null) {
            // null is same as ignore
            strategy = OrphansStrategy.IGNORE;
        }
        // If PROCESS, we do not care to validate if really orphan since
        // all cache items will be reprocessed regardless
        if (strategy == OrphansStrategy.PROCESS) {
            reprocessCacheOrphans();
            return;
        }
        if (strategy == OrphansStrategy.DELETE) {
            deleteCacheOrphans();
        }
        // else, ignore (i.e. don't do anything)
        //TODO log how many where ignored (cache count)
    }

    void reprocessCacheOrphans() {
        if (isMaxDocuments()) {
            LOG.info("Max documents reached. " + "Not reprocessing orphans (if any).");
            return;
        }
        LOG.info("Reprocessing any cached/orphan references...");
        var count = new MutableLong();
        docRecordService.forEachCached((ref, docInfo) -> {
            crawlerImpl.queuePipeline().accept(new DocRecordPipelineContext(this, docInfo));
            count.increment();
            return true;
        });
        if (count.longValue() > 0) {
            processReferences(new ProcessFlags().orphan());
        }
        LOG.info("Reprocessed {} cached/orphan references.", count);
    }

    void deleteCacheOrphans() {
        LOG.info("Deleting orphan references (if any)...");
        var count = new MutableLong();
        docRecordService.forEachCached((k, v) -> {
            docRecordService.queue(v);
            count.increment();
            return true;
        });
        if (count.longValue() > 0) {
            processReferences(new ProcessFlags().delete());
        }
        LOG.info("Deleted {} orphan references.", count);
    }

    //TODO duplicate method, move to util class
    boolean isMaxDocuments() {
        //TODO replace check for "processedCount" vs "maxDocuments"
        // with event counts vs max committed, max processed, max etc...
        // Check if we merge with StopCrawlerOnMaxEventListener
        // or if we remove maxDocument in favor of the listener.
        // what about clustering?
        var maxDocs = resumableMaxDocuments.intValue();
        var isMax = maxDocs > -1 && monitor.getProcessedCount() >= maxDocs;
        if (isMax) {
            LOG.info("Maximum documents reached for this session: {}", maxDocs);
        }
        return isMax;
    }

    public void importDataStore(Path inFile) {
        initCrawler(null);
        try {
            DataStoreImporter.importDataStore(this, inFile);
        } catch (IOException e) {
            throw new CrawlerException("Could not import data store.", e);
        } finally {
            destroyCrawler();
        }
    }

    public Path exportDataStore(Path dir) {
        initCrawler(null);
        try {
            return DataStoreExporter.exportDataStore(this, dir);
        } catch (IOException e) {
            throw new CrawlerException("Could not export data store.", e);
        } finally {
            destroyCrawler();
        }
    }

    void destroyCrawler() {
        ofNullable(docRecordService).ifPresent(CrawlDocRecordService::close);
        ofNullable(dataStoreEngine).ifPresent(DataStoreEngine::close);
        //TODO shall we clear crawler listeners, or leave to collector impl
        // to clean all?
        // eventManager.clearListeners();
        ofNullable(committerService).ifPresent(CommitterService::close);
    }

    // store made of: checksum -> ref
    private DataStore<String> resolveMetaDedupStore() {
        if (configuration.isMetadataDeduplicate() && configuration.getMetadataChecksummer() != null) {
            return getDataStoreEngine().openStore("dedup-metadata", String.class);
        }
        return null;
    }

    // store made of: checksum -> ref
    private DataStore<String> resolveDocumentDedupStore() {
        if (configuration.isDocumentDeduplicate() && configuration.getDocumentChecksummer() != null) {
            return getDataStoreEngine().openStore("dedup-document", String.class);
        }
        return null;
    }

    private void logContextInfo() {
        if (Boolean.getBoolean(SYS_PROP_ENABLE_JMX)) {
            LOG.info("JMX support enabled.");
        } else {
            LOG.info("JMX support disabled. To enable, set -DenableJMX=true " + "system property as JVM argument.");
        }
    }

    public void fire(Event event) {
        getEventManager().fire(event);
    }

    public void fire(String eventName) {
        fire(CrawlerEvent.builder().name(eventName).source(this).build());
    }

    public void fire(String eventName, Object subject) {
        fire(CrawlerEvent.builder().name(eventName).source(this).subject(subject).build());
    }

    public boolean isQueueInitialized() {
        // exceptions aside, this is never null when start method was called
        return ofNullable(queueInitialized).map(MutableBoolean::booleanValue).orElse(false);
    }

    //TODO make enum if never mixed, and add "default"
    //TODO or add this to CrawlDoc
    public static final class ProcessFlags {
        private boolean delete;
        private boolean orphan;

        private ProcessFlags delete() {
            delete = true;
            return this;
        }

        private ProcessFlags orphan() {
            orphan = true;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean isDelete() {
            return this.delete;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean isOrphan() {
            return this.orphan;
        }
    }

    private static void createDirectory(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new CrawlSessionException("Could not create directory: " + dir, e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(getId()).build();
    }


    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public static class CrawlerBuilder {
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        private CrawlSession crawlSession;
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        private CrawlerConfig crawlerConfig;
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        private CrawlerImpl crawlerImpl;

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        CrawlerBuilder() {
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Crawler.CrawlerBuilder crawlSession(@NonNull final CrawlSession crawlSession) {
            java.util.Objects.requireNonNull(crawlSession, "crawlSession is marked non-null but is null");
            this.crawlSession = crawlSession;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Crawler.CrawlerBuilder crawlerConfig(@NonNull final CrawlerConfig crawlerConfig) {
            java.util.Objects.requireNonNull(crawlerConfig, "crawlerConfig is marked non-null but is null");
            this.crawlerConfig = crawlerConfig;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Crawler.CrawlerBuilder crawlerImpl(@NonNull final CrawlerImpl crawlerImpl) {
            java.util.Objects.requireNonNull(crawlerImpl, "crawlerImpl is marked non-null but is null");
            this.crawlerImpl = crawlerImpl;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Crawler build() {
            return new Crawler(this.crawlSession, this.crawlerConfig, this.crawlerImpl);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "Crawler.CrawlerBuilder(crawlSession=" + this.crawlSession + ", crawlerConfig=" + this.crawlerConfig + ", crawlerImpl=" + this.crawlerImpl + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public static Crawler.CrawlerBuilder builder() {
        return new Crawler.CrawlerBuilder();
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Crawler)) return false;
        final Crawler other = (Crawler) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$configuration = this.getConfiguration();
        final java.lang.Object other$configuration = other.getConfiguration();
        if (this$configuration == null ? other$configuration != null : !this$configuration.equals(other$configuration)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof Crawler;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $configuration = this.getConfiguration();
        result = result * PRIME + ($configuration == null ? 43 : $configuration.hashCode());
        return result;
    }

    /**
     * Gets the crawl session this crawler is part of.
     * @return crawl session
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CrawlSession getCrawlSession() {
        return this.crawlSession;
    }

    /**
     * Gets the crawler configuration.
     * @return the crawler configuration
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CrawlerConfig getConfiguration() {
        return this.configuration;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    CrawlerImpl getCrawlerImpl() {
        return this.crawlerImpl;
    }

    /**
     * Gets the directory where files needing to be persisted between
     * crawling sessions are kept.
     * @return working directory, never <code>null</code>
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Path getWorkDir() {
        return this.workDir;
    }

    /**
     * Gets the directory where most temporary files are created for the
     * duration of a crawling session. Those files are typically deleted
     * after a crawling session.
     * @return temporary directory, never <code>null</code>
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Path getTempDir() {
        return this.tempDir;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CommitterService<CrawlDoc> getCommitterService() {
        return this.committerService;
    }

    /**
     * Crawler implementation-specific context holding state-data not relevant
     * to Crawler Core.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Object getCrawlerContext() {
        return this.crawlerContext;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DataStoreEngine getDataStoreEngine() {
        return this.dataStoreEngine;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CrawlDocRecordService getDocRecordService() {
        return this.docRecordService;
    }

    /**
     * Gets the crawler Importer module.
     * @return the Importer
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Importer getImporter() {
        return this.importer;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CrawlerMonitor getMonitor() {
        return this.monitor;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CrawlProgressLogger getProgressLogger() {
        return this.progressLogger;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Fetcher<? extends FetchRequest, ? extends FetchResponse> getFetcher() {
        return this.fetcher;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DataStore<String> getDedupMetadataStore() {
        return this.dedupMetadataStore;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DataStore<String> getDedupDocumentStore() {
        return this.dedupDocumentStore;
    }
}
