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
package com.norconex.crawler.core.crawler;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.norconex.crawler.core.CoreStubber;
import com.norconex.crawler.core.crawler.CrawlerThread.ThreadActionContext;
import com.norconex.importer.response.ImporterResponse;

class ThreadActionUpsertTest {

    @TempDir
    private Path tempDir;

    @Test
    void testThreadActionUpsert() {

        var crawler = CoreStubber.crawler(tempDir);
        crawler.getConfiguration().getImporterConfig().setResponseProcessors(
            List.of(resp -> resp.setNestedResponses(List.of(
                    new ImporterResponse()
                        .setDoc(CoreStubber.crawlDoc("childResponse1")),
                    new ImporterResponse()
                        .setDoc(CoreStubber.crawlDoc("childResponse2"))
            ))));
        // start just so we have the crawler setup properly to run our
        // tests
        crawler.start();


        var doc = CoreStubber.crawlDoc("ref");
        var ctx = new ThreadActionContext();
        ctx.finalized(false);
        ctx.crawler(crawler);
        ctx.doc(doc);
        ctx.docRecord(doc.getDocRecord());

        //TODO call within crawl session to prevent exception?
        ThreadActionUpsert.execute(ctx);

        assertThat(ctx.importerResponse()).isNotNull();
        assertThat(ctx.importerResponse().getNestedResponses()).hasSize(2);
    }
}
