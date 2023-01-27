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
package com.norconex.crawler.core.fetch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.norconex.crawler.core.doc.CrawlDocState;

import lombok.Data;

class GenericMultiFetchResponseTest {

    @Data
    static class TestResponse implements FetchResponse {
        private final String salt;
        @Override
        public int getStatusCode() {
            return 123;
        }
        @Override
        public String getReasonPhrase() {
            return "Just because.";
        }
        @Override
        public Exception getException() {
            return new IllegalArgumentException("TEST");
        }
        @Override
        public CrawlDocState getCrawlState() {
            return CrawlDocState.MODIFIED;
        }
    }

    @Data
    static class TestFetcher
            implements Fetcher<MockFetchRequest, TestResponse> {
        private final String salt;
        @Override
        public boolean accept(MockFetchRequest fetchRequest) {
            return true;
        }
        @Override
        public TestResponse fetch(MockFetchRequest fetchRequest)
                throws FetchException {
            return null;
        }
    }

    @Test
    void testGenericMultiFetchResponse() {
        var gmfr = new GenericMultiFetchResponse<TestResponse>();

        var resp1 = new TestResponse("resp1");
        gmfr.addFetchResponse(resp1, new TestFetcher("fetcher1"));
        var resp2 = new TestResponse("resp2");
        gmfr.addFetchResponse(resp2, new TestFetcher("fetcher2"));

        assertThat(gmfr.getStatusCode()).isEqualTo(123);
        assertThat(gmfr.getReasonPhrase()).isEqualTo("Just because.");
        assertThat(gmfr.getException().getMessage()).isEqualTo("TEST");
        assertThat(gmfr.getCrawlState()).isSameAs(CrawlDocState.MODIFIED);
        assertThat(gmfr.getFetchResponses()).containsExactlyInAnyOrder(
                resp1, resp2);
        assertThat(gmfr.getLastFetchResponse()).containsSame(resp2);
        assertThat(gmfr).hasToString("123 Just because. - TestFetcher");
    }
}
