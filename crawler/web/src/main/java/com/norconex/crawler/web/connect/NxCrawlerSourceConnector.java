package com.norconex.crawler.web.connect;

import com.norconex.crawler.core.session.CrawlSession;
import com.norconex.crawler.web.crawler.WebCrawlerConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NxCrawlerSourceConnector extends SourceConnector {

    private Map<String, String> props;
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

    static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(START_URLS, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, "Start URLs.")
            .define(CRAWLER_ID, ConfigDef.Type.STRING, "1", new ConfigDef.NonEmptyString(), ConfigDef.Importance.LOW, "Crawler Id (Optional)")
            .define(WORK_DIR, ConfigDef.Type.STRING, "./work", new ConfigDef.NonEmptyString(), ConfigDef.Importance.LOW, "Work Dir")
            .define(MAX_DEPTH, ConfigDef.Type.INT, 0, ConfigDef.Importance.LOW, "Max Depth")
            .define(MAX_DOCUMENTS, ConfigDef.Type.INT, -1, ConfigDef.Importance.MEDIUM, "MAX_DOCUMENTS")
            .define(CONFIG_PATH, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Crawler config file path.");


    @Override
    public void start(Map<String, String> map) {
        this.props = map;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return NxCrawlerSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();

//        List<String> startUrls = List.of(props.get(START_URLS).split(","));

        // Crawls duplicate documents, runs in a distributed mode.
        for (int i = 1; i < maxTasks + 1; i++) {
            Map<String, String> tempProps = new HashMap<>(props);
            tempProps.put("workDir", props.get("workDir") + File.separator + i);
//            tempProps.put("crawlerId", String.valueOf(i));
//            tempProps.put(START_URLS, startUrls.get(i - 1));
            configs.add(tempProps);
        }

        // Should crawl documents once only, runs in a distributed mode.
        // Using JdbcDataStore.
//        for (int i = 1; i < maxTasks + 1; i++) {
//            Map<String, String> tempProps = new HashMap<>(props);
//            tempProps.put("workDir", props.get("workDir"));
//            tempProps.put("crawlerId", String.valueOf(i));
//            configs.add(tempProps);
//        }

        return configs;
    }

    @Override
    public void stop() {
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }
}
