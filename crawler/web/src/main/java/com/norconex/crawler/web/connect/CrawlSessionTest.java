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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class CrawlSessionTest {
    private static CrawlSession crawlSession;
    private static WebCrawlerConfig cc;
    private static final String CRAWLER_ID = "1";
    private static final String START_URLS = "https://opensource.norconex.com";
    private static final String TOPIC = "topic";
    private static final String WORK_DIR = "C:\\Workspace\\Kafka\\nx-crawler-workdir\\temp";
    private static final int MAX_DEPTH = 1;
    private static final int MAX_DOCUMENTS = 5;

    public static void main(String[] args) {
//            runCrawlSession();
            runCrawlingFromFile();
    }

    private static void runCrawlingFromFile() {
        String configFilePath = "C:\\Workspace\\SaaS\\temp\\crawler-v4-test.xml";
        CrawlSessionConfig csc = new CrawlSessionConfig(WebCrawlerConfig.class);
        csc.loadFromXML(new XML(Paths.get(configFilePath)));
        CrawlSession crawlSession2 =  WebCrawlSession.createSession(csc);

        crawlSession2.start();
    }

    private static void runCrawlSession() {
        CrawlSessionConfig myConfig = new CrawlSessionConfig();

        myConfig.setId(CRAWLER_ID);
        myConfig.setWorkDir(Paths.get(WORK_DIR));

        List<CrawlerConfig> crawlerConfigs = new ArrayList<>();

        cc = new WebCrawlerConfig();
        cc.setId(CRAWLER_ID);
        cc.setStartReferences(START_URLS.split(","));
        cc.setMaxDepth(MAX_DEPTH);
        cc.setMaxDocuments(MAX_DOCUMENTS);
//        cc.setIgnoreSitemap(true);

        List<Committer> committers = new ArrayList<>();
        XMLFileCommitter xmlFileCommitter = new XMLFileCommitter();
        xmlFileCommitter.setIndent(4);
        committers.add(xmlFileCommitter);

//        kafkaConnectCommitter = new KafkaConnectCommitter("crawler");
//        committers.add(kafkaConnectCommitter);

        cc.setCommitters(committers);

        var s = new URLCrawlScopeStrategy();
        s.setStayOnDomain(true);
        cc.setUrlCrawlScopeStrategy(s);

        crawlerConfigs.add(cc);
        myConfig.setCrawlerConfigs(crawlerConfigs);

        crawlSession = WebCrawlSession.createSession(myConfig);
        crawlSession.start();

        System.out.println("After start");
    }
}
