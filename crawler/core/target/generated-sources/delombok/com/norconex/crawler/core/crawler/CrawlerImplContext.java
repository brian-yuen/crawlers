// Generated by delombok at Fri Mar 08 16:24:33 MST 2024
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Base class for of a crawler implementation context that
 * is useful for storing random objects in memory.
 * Crawler implementations are encouraged to provide their own subclass
 * with concrete methods instead of using this class directly.
 * Extending this base class mainly makes it easier for developers to store
 * ad-hoc data in their own extensions of an existing crawler implementation.
 */
public class CrawlerImplContext {
    private final Map<String, Object> attributes = new HashMap<>();

    public Optional<Object> getAttribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    public Optional<Object> setAttribute(String key, Object obj) {
        return Optional.ofNullable(attributes.put(key, obj));
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof CrawlerImplContext)) return false;
        final CrawlerImplContext other = (CrawlerImplContext) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$attributes = this.attributes;
        final java.lang.Object other$attributes = other.attributes;
        if (this$attributes == null ? other$attributes != null : !this$attributes.equals(other$attributes)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof CrawlerImplContext;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $attributes = this.attributes;
        result = result * PRIME + ($attributes == null ? 43 : $attributes.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "CrawlerImplContext(attributes=" + this.attributes + ")";
    }
}
