// Generated by delombok at Fri Mar 08 16:24:33 MST 2024
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
package com.norconex.crawler.core.cli;

import java.util.concurrent.Callable;
import com.norconex.commons.lang.bean.BeanMapper;
import com.norconex.crawler.core.session.CrawlSession;
import com.norconex.crawler.core.session.CrawlSessionBuilder;
import com.norconex.crawler.core.session.CrawlSessionConfigMapperFactory;
import lombok.NonNull;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.PicocliException;
import picocli.CommandLine.Spec;

/**
 * Main entry point to the crawler command-line usage.
 */
@Command(name = "<app>", description = "%nOptions:", descriptionHeading = "%n<app> is the executable program used to " + "launch me%n", sortOptions = false, separator = " ", commandListHeading = "%nCommands:%n", footerHeading = "%nExamples:%n", footer = """
    %n  Start a crawl session:%n%n    <app> start -config=/path/to/config.xml%n%n  Stop a crawl session:%n%n    <app> stop -config=/path/to/config.xml%n%n  Get usage help on \"check\" command:%n%n    <app> help configcheck%n
    """, subcommands = {HelpCommand.class, StartCommand.class, StopCommand.class, ConfigCheckCommand.class, ConfigRenderCommand.class, CleanCommand.class, StoreExportCommand.class, StoreImportCommand.class})
public class MainCommand implements Callable<Integer>, IExecutionExceptionHandler {
    @NonNull
    private final CrawlSessionBuilder crawlSessionBuilder;
    @NonNull
    private final BeanMapper beanMapper;
    // CrawlSessionMapperProvider<BeanMapper>  // or part of session builder?
    @Option(names = {"-h", "-help"}, usageHelp = true, description = "Show this help message and exit")
    private boolean help;
    @Option(names = {"-v", "-version"}, description = "Show the crawler version and exit")
    private boolean version;
    @Spec
    private CommandSpec spec;

    public MainCommand(@NonNull CrawlSessionBuilder crawlSessionBuilder) {
        java.util.Objects.requireNonNull(crawlSessionBuilder, "crawlSessionBuilder is marked non-null but is null");
        this.crawlSessionBuilder = crawlSessionBuilder;
//        beanMapper = crawlSessionBuilder.mapperBuilderFactory().apply(
//                crawlSessionBuilder.crawlerConfigClass()).build();
        beanMapper = CrawlSessionConfigMapperFactory.create(crawlSessionBuilder.crawlerConfigClass());
    }

    CrawlSessionBuilder getCrawlSessionBuilder() {
        return crawlSessionBuilder;
    }

    BeanMapper getBeanMapper() {
        return beanMapper;
    }

    @Override
    public Integer call() throws Exception {
        if (version) {
            spec.commandLine().getOut().println(CrawlSession.getReleaseInfo(crawlSessionBuilder.crawlSessionConfig()));
        }
        return 0;
    }

    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult) throws Exception {
        if (ex instanceof PicocliException) {
            commandLine.getErr().println(ex.getMessage());
            commandLine.getErr().println();
            commandLine.usage(commandLine.getErr());
            return -1;
        }
        throw ex;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof MainCommand)) return false;
        final MainCommand other = (MainCommand) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.help != other.help) return false;
        if (this.version != other.version) return false;
        final java.lang.Object this$crawlSessionBuilder = this.getCrawlSessionBuilder();
        final java.lang.Object other$crawlSessionBuilder = other.getCrawlSessionBuilder();
        if (this$crawlSessionBuilder == null ? other$crawlSessionBuilder != null : !this$crawlSessionBuilder.equals(other$crawlSessionBuilder)) return false;
        final java.lang.Object this$beanMapper = this.getBeanMapper();
        final java.lang.Object other$beanMapper = other.getBeanMapper();
        if (this$beanMapper == null ? other$beanMapper != null : !this$beanMapper.equals(other$beanMapper)) return false;
        final java.lang.Object this$spec = this.spec;
        final java.lang.Object other$spec = other.spec;
        if (this$spec == null ? other$spec != null : !this$spec.equals(other$spec)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof MainCommand;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.help ? 79 : 97);
        result = result * PRIME + (this.version ? 79 : 97);
        final java.lang.Object $crawlSessionBuilder = this.getCrawlSessionBuilder();
        result = result * PRIME + ($crawlSessionBuilder == null ? 43 : $crawlSessionBuilder.hashCode());
        final java.lang.Object $beanMapper = this.getBeanMapper();
        result = result * PRIME + ($beanMapper == null ? 43 : $beanMapper.hashCode());
        final java.lang.Object $spec = this.spec;
        result = result * PRIME + ($spec == null ? 43 : $spec.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "MainCommand(crawlSessionBuilder=" + this.getCrawlSessionBuilder() + ", beanMapper=" + this.getBeanMapper() + ", help=" + this.help + ", version=" + this.version + ", spec=" + this.spec + ")";
    }
}
