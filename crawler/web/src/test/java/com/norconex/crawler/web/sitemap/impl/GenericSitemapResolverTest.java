/* Copyright 2019-2023 Norconex Inc.
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
package com.norconex.crawler.web.sitemap.impl;

import static com.norconex.crawler.web.WebsiteMock.serverUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.MediaType;

import com.norconex.commons.lang.xml.XML;
import com.norconex.crawler.core.crawler.Crawler;
import com.norconex.crawler.web.MockWebCrawlSession;
import com.norconex.crawler.web.crawler.HttpCrawlerConfig;
import com.norconex.crawler.web.doc.HttpDocRecord;
import com.norconex.crawler.web.fetch.HttpFetcher;
import com.norconex.crawler.web.sitemap.SitemapResolutionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@MockServerSettings
class GenericSitemapResolverTest {

    @Test
    @MockWebCrawlSession
    void testResolveSitemaps(ClientAndServer client, Crawler crawler)
            throws IOException {

        // We test having a sitemap index file pointing to sitemap files, and
        // We test compression.
        // We test redirect

        client
            .when(request()
                    .withPath("/sitemap-index"))
            .respond(response()
                .withBody("""
                    <?xml version="1.0" encoding="UTF-8"?>
                    <sitemapindex \
                        xmlns="https://www.sitemaps.org/schemas/sitemap/0.9">
                      <sitemap>
                        <loc>%s</loc>
                        <lastmod>2000-10-01T18:23:17+00:00</lastmod>
                      </sitemap>
                    </sitemapindex>
                    """.formatted(serverUrl(client, "sitemap")),
                    MediaType.XML_UTF_8));

        client
            .when(request()
                .withPath("/sitemap"))
            .respond(response()
                .withStatusCode(302)
                .withHeader("Location",
                        serverUrl(client, "/sitemap-new")));

        client
            .when(request()
                .withPath("/sitemap-new"))
            .respond(response()
                    .withHeader("Content-Encoding", "gzip")
                    .withHeader("Content-type", "text/xml; charset=utf-8")
                    .withBody(compressSitemap(serverUrl(client, ""))));

        List<String> urls = new ArrayList<>();


        var resolver = ((HttpCrawlerConfig)
                crawler.getCrawlerConfig()).getSitemapResolver();
        resolver.resolveSitemaps(SitemapResolutionContext
                .builder()
                .fetcher((HttpFetcher) crawler.getFetcher())
                .sitemapLocations(List.of(serverUrl(client, "sitemap-index")))
                .startURLs(false)
                .urlRoot(serverUrl(client, ""))
                .urlConsumer(rec -> urls.add(rec.getReference()))
                .build());

        assertThat(urls).containsExactly(
                serverUrl(client, "/pageA.html"),
                serverUrl(client, "/pageB.html"));
    }

    private byte[] compressSitemap(String baseUrl) throws IOException {
        var content = """
                <urlset>
                  <url>
                    <loc>%s</loc>
                    <lastmod>2021-02-26</lastmod>
                    <changefreq>daily</changefreq>
                    <priority>0.5</priority>
                  </url>
                  <url>
                    <loc>%s</loc>
                    <lastmod>2021-04-01</lastmod>
                    <changefreq>daily</changefreq>
                    <priority>1.0</priority>
                  </url>
                </urlset>
                """.formatted(
                        baseUrl + "pageA.html",
                        baseUrl + "pageB.html")
                .getBytes();
        var bos = new ByteArrayOutputStream(content.length);
        var gzip = new GZIPOutputStream(bos);
        gzip.write(content);
        gzip.close();
        var compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    @Test
    void testSitemapResolverParsing()
            throws IOException, XMLStreamException {

        List<HttpDocRecord> extractedLinks = new ArrayList<>();
        var r = new GenericSitemapResolver();
        try (var is = getClass().getResourceAsStream("sitemap.xml")) {
            r.parseSitemap(is, null, d -> {
                extractedLinks.add(d);
            }, new HashSet<>(), "https://example.com/sitemap.xml");
        }

        // All links there?
        Assertions.assertEquals(
                Arrays.asList(
                        "https://example.com/linkA",
                        "https://example.com/linkB",
                        "https://example.com/linkC",
                        "https://example.com/linkD"),
                extractedLinks.stream()
                        .map(HttpDocRecord::getReference)
                        .collect(Collectors.toList()));

        // test second one:
        var doc = extractedLinks.get(1);
        Assertions.assertEquals(
                "https://example.com/linkB", doc.getReference());
        Assertions.assertEquals("2021-04-01",
                doc.getSitemapLastMod().toLocalDate().toString());
        Assertions.assertEquals("daily", doc.getSitemapChangeFreq());
        Assertions.assertEquals(1f, doc.getSitemapPriority());
    }

    @Test
    void testWriteRead() {
        var r = new GenericSitemapResolver();
        r.setLenient(true);
        r.setSitemapPaths(List.of("/sitemap.xml", "/subdir/sitemap.xml"));
        LOG.debug("Writing/Reading this: {}", r);
        XML.assertWriteRead(r, "sitemapResolver");

        // try with empty paths
        r.setSitemapPaths(null);
        LOG.debug("Writing/Reading this: {}", r);
        XML.assertWriteRead(r, "sitemapResolver");
    }
}
