// Generated by delombok at Fri Mar 08 16:24:33 MST 2024
/* Copyright 2019-2023 Norconex Inc.
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
package com.norconex.crawler.core.store.impl.mvstore;

/**
 * <p>
 * MVStore configuration parameters.  For advanced use only.
 * Differences from MVStore defaults:
 * All data size values are expected to be set in bytes.
 * Light compression is enabled by default (compress = 1)
 * </p>
 * <p>
 * For more info:
 * </p>
 * <ul>
 *   <li><a href="http://www.h2database.com/html/mvstore.html">
 *       MVStore documentation</a></li>
 *   <li><a href="https://javadoc.io/doc/com.h2database/h2-mvstore/latest/">
 *       Javadoc</a></li>
 * </ul>
 * @since 1.10.0
 * @author Pascal Essiembre
 */
public class MVStoreDataStoreEngineConfig {
    /**
     * The max memory page size in bytes before splitting it.
     * Defaults to 4KB for memory, and  16KB for disk.
     */
    @SuppressWarnings("javadoc")
    private Long pageSplitSize;
    /**
     * <p>The level of compression when storing data. Supported values:</p>
     * <ul>
     *   <li>0: No compression</li>
     *   <li>1: Low compression (default)</li>
     *   <li>2: High compression</li>
     * </ul>
     */
    @SuppressWarnings("javadoc")
    private Integer compress = 1;
    /**
     * The maximum number of concurrent operations when reading from
     * the store cache. Default is 16.
     */
    @SuppressWarnings("javadoc")
    private Integer cacheConcurrency = 16;
    /**
     * The read cache size in bytes. Default is 16MB.
     */
    @SuppressWarnings("javadoc")
    private Long cacheSize;
    /**
     * The auto-compact target fill rate, in percentage. Default is 90%.
     */
    @SuppressWarnings("javadoc")
    private Integer autoCompactFillRate;
    /**
     * The size of the write buffer. Defaults to 1024KB.
     */
    @SuppressWarnings("javadoc")
    private Long autoCommitBufferSize;
    /**
     * The maximum delay in milliseconds to auto-commit changes. Defaults
     * to 1000ms (1 second).
     */
    @SuppressWarnings("javadoc")
    private Long autoCommitDelay;
    /**
     * Stores data in memory and does not persist any information
     * between each crawling sessions (breaking from the crawler
     * normal behavior).
     * <b>Not recommended for regular use.</b>
     * Useful for testing and troubleshooting, or if you know what your doing.
     */
    @SuppressWarnings("javadoc")
    private boolean ephemeral;

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig() {
    }

    /**
     * The max memory page size in bytes before splitting it.
     * Defaults to 4KB for memory, and  16KB for disk.
     * @return page size
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Long getPageSplitSize() {
        return this.pageSplitSize;
    }

    /**
     * <p>The level of compression when storing data. Supported values:</p>
     * <ul>
     *   <li>0: No compression</li>
     *   <li>1: Low compression (default)</li>
     *   <li>2: High compression</li>
     * </ul>
     * @return level of compression
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Integer getCompress() {
        return this.compress;
    }

    /**
     * The maximum number of concurrent operations when reading from
     * the store cache. Default is 16.
     * @return maximum number of concurrent operations
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Integer getCacheConcurrency() {
        return this.cacheConcurrency;
    }

    /**
     * The read cache size in bytes. Default is 16MB.
     * @return read cache size
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Long getCacheSize() {
        return this.cacheSize;
    }

    /**
     * The auto-compact target fill rate, in percentage. Default is 90%.
     * @return auto compact fill rate
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Integer getAutoCompactFillRate() {
        return this.autoCompactFillRate;
    }

    /**
     * The size of the write buffer. Defaults to 1024KB.
     * @return size of the write buffer
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Long getAutoCommitBufferSize() {
        return this.autoCommitBufferSize;
    }

    /**
     * The maximum delay in milliseconds to auto-commit changes. Defaults
     * to 1000ms (1 second).
     * @return maximum delay to auto-commit changes
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Long getAutoCommitDelay() {
        return this.autoCommitDelay;
    }

    /**
     * Stores data in memory and does not persist any information
     * between each crawling sessions (breaking from the crawler
     * normal behavior).
     * <b>Not recommended for regular use.</b>
     * Useful for testing and troubleshooting, or if you know what your doing.
     * @return <code>true</code> if only using memory (data is not persisted)
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isEphemeral() {
        return this.ephemeral;
    }

    /**
     * The max memory page size in bytes before splitting it.
     * Defaults to 4KB for memory, and  16KB for disk.
     * @param pageSplitSize split size
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setPageSplitSize(final Long pageSplitSize) {
        this.pageSplitSize = pageSplitSize;
        return this;
    }

    /**
     * <p>The level of compression when storing data. Supported values:</p>
     * <ul>
     *   <li>0: No compression</li>
     *   <li>1: Low compression (default)</li>
     *   <li>2: High compression</li>
     * </ul>
     * @param compress level of compression
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setCompress(final Integer compress) {
        this.compress = compress;
        return this;
    }

    /**
     * The maximum number of concurrent operations when reading from
     * the store cache. Default is 16.
     * @param cacheConcurrency maximum number of concurrent operations
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setCacheConcurrency(final Integer cacheConcurrency) {
        this.cacheConcurrency = cacheConcurrency;
        return this;
    }

    /**
     * The read cache size in bytes. Default is 16MB.
     * @param cacheSize read cache size
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setCacheSize(final Long cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    /**
     * The auto-compact target fill rate, in percentage. Default is 90%.
     * @param autoCompactFillRate auto compact fill rate
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setAutoCompactFillRate(final Integer autoCompactFillRate) {
        this.autoCompactFillRate = autoCompactFillRate;
        return this;
    }

    /**
     * The size of the write buffer. Defaults to 1024KB.
     * @param autoCommitBufferSize size of the write buffer
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setAutoCommitBufferSize(final Long autoCommitBufferSize) {
        this.autoCommitBufferSize = autoCommitBufferSize;
        return this;
    }

    /**
     * The maximum delay in milliseconds to auto-commit changes. Defaults
     * to 1000ms (1 second).
     * @param autoCommitDelay maximum delay to auto-commit changes
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setAutoCommitDelay(final Long autoCommitDelay) {
        this.autoCommitDelay = autoCommitDelay;
        return this;
    }

    /**
     * Stores data in memory and does not persist any information
     * between each crawling sessions (breaking from the crawler
     * normal behavior).
     * <b>Not recommended for regular use.</b>
     * Useful for testing and troubleshooting, or if you know what your doing.
     * @param ephemeral whether to persist store data or keep it all in memory
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public MVStoreDataStoreEngineConfig setEphemeral(final boolean ephemeral) {
        this.ephemeral = ephemeral;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof MVStoreDataStoreEngineConfig)) return false;
        final MVStoreDataStoreEngineConfig other = (MVStoreDataStoreEngineConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.isEphemeral() != other.isEphemeral()) return false;
        final java.lang.Object this$pageSplitSize = this.getPageSplitSize();
        final java.lang.Object other$pageSplitSize = other.getPageSplitSize();
        if (this$pageSplitSize == null ? other$pageSplitSize != null : !this$pageSplitSize.equals(other$pageSplitSize)) return false;
        final java.lang.Object this$compress = this.getCompress();
        final java.lang.Object other$compress = other.getCompress();
        if (this$compress == null ? other$compress != null : !this$compress.equals(other$compress)) return false;
        final java.lang.Object this$cacheConcurrency = this.getCacheConcurrency();
        final java.lang.Object other$cacheConcurrency = other.getCacheConcurrency();
        if (this$cacheConcurrency == null ? other$cacheConcurrency != null : !this$cacheConcurrency.equals(other$cacheConcurrency)) return false;
        final java.lang.Object this$cacheSize = this.getCacheSize();
        final java.lang.Object other$cacheSize = other.getCacheSize();
        if (this$cacheSize == null ? other$cacheSize != null : !this$cacheSize.equals(other$cacheSize)) return false;
        final java.lang.Object this$autoCompactFillRate = this.getAutoCompactFillRate();
        final java.lang.Object other$autoCompactFillRate = other.getAutoCompactFillRate();
        if (this$autoCompactFillRate == null ? other$autoCompactFillRate != null : !this$autoCompactFillRate.equals(other$autoCompactFillRate)) return false;
        final java.lang.Object this$autoCommitBufferSize = this.getAutoCommitBufferSize();
        final java.lang.Object other$autoCommitBufferSize = other.getAutoCommitBufferSize();
        if (this$autoCommitBufferSize == null ? other$autoCommitBufferSize != null : !this$autoCommitBufferSize.equals(other$autoCommitBufferSize)) return false;
        final java.lang.Object this$autoCommitDelay = this.getAutoCommitDelay();
        final java.lang.Object other$autoCommitDelay = other.getAutoCommitDelay();
        if (this$autoCommitDelay == null ? other$autoCommitDelay != null : !this$autoCommitDelay.equals(other$autoCommitDelay)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof MVStoreDataStoreEngineConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isEphemeral() ? 79 : 97);
        final java.lang.Object $pageSplitSize = this.getPageSplitSize();
        result = result * PRIME + ($pageSplitSize == null ? 43 : $pageSplitSize.hashCode());
        final java.lang.Object $compress = this.getCompress();
        result = result * PRIME + ($compress == null ? 43 : $compress.hashCode());
        final java.lang.Object $cacheConcurrency = this.getCacheConcurrency();
        result = result * PRIME + ($cacheConcurrency == null ? 43 : $cacheConcurrency.hashCode());
        final java.lang.Object $cacheSize = this.getCacheSize();
        result = result * PRIME + ($cacheSize == null ? 43 : $cacheSize.hashCode());
        final java.lang.Object $autoCompactFillRate = this.getAutoCompactFillRate();
        result = result * PRIME + ($autoCompactFillRate == null ? 43 : $autoCompactFillRate.hashCode());
        final java.lang.Object $autoCommitBufferSize = this.getAutoCommitBufferSize();
        result = result * PRIME + ($autoCommitBufferSize == null ? 43 : $autoCommitBufferSize.hashCode());
        final java.lang.Object $autoCommitDelay = this.getAutoCommitDelay();
        result = result * PRIME + ($autoCommitDelay == null ? 43 : $autoCommitDelay.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "MVStoreDataStoreEngineConfig(pageSplitSize=" + this.getPageSplitSize() + ", compress=" + this.getCompress() + ", cacheConcurrency=" + this.getCacheConcurrency() + ", cacheSize=" + this.getCacheSize() + ", autoCompactFillRate=" + this.getAutoCompactFillRate() + ", autoCommitBufferSize=" + this.getAutoCommitBufferSize() + ", autoCommitDelay=" + this.getAutoCommitDelay() + ", ephemeral=" + this.isEphemeral() + ")";
    }


    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public static final class Fields {
        public static final java.lang.String pageSplitSize = "pageSplitSize";
        public static final java.lang.String compress = "compress";
        public static final java.lang.String cacheConcurrency = "cacheConcurrency";
        public static final java.lang.String cacheSize = "cacheSize";
        public static final java.lang.String autoCompactFillRate = "autoCompactFillRate";
        public static final java.lang.String autoCommitBufferSize = "autoCommitBufferSize";
        public static final java.lang.String autoCommitDelay = "autoCommitDelay";
        public static final java.lang.String ephemeral = "ephemeral";
    }
}
