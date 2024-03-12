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
package com.norconex.crawler.web.crawler.impl;

import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import org.apache.commons.lang3.mutable.MutableInt;

import com.norconex.commons.lang.config.ConfigurationException;
import com.norconex.crawler.core.crawler.CrawlerImpl.QueueInitContext;
import com.norconex.crawler.web.doc.WebDocRecord;
import com.norconex.crawler.web.sitemap.SitemapContext;
import com.norconex.crawler.web.util.Web;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SitemapQueueInitializer implements ToIntFunction<QueueInitContext>{
    @Override
    public int applyAsInt(QueueInitContext queueInitCtx) {

        var cfg = Web.config(queueInitCtx.getCrawler());
        var sitemapURLs = cfg.getStartReferencesSitemaps();
        var sitemapResolver = cfg.getSitemapResolver();

        if (!sitemapURLs.isEmpty() && sitemapResolver == null) {
            throw new ConfigurationException("""
                One or more sitemap URLs were\s\
                configured as start references but the sitemap resolver\s\
                was set to null.
                """);
        }

        final var urlCount = new MutableInt();
        Consumer<WebDocRecord> urlConsumer = rec -> {
            queueInitCtx.queue(rec);
            urlCount.increment();
        };

        // Process each sitemap URL
        for (String url : sitemapURLs) {
            sitemapResolver.resolve(SitemapContext.builder()
                .fetcher(Web.fetcher(queueInitCtx.getCrawler()))
                .location(url)
                .urlConsumer(urlConsumer)
                .build());
        }
        if (urlCount.intValue() > 0) {
            LOG.info("Queued {} start references from {} sitemap(s).",
                    urlCount, sitemapURLs.size());
        }
        return urlCount.intValue();
    }
}
