// Generated by delombok at Fri Mar 08 16:24:35 MST 2024
/* Copyright 2022-2023 Norconex Inc.
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

import com.norconex.crawler.core.CoreStubber;
import com.norconex.crawler.core.doc.CrawlDoc;

public class MockFetchRequest implements FetchRequest {
    private String ref;

    @Override
    public CrawlDoc getDoc() {
        return CoreStubber.crawlDoc(ref);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getRef() {
        return this.ref;
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
        if (!(o instanceof MockFetchRequest)) return false;
        final MockFetchRequest other = (MockFetchRequest) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$ref = this.getRef();
        final java.lang.Object other$ref = other.getRef();
        if (this$ref == null ? other$ref != null : !this$ref.equals(other$ref)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof MockFetchRequest;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $ref = this.getRef();
        result = result * PRIME + ($ref == null ? 43 : $ref.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "MockFetchRequest(ref=" + this.getRef() + ")";
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MockFetchRequest() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MockFetchRequest(final String ref) {
        this.ref = ref;
    }
}