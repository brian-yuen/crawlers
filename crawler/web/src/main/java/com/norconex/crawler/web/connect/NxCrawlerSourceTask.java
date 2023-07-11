package com.norconex.crawler.web.connect;

import com.norconex.committer.core.Committer;
import com.norconex.committer.core.fs.impl.XMLFileCommitter;
import com.norconex.commons.lang.xml.XML;
import com.norconex.crawler.core.crawler.CrawlerConfig;
import com.norconex.crawler.core.session.CrawlSession;
import com.norconex.crawler.core.session.CrawlSessionConfig;
import com.norconex.crawler.web.WebCrawlSession;
import com.norconex.crawler.web.crawler.URLCrawlScopeStrategy;
import com.norconex.crawler.web.crawler.WebCrawlerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class NxCrawlerSourceTask extends SourceTask {
    private CrawlSession crawlSession;
    private WebCrawlerConfig cc;
    private static final String CRAWLER_ID = "crawlerId";
    private static final String START_URLS = "startURLs";
    private static final String TOPIC = "topic";
    private static final String WORK_DIR = "workDir";
    private static final String MAX_DEPTH = "maxDepth";
    private static final String MAX_DOCUMENTS = "maxDocuments";
    private static final String CONFIG_PATH = "configPath";

    private static boolean PROCESS_COMPLETED = false;
    //    private KafkaConnectCommitter kafkaConnectCommitter;
    private static boolean shouldWait;

    static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(START_URLS, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, "Start URLs.")
            .define(CRAWLER_ID, ConfigDef.Type.STRING, "1", new ConfigDef.NonEmptyString(), ConfigDef.Importance.LOW, "Crawler Id (Optional)")
            .define(WORK_DIR, ConfigDef.Type.STRING, "./work", new ConfigDef.NonEmptyString(), ConfigDef.Importance.LOW, "Work Dir")
            .define(MAX_DEPTH, ConfigDef.Type.INT, 0, ConfigDef.Importance.LOW, "Max Depth")
            .define(MAX_DOCUMENTS, ConfigDef.Type.INT, -1, ConfigDef.Importance.MEDIUM, "MAX_DOCUMENTS")
            .define(CONFIG_PATH, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Crawler config file path.");


    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> map) {
        AbstractConfig config = new AbstractConfig(CONFIG_DEF, map);

//        System.out.println("WORKDIR: " + config.getString(WORK_DIR));
//
//        CrawlSessionConfig myConfig = new CrawlSessionConfig();
//
//        myConfig.setId(config.getString(CRAWLER_ID));
//        myConfig.setWorkDir(Paths.get(config.getString(WORK_DIR)));
//
//        List<CrawlerConfig> crawlerConfigs = new ArrayList<>();
//
//        cc = new WebCrawlerConfig();
//        cc.setId(config.getString(CRAWLER_ID));
//        cc.setStartURLs(config.getString(START_URLS).split(","));
//        cc.setMaxDepth(config.getInt(MAX_DEPTH));
//        cc.setMaxDocuments(config.getInt(MAX_DOCUMENTS));
//        cc.setIgnoreSitemap(true);
//
//        var s = new URLCrawlScopeStrategy();
//        s.setStayOnDomain(true);
//        cc.setUrlCrawlScopeStrategy(s);
//
//        List<Committer> committers = new ArrayList<>();
//        XMLFileCommitter xmlFileCommitter = new XMLFileCommitter();
//        xmlFileCommitter.setIndent(4);
//        committers.add(xmlFileCommitter);

//        kafkaConnectCommitter = new KafkaConnectCommitter("crawler");
//        committers.add(kafkaConnectCommitter);

//        cc.setCommitters(committers);
//
//        crawlerConfigs.add(cc);
//        myConfig.setCrawlerConfigs(crawlerConfigs);
//
//
//        crawlSession = WebCrawlSession.createSession(myConfig);

        String configFilePath = config.getString(CONFIG_PATH);
        CrawlSessionConfig csc = new CrawlSessionConfig(WebCrawlerConfig.class);
        csc.loadFromXML(new XML(Paths.get(configFilePath)));

        csc.setWorkDir(Paths.get(config.getString(WORK_DIR)));
        shouldWait = !config.getString(WORK_DIR).endsWith("1");

        /*
        TODO: Need to set unique crawler id when there are multiple
            Crawlers in the same file.
        */
//        for (CrawlerConfig cc : csc.getCrawlerConfigs()) {
//            cc.setId(cc.getId() + "-" + crawlerIdPostfix);
//        }

        if (shouldWait) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        crawlSession = WebCrawlSession.createSession(csc);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        // TODO: From where to get new records?
        // 1. From kafka data store?
        // 2. From queue which has multiple partitions? Each task has 1 partition associated with it.
        // So at the time of pushing record, we will have KafkaConnectCommitter, which push records to
        // those partitions in round robin fashion.
        // Here, we need to understand that how we will satisfy max depth, stay in domain and other conditions.

        LOG.info("Checking condition...");

        if (!PROCESS_COMPLETED) {
            LOG.info("Starting crawling...");
            if (!crawlSession.isRunning()) {
                if (shouldWait) {
                    Thread.sleep(2000);
                }

                crawlSession.start();
            } else {
                Thread.sleep(5000);
            }

            PROCESS_COMPLETED = true;

            stop();
            LOG.info("Returning null...");
            return null;
        }

        Thread.sleep(5000);
        LOG.info("Returning null from outside...");
        return null;
    }

    @Override
    public void stop() {
        if (crawlSession.isRunning()) {
            crawlSession.stop();
        }
    }
}
