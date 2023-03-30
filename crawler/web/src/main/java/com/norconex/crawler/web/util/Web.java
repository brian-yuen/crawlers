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
package com.norconex.crawler.web.util;

import java.util.Collection;
import java.util.List;

import com.norconex.crawler.core.crawler.Crawler;
import com.norconex.crawler.core.crawler.CrawlerConfig;
import com.norconex.crawler.core.fetch.Fetcher;
import com.norconex.crawler.core.pipeline.AbstractPipelineContext;
import com.norconex.crawler.web.crawler.WebCrawlerConfig;
import com.norconex.crawler.web.fetch.HttpFetcher;
import com.norconex.crawler.web.pipeline.importer.WebImporterPipelineContext;

import lombok.NonNull;

public final class Web {

    private Web() {}

    public static WebCrawlerConfig config(CrawlerConfig cfg) {
        return (WebCrawlerConfig) cfg;
    }
    public static WebCrawlerConfig config(AbstractPipelineContext ctx) {
        return (WebCrawlerConfig) ctx.getConfig();
    }
    public static WebCrawlerConfig config(Crawler crawler) {
        return (WebCrawlerConfig) crawler.getCrawlerConfig();
    }

    public static WebImporterPipelineContext context(
            AbstractPipelineContext ctx) {
        return (WebImporterPipelineContext) ctx;
    }

    public static List<HttpFetcher> toHttpFetcher(
            @NonNull Collection<Fetcher<?, ?>> fetchers) {
        return fetchers.stream()
            .map(HttpFetcher.class::cast)
            .toList();
    }

//    public static HttpFetchResponse fetchResponse(FetchResponse response) {
//        MultiFetchResponse<?> multiResp = (MultiFetchResponse<?>) response;
//        return (HttpFetchResponse) multiResp.getLastFetchResponse().get();
//    }
}