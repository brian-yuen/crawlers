/* Copyright 2019-2022 Norconex Inc.
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
package com.norconex.crawler.core;

import com.norconex.crawler.core.session.CrawlSession;

//TODO maybe move to main/java to provide test classes for collector impls?
class MockCrawlSession extends CrawlSession {

    protected MockCrawlSession(CrawlSessionBuilder builder) {
        super(builder);
        // TODO Auto-generated constructor stub
    }

//    MockCrawlSession(String id, Path workdir) {
//        super()
//        super(new MockCollectorConfig());
//        getCollectorConfig().setId(id);
//        getCollectorConfig().setWorkDir(workdir);
//    }
//    MockCrawlSession(CollectorConfig collectorConfig) {
//        super(Objects.requireNonNull(collectorConfig));
//    }
//
//    @Override
//    protected Crawler createCrawler(CrawlerConfig crawlerConfig) {
//        return null;//new MockCrawler(crawlerConfig, this);
//    }
//
//    CrawlerConfig getFirstCrawlerConfig() {
//        return getCollectorConfig().getCrawlerConfigs().get(0);
//    }
}
