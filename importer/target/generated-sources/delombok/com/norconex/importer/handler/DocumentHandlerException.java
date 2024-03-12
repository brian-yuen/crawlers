// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
/* Copyright 2014-2023 Norconex Inc.
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
package com.norconex.importer.handler;

import com.norconex.importer.ImporterException;

/**
 * Exception thrown by several handler classes upon encountering
 * issues.
 */
public class DocumentHandlerException extends ImporterException {
    private static final long serialVersionUID = 6845549545987836093L;

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DocumentHandlerException() {
        this(null, null);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DocumentHandlerException(final java.lang.String message) {
        this(message, null);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DocumentHandlerException(final java.lang.Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public DocumentHandlerException(final java.lang.String message, final java.lang.Throwable cause) {
        super(message);
        if (cause != null) super.initCause(cause);
    }
}
