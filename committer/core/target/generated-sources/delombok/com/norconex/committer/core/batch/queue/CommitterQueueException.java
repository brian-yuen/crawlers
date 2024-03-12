// Generated by delombok at Fri Mar 08 16:23:50 MST 2024
/* Copyright 2020-2022 Norconex Inc.
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
package com.norconex.committer.core.batch.queue;

import com.norconex.committer.core.CommitterException;

/**
 * Triggered when something went wrong with the committer queue.
 */
public class CommitterQueueException extends CommitterException {
    private static final long serialVersionUID = 1L;

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CommitterQueueException() {
        this(null, null);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CommitterQueueException(final java.lang.String message) {
        this(message, null);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CommitterQueueException(final java.lang.Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public CommitterQueueException(final java.lang.String message, final java.lang.Throwable cause) {
        super(message);
        if (cause != null) super.initCause(cause);
    }
}
