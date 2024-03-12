/* Copyright 2010-2023 Norconex Inc.
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
package com.norconex.crawler.web.pipeline.importer;

import java.io.IOException;

import com.norconex.crawler.core.crawler.CrawlerEvent;
import com.norconex.crawler.core.crawler.CrawlerException;
import com.norconex.crawler.core.pipeline.importer.AbstractImporterStage;
import com.norconex.crawler.core.pipeline.importer.ImporterPipelineContext;
import com.norconex.crawler.web.crawler.WebCrawlerEvent;

/**
 * Robots meata creation.
 */
class RobotsMetaCreateStage extends AbstractImporterStage {

    @Override
    protected boolean executeStage(ImporterPipelineContext context) { //NOSONAR
        var ctx = (WebImporterPipelineContext) context;
        if (ctx.getConfig().getRobotsMetaProvider() == null) {
            return true;
        }

        try {
            var reader = ctx.getContentReader();
            ctx.setRobotsMeta(
                    ctx.getConfig().getRobotsMetaProvider().getRobotsMeta(
                            reader, ctx.getDocRecord().getReference(),
                            ctx.getDocument().getDocRecord().getContentType(),
                            ctx.getDocument().getMetadata()));
            reader.close();
            if (ctx.getRobotsMeta() != null) {
                ctx.fire(CrawlerEvent.builder()
                        .name(WebCrawlerEvent.EXTRACTED_ROBOTS_META)
                        .source(ctx.getCrawler())
                        .subject(ctx.getRobotsMeta())
                        .crawlDocRecord(ctx.getDocRecord())
                        .build());
            }
        } catch (IOException e) {
            throw new CrawlerException("Cannot create RobotsMeta for : "
                            + ctx.getDocRecord().getReference(), e);
        }
        return true;
    }
}