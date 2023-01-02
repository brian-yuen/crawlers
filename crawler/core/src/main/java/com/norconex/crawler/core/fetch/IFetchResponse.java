/* Copyright 2022-2022 Norconex Inc.
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

import com.norconex.crawler.core.doc.CrawlState;

/**
 *
// * @param <T> response attributes specific to a collection or crawler fetcher
 */
public interface IFetchResponse{ //<T> {

    CrawlState getCrawlState();

    int getStatusCode();

    String getReasonPhrase();
//
//    String getUserAgent();
//
    Exception getException();
//
//    String getRedirectTarget();

//    <T> Optional<T> getAttributes();
//    default Optional<T> getAttributes() {
//        return Optional.empty();
//    }
}