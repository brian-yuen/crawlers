// Generated by delombok at Fri Mar 08 16:23:53 MST 2024
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
package com.norconex.committer.core.service;

import static java.io.InputStream.nullInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import com.norconex.committer.core.CommitterContext;
import com.norconex.committer.core.CommitterEvent;
import com.norconex.committer.core.CommitterException;
import com.norconex.committer.core.DeleteRequest;
import com.norconex.committer.core.StringListConverter;
import com.norconex.committer.core.UpsertRequest;
import com.norconex.committer.core.fs.AbstractFSCommitter;
import com.norconex.committer.core.fs.impl.JSONFileCommitter;
import com.norconex.committer.core.fs.impl.XMLFileCommitter;
import com.norconex.committer.core.impl.MemoryCommitter;
import com.norconex.commons.lang.map.Properties;
import com.norconex.commons.lang.map.PropertyMatcher;
import com.norconex.commons.lang.text.TextMatcher;

class CommitterServiceTest {
    private final Map<String, TestContext> testContexes = new HashMap<>();


    private static class TestContext {
        private final List<MemoryCommitter> committers;
        private final CommitterService<TestDoc> service;
        private final List<String> events = new ArrayList<>();
        private CommitterContext committerContext;

        public TestContext(CommitterService<TestDoc> service) {
            this.service = service;
            committers = service.getCommitters().stream().map(MemoryCommitter.class::cast).toList();
            service.getEventManager().addListener(e -> events.add(e.getName()));
            committerContext = CommitterContext.builder().setEventManager(service.getEventManager()).build();
        }

        void initService() {
            service.init(committerContext);
            events.clear();
        }

        int upsertCount() {
            return committers.stream().mapToInt(MemoryCommitter::getUpsertCount).sum();
        }

        int deleteCount() {
            return committers.stream().mapToInt(MemoryCommitter::getDeleteCount).sum();
        }

        int closeCount() {
            return (int) committers.stream().filter(MemoryCommitter::isClosed).count();
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public List<MemoryCommitter> getCommitters() {
            return this.committers;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public CommitterService<TestDoc> getService() {
            return this.service;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public List<String> getEvents() {
            return this.events;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public CommitterContext getCommitterContext() {
            return this.committerContext;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public void setCommitterContext(final CommitterContext committerContext) {
            this.committerContext = committerContext;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof CommitterServiceTest.TestContext)) return false;
            final CommitterServiceTest.TestContext other = (CommitterServiceTest.TestContext) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$committers = this.getCommitters();
            final java.lang.Object other$committers = other.getCommitters();
            if (this$committers == null ? other$committers != null : !this$committers.equals(other$committers)) return false;
            final java.lang.Object this$service = this.getService();
            final java.lang.Object other$service = other.getService();
            if (this$service == null ? other$service != null : !this$service.equals(other$service)) return false;
            final java.lang.Object this$events = this.getEvents();
            final java.lang.Object other$events = other.getEvents();
            if (this$events == null ? other$events != null : !this$events.equals(other$events)) return false;
            final java.lang.Object this$committerContext = this.getCommitterContext();
            final java.lang.Object other$committerContext = other.getCommitterContext();
            if (this$committerContext == null ? other$committerContext != null : !this$committerContext.equals(other$committerContext)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof CommitterServiceTest.TestContext;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $committers = this.getCommitters();
            result = result * PRIME + ($committers == null ? 43 : $committers.hashCode());
            final java.lang.Object $service = this.getService();
            result = result * PRIME + ($service == null ? 43 : $service.hashCode());
            final java.lang.Object $events = this.getEvents();
            result = result * PRIME + ($events == null ? 43 : $events.hashCode());
            final java.lang.Object $committerContext = this.getCommitterContext();
            result = result * PRIME + ($committerContext == null ? 43 : $committerContext.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "CommitterServiceTest.TestContext(committers=" + this.getCommitters() + ", service=" + this.getService() + ", events=" + this.getEvents() + ", committerContext=" + this.getCommitterContext() + ")";
        }
    }


    static class TestDoc {
        private String ref;
        private final Properties meta = new Properties();

        public TestDoc(String ref) {
            this.ref = ref;
            meta.set("document.reference", ref);
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public String getRef() {
            return this.ref;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Properties getMeta() {
            return this.meta;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public void setRef(final String ref) {
            this.ref = ref;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof CommitterServiceTest.TestDoc)) return false;
            final CommitterServiceTest.TestDoc other = (CommitterServiceTest.TestDoc) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$ref = this.getRef();
            final java.lang.Object other$ref = other.getRef();
            if (this$ref == null ? other$ref != null : !this$ref.equals(other$ref)) return false;
            final java.lang.Object this$meta = this.getMeta();
            final java.lang.Object other$meta = other.getMeta();
            if (this$meta == null ? other$meta != null : !this$meta.equals(other$meta)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof CommitterServiceTest.TestDoc;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $ref = this.getRef();
            result = result * PRIME + ($ref == null ? 43 : $ref.hashCode());
            final java.lang.Object $meta = this.getMeta();
            result = result * PRIME + ($meta == null ? 43 : $meta.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "CommitterServiceTest.TestDoc(ref=" + this.getRef() + ", meta=" + this.getMeta() + ")";
        }
    }

    @BeforeEach
    void beforeEach() {
        // Case 1:
        var acceptOnlyAAA = new MemoryCommitter();
        acceptOnlyAAA.getConfiguration().addRestriction(new PropertyMatcher(TextMatcher.basic("document.reference"), TextMatcher.basic("aaa")));
        testContexes.put("2committers", new TestContext(CommitterService.<TestDoc>builder().committers(List.of(new MemoryCommitter(), acceptOnlyAAA)).upsertRequestBuilder(doc -> new UpsertRequest(doc.getRef(), doc.getMeta(), null)).deleteRequestBuilder(doc -> new DeleteRequest(doc.ref, doc.meta)).build()));
        testContexes.put("0committers", new TestContext(CommitterService.<TestDoc>builder().committers(List.of()).upsertRequestBuilder(doc -> new UpsertRequest(doc.getRef(), doc.getMeta(), null)).deleteRequestBuilder(doc -> new DeleteRequest(doc.ref, doc.meta)).build()));
    }

    @ParameterizedTest
    @CsvSource({"2committers, true", "0committers, false"})
    void testIsOperative(String testCtx, boolean expected) {
        var test = testContexes.get(testCtx);
        assertThat(test.service.isOperative()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"2committers, " + CommitterServiceEvent.COMMITTER_SERVICE_INIT_BEGIN + "|" + CommitterEvent.COMMITTER_INIT_BEGIN + "|" + CommitterEvent.COMMITTER_INIT_END + "|" + CommitterEvent.COMMITTER_INIT_BEGIN + "|" + CommitterEvent.COMMITTER_INIT_END + "|" + CommitterServiceEvent.COMMITTER_SERVICE_INIT_END, "0committers, " + CommitterServiceEvent.COMMITTER_SERVICE_INIT_BEGIN + "|" + CommitterServiceEvent.COMMITTER_SERVICE_INIT_END})
    void testInit(String testCtx, @ConvertWith(StringListConverter.class) String[] expectedEvents) {
        var test = testContexes.get(testCtx);
        test.service.init(test.committerContext);
        assertThat(test.events).containsExactly(expectedEvents);
    }

    // testContext, reference, expectedUpsertCount, expectedEvents
    @ParameterizedTest
    @CsvSource({"2committers, aaa, 2, " + CommitterServiceEvent.COMMITTER_SERVICE_UPSERT_BEGIN + "|" + CommitterEvent.COMMITTER_ACCEPT_YES + "|" + CommitterEvent.COMMITTER_UPSERT_BEGIN + "|" + CommitterEvent.COMMITTER_UPSERT_END + "|" + CommitterEvent.COMMITTER_ACCEPT_YES + "|" + CommitterEvent.COMMITTER_UPSERT_BEGIN + "|" + CommitterEvent.COMMITTER_UPSERT_END + "|" + CommitterServiceEvent.COMMITTER_SERVICE_UPSERT_END, "0committers, aaa, 0, " + CommitterServiceEvent.COMMITTER_SERVICE_UPSERT_BEGIN + "|" + CommitterServiceEvent.COMMITTER_SERVICE_UPSERT_END, "2committers, bbb, 1, " + CommitterServiceEvent.COMMITTER_SERVICE_UPSERT_BEGIN + "|" + CommitterEvent.COMMITTER_ACCEPT_YES + "|" + CommitterEvent.COMMITTER_UPSERT_BEGIN + "|" + CommitterEvent.COMMITTER_UPSERT_END + "|" + CommitterEvent.COMMITTER_ACCEPT_NO + "|" + CommitterServiceEvent.COMMITTER_SERVICE_UPSERT_END})
    void testUpsert(String testCtx, String reference, int expectedUpsertCount, @ConvertWith(StringListConverter.class) String[] expectedEvents) throws CommitterException {
        var test = testContexes.get(testCtx);
        test.initService();
        test.service.upsert(new TestDoc(reference));
        if (expectedEvents != null) {
            assertThat(test.events).containsExactly(expectedEvents);
        }
        assertThat(test.upsertCount()).isEqualTo(expectedUpsertCount);
    }

    // testContext, reference, expectedDeleteCount, expectedEvents
    @ParameterizedTest
    @CsvSource({"2committers, aaa, 2, " + CommitterServiceEvent.COMMITTER_SERVICE_DELETE_BEGIN + "|" + CommitterEvent.COMMITTER_ACCEPT_YES + "|" + CommitterEvent.COMMITTER_DELETE_BEGIN + "|" + CommitterEvent.COMMITTER_DELETE_END + "|" + CommitterEvent.COMMITTER_ACCEPT_YES + "|" + CommitterEvent.COMMITTER_DELETE_BEGIN + "|" + CommitterEvent.COMMITTER_DELETE_END + "|" + CommitterServiceEvent.COMMITTER_SERVICE_DELETE_END, "0committers, aaa, 0, " + CommitterServiceEvent.COMMITTER_SERVICE_DELETE_BEGIN + "|" + CommitterServiceEvent.COMMITTER_SERVICE_DELETE_END, "2committers, bbb, 1, " + CommitterServiceEvent.COMMITTER_SERVICE_DELETE_BEGIN + "|" + CommitterEvent.COMMITTER_ACCEPT_YES + "|" + CommitterEvent.COMMITTER_DELETE_BEGIN + "|" + CommitterEvent.COMMITTER_DELETE_END + "|" + CommitterEvent.COMMITTER_ACCEPT_NO + "|" + CommitterServiceEvent.COMMITTER_SERVICE_DELETE_END})
    void testDelete(String testCtx, String reference, int expectedDeleteCount, @ConvertWith(StringListConverter.class) String[] expectedEvents) throws CommitterException {
        var test = testContexes.get(testCtx);
        test.initService();
        test.service.delete(new TestDoc(reference));
        if (expectedEvents != null) {
            assertThat(test.events).containsExactly(expectedEvents);
        }
        assertThat(test.deleteCount()).isEqualTo(expectedDeleteCount);
    }

    @ParameterizedTest
    @CsvSource({"2committers, 2, " + CommitterServiceEvent.COMMITTER_SERVICE_CLOSE_BEGIN + "|" + CommitterEvent.COMMITTER_CLOSE_BEGIN + "|" + CommitterEvent.COMMITTER_CLOSE_END + "|" + CommitterEvent.COMMITTER_CLOSE_BEGIN + "|" + CommitterEvent.COMMITTER_CLOSE_END + "|" + CommitterServiceEvent.COMMITTER_SERVICE_CLOSE_END, "0committers, 0, " + CommitterServiceEvent.COMMITTER_SERVICE_CLOSE_BEGIN + "|" + CommitterServiceEvent.COMMITTER_SERVICE_CLOSE_END})
    void testClose(String testCtx, int expectedCloseCount, @ConvertWith(StringListConverter.class) String[] expectedEvents) {
        var test = testContexes.get(testCtx);
        test.initService();
        test.service.close();
        assertThat(test.closeCount()).isEqualTo(expectedCloseCount);
        assertThat(test.events).containsExactly(expectedEvents);
    }

    @ParameterizedTest
    @CsvSource({"2committers, " + CommitterServiceEvent.COMMITTER_SERVICE_CLEAN_BEGIN + "|" + CommitterEvent.COMMITTER_CLEAN_BEGIN + "|" + CommitterEvent.COMMITTER_CLEAN_END + "|" + CommitterEvent.COMMITTER_CLEAN_BEGIN + "|" + CommitterEvent.COMMITTER_CLEAN_END + "|" + CommitterServiceEvent.COMMITTER_SERVICE_CLEAN_END, "0committers, " + CommitterServiceEvent.COMMITTER_SERVICE_CLEAN_BEGIN + "|" + CommitterServiceEvent.COMMITTER_SERVICE_CLEAN_END})
    void testClean(String testCtx, @ConvertWith(StringListConverter.class) String[] expectedEvents) {
        var test = testContexes.get(testCtx);
        test.initService();
        test.service.clean();
        assertThat(test.events).containsExactly(expectedEvents);
    }

    @ParameterizedTest
    @CsvSource({"2committers, 2", "0committers, 0"})
    void testGetCommitters(String testCtx, int expectedCommitterCount) {
        var test = testContexes.get(testCtx);
        assertThat(test.getService().getCommitters()).hasSize(expectedCommitterCount);
    }

    @ParameterizedTest
    @CsvSource({"2committers, \'CommitterService[MemoryCommitter,MemoryCommitter]\'", "0committers, CommitterService[]"})
    void testToString(String testCtx, String expectedToString) {
        var test = testContexes.get(testCtx);
        assertThat(test.getService()).hasToString(expectedToString);
    }

    @Test
    void testCommitterDefaultDirs(@TempDir Path tempDir) {
        // Directory names bearing the committer class names should be created.
        // In case of using the same Committer more than once, a counter is
        // added.  It should still be possible to overwrite and give
        // a specific path.
        var overwrittenPath = new XMLFileCommitter();
        overwrittenPath.getConfiguration().setDirectory(tempDir.resolve("customOne"));
        var service = CommitterService.builder().committers(List.of(new XMLFileCommitter(), new JSONFileCommitter(), overwrittenPath, new XMLFileCommitter())).upsertRequestBuilder(obj -> new UpsertRequest("mock", new Properties(), nullInputStream())).deleteRequestBuilder(obj -> new DeleteRequest("mock", new Properties())).build();
        service.init(CommitterContext.builder().setWorkDir(tempDir).build());
        var actualDirNames = service.getCommitters().stream().map(AbstractFSCommitter.class::cast).map(c -> c.getResolvedDirectory().getFileName().toString()).toList();
        assertThat(actualDirNames).containsExactly("XMLFileCommitter", "JSONFileCommitter", "customOne", 
        // we expect this one to be _3, not _2, since it is the 3rd XML
        // committer, after the "customOne".
        "XMLFileCommitter_3");
    }
}