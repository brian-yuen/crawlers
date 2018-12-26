/* Copyright 2016-2018 Norconex Inc.
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
package com.norconex.collector.http.recrawl.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norconex.collector.http.recrawl.IRecrawlableResolver;
import com.norconex.collector.http.recrawl.PreviousCrawlData;
import com.norconex.collector.http.sitemap.SitemapChangeFrequency;
import com.norconex.commons.lang.collection.CollectionUtil;
import com.norconex.commons.lang.time.DurationParser;
import com.norconex.commons.lang.xml.IXMLConfigurable;
import com.norconex.commons.lang.xml.XML;

/**
 * <p>Relies on both sitemap directives and custom instructions for
 * establishing the minimum frequency between each document recrawl.
 * </p>
 *
 * <h3>Sitemap support:</h3>
 * <p>
 * Provided crawler support for sitemaps has not been disabled,
 * this class tries to honor last modified and frequency directives found
 * in sitemap files.
 * </p>
 * <p>
 * By default, existing sitemap directives take precedence over custom ones.
 * You chose to have sitemap directives be considered last or even disable
 * sitemap directives using the {@link #setSitemapSupport(SitemapSupport)}
 * method.
 * </p>
 *
 * <h3>Custom recrawl frequencies:</h3>
 * <p>
 * You can chose to have some of your crawled documents be re-crawled less
 * frequently than others by specifying custom minimum frequencies
 * ({@link #setMinFrequencies(MinFrequency...)}). Minimum frequencies are
 * processed in the order specified and must each have to following:
 * </p>
 * <ul>
 *   <li>applyTo: Either "reference" or "contentType"
 *       (defaults to "reference").</li>
 *   <li>pattern: A regular expression.</li>
 *   <li>value: one of "always", "hourly", "daily", "weekly", "monthly",
 *       "yearly", "never", or a numeric value in milliseconds.</li>
 * </ul>
 *
 * <p>
 * As of 2.7.0, XML configuration entries expecting millisecond durations
 * can be provided in human-readable format (English only), as per
 * {@link DurationParser} (e.g., "5 minutes and 30 seconds" or "5m30s").
 * </p>
 *
 * <h3>XML configuration usage:</h3>
 * <pre>
 *  &lt;recrawlableResolver
 *         class="com.norconex.collector.http.recrawl.impl.GenericRecrawlableResolver"
 *         sitemapSupport="[first|last|never]" &gt;
 *
 *     &lt;minFrequency applyTo="[reference|contentType]" caseSensitive="[false|true]"
 *             value="([always|hourly|daily|weekly|monthly|yearly|never] or milliseconds)" &gt;
 *         (regex pattern)
 *     &lt;/minFrequency&gt;
 *     (... repeat frequency tag as needed ...)
 *
 *  &lt;/recrawlableResolver&gt;
 * </pre>
 *
 * <h4>Usage example:</h4>
 * <p>
 * The following example ensures PDFs recrawled no more frequently than
 * once a month, while HTML news can be crawled as fast at every half hour.
 * For the rest, it relies on the website sitemap directives (if any).
 * </p>
 * <pre>
 *  &lt;recrawlableResolver
 *         class="com.norconex.collector.http.recrawl.impl.GenericRecrawlableResolver"
 *         sitemapSupport="last" &gt;
 *     &lt;minFrequency applyTo="contentType" value="monthly"&gt;application/pdf&lt;/minFrequency&gt;
 *     &lt;minFrequency applyTo="reference" value="1800000"&gt;.*latest-news.*\.html&lt;/minFrequency&gt;
 *  &lt;/recrawlableResolver&gt;
 * </pre>
 *
 * @author Pascal Essiembre
 * @since 2.5.0
 */
public class GenericRecrawlableResolver
        implements IRecrawlableResolver, IXMLConfigurable{

    private static final Logger LOG =
            LoggerFactory.getLogger(GenericRecrawlableResolver.class);

    public enum SitemapSupport {
        FIRST, LAST, NEVER;
        public static SitemapSupport getSitemapSupport(String sitemapSupport) {
            if (StringUtils.isBlank(sitemapSupport)) {
                return null;
            }
            for (SitemapSupport v : SitemapSupport.values()) {
                if (v.toString().equalsIgnoreCase(sitemapSupport)) {
                    return v;
                }
            }
            return null;
        }
    }

    private SitemapSupport sitemapSupport = SitemapSupport.FIRST;
    private final List<MinFrequency> minFrequencies = new ArrayList<>();

    /**
     * Gets the sitemap support strategy. Defualt is
     * {@link SitemapSupport#FIRST}.
     * @return sitemap support strategy
     */
    public SitemapSupport getSitemapSupport() {
        return sitemapSupport;
    }
    /**
     * Sets the sitemap support strategy. A <code>null</code> value
     * is equivalent to specifying the default {@link SitemapSupport#FIRST}.
     * @param sitemapSupport sitemap support strategy
     */
    public void setSitemapSupport(SitemapSupport sitemapSupport) {
        this.sitemapSupport = sitemapSupport;
    }

    /**
     * Gets minimum frequencies.
     * @return minimum frequencies
     */
    public List<MinFrequency> getMinFrequencies() {
        return Collections.unmodifiableList(minFrequencies);
    }
    /**
     * Sets minimum frequencies.
     * @param minFrequencies minimum frequencies
     */
    public void setMinFrequencies(MinFrequency... minFrequencies) {
        CollectionUtil.setAll(this.minFrequencies, minFrequencies);
    }
    /**
     * Sets minimum frequencies.
     * @param minFrequencies minimum frequencies
     * @since 3.0.0
     */
    public void setMinFrequencies(List<MinFrequency> minFrequencies) {
        CollectionUtil.setAll(this.minFrequencies, minFrequencies);
    }

    @Override
    public boolean isRecrawlable(PreviousCrawlData prevData) {

        // if never crawled: yes, crawl it
        if (prevData.getCrawlDate() == null) {
            return true;
        }

        SitemapSupport ss = sitemapSupport;
        if (ss == null) {
            ss = SitemapSupport.FIRST;
        }
        boolean hasSitemapInstructions =
                hasSitemapFrequency(prevData)
                        || hasSitemapLastModified(prevData);

        if (ss == SitemapSupport.FIRST && hasSitemapInstructions) {
            return isRecrawlableFromSitemap(prevData);
        }

        MinFrequency f = getMatchingMinFrequency(prevData);
        if (f != null) {
            return isRecrawlableFromMinFrequencies(f, prevData);
        }

        if (ss == SitemapSupport.LAST && hasSitemapInstructions) {
            return isRecrawlableFromSitemap(prevData);
        }

        // if we have not found a reason not to recrawl, then recrawl
        return true;
    }

    private MinFrequency getMatchingMinFrequency(PreviousCrawlData prevData) {
        for (MinFrequency f : minFrequencies) {
            if (f.pattern == null || f.value == null) {
                LOG.warn("Value or pattern missing in minimum frequency.");
                continue;
            }
            String applyTo = f.getApplyTo();
            if (StringUtils.isBlank(applyTo)) {
                applyTo = "reference";
            }
            if ("reference".equalsIgnoreCase(applyTo)
                    && f.getCachedPattern().matcher(
                            prevData.getReference()).matches()) {
                return f;
            }
            if ("contentType".equalsIgnoreCase(applyTo)
                    && f.getCachedPattern().matcher(
                            prevData.getContentType().toString()).matches()) {
                return f;
            }
        }
        return null;
    }


    private boolean hasSitemapFrequency(PreviousCrawlData prevData) {
        return StringUtils.isNotBlank(prevData.getSitemapChangeFreq());
    }
    private boolean hasSitemapLastModified(PreviousCrawlData prevData) {
        return prevData.getSitemapLastMod() != null
                && prevData.getSitemapLastMod() > 0;
    }

    private boolean isRecrawlableFromMinFrequencies(
            MinFrequency f, PreviousCrawlData prevData) {
        String value = f.getValue();
        if (StringUtils.isBlank(value)) {
            return true;
        }

        SitemapChangeFrequency cf =
                SitemapChangeFrequency.getChangeFrequency(value);
        if (cf != null) {
            return isRecrawlableFromFrequency(cf, prevData, "custom");
        }

        int millis;
        if (NumberUtils.isDigits(value)) {
            millis = NumberUtils.toInt(value);
        } else {
            millis = (int) new DurationParser().parseToMillis(value);
        }
        DateTime minCrawlDate = new DateTime(prevData.getCrawlDate());
        minCrawlDate = minCrawlDate.plusMillis(millis);
        if (minCrawlDate.isBeforeNow()) {
            LOG.debug("Recrawl suggested according to custom "
                    + "directive (min frequency < elapsed "
                    + "time since {}) for: {}",
                    prevData.getCrawlDate(), prevData.getReference());
            return true;
        }
        LOG.debug("No recrawl suggested according to custom directive "
                + "(min frequency >= elapsed time since {}) for: {}",
                prevData.getCrawlDate(), prevData.getReference());
        return false;
    }

    private boolean isRecrawlableFromSitemap(PreviousCrawlData prevData) {

        // If sitemap specifies a last modified date and it is more recent
        // than the the document last crawl date, recrawl it (otherwise don't).
        if (hasSitemapLastModified(prevData)) {
            DateTime lastModified = new DateTime(prevData.getSitemapLastMod());
            LOG.debug("Sitemap last modified date is "
                    + lastModified + " for: " + prevData.getReference());
            if (lastModified.isAfter(prevData.getCrawlDate().getTime())) {
                LOG.debug("Recrawl suggested according to sitemap directive "
                        + "(last modified > last crawl date) for: {}",
                        prevData.getReference());
                return true;
            }
            LOG.debug("No recrawl suggested according to sitemap directive "
                    + "(last modified <= last crawl date) for: {}",
                    prevData.getReference());
            return false;
        }

        // If sitemap specifies a change frequency, check if we are past
        // it and recrawl if so (otherwise don't).
        SitemapChangeFrequency cf = SitemapChangeFrequency.getChangeFrequency(
                prevData.getSitemapChangeFreq());

        return isRecrawlableFromFrequency(cf, prevData, "Sitemap");
    }


    private boolean isRecrawlableFromFrequency(
            SitemapChangeFrequency cf, PreviousCrawlData prevData,
            String context) {
        if (cf == null) {
            return true;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("The " + context + " change frequency is "
                    + cf + " for: " + prevData.getReference());
        }
        if (cf == SitemapChangeFrequency.ALWAYS) {
            return true;
        }
        if (cf == SitemapChangeFrequency.NEVER) {
            return false;
        }

        DateTime minCrawlDate = new DateTime(prevData.getCrawlDate());
        switch (cf) {
        case HOURLY:
            minCrawlDate = minCrawlDate.plusHours(1);
            break;
        case DAILY:
            minCrawlDate = minCrawlDate.plusDays(1);
            break;
        case WEEKLY:
            minCrawlDate = minCrawlDate.plusWeeks(1);
            break;
        case MONTHLY:
            minCrawlDate = minCrawlDate.plusMonths(1);
            break;
        case YEARLY:
            minCrawlDate = minCrawlDate.plusYears(1);
            break;
        default:
            break;
        }

        if (minCrawlDate.isBeforeNow()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Recrawl suggested according to " + context
                        + " directive (change frequency < elapsed time since "
                        + prevData.getCrawlDate() + ") for: "
                        + prevData.getReference());
            }
            return true;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("No recrawl suggested according to " + context
                    + " directive (change frequency >= elapsed time since "
                    + prevData.getCrawlDate() + ") for: "
                    + prevData.getReference());
        }
        return false;
    }

    public static class MinFrequency {
        private String applyTo;
        private String value;
        private String pattern;
        private Pattern cachedPattern;
        private boolean caseSensitive;
        public MinFrequency() {
            super();
        }
        public MinFrequency(String applyTo, String value, String pattern) {
            super();
            this.applyTo = applyTo;
            this.value = value;
            setPattern(pattern);
        }
        public String getApplyTo() {
            return applyTo;
        }
        public void setApplyTo(String applyTo) {
            this.applyTo = applyTo;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getPattern() {
            return pattern;
        }
        public void setPattern(String pattern) {
            this.pattern = pattern;
            cachedPattern = null;
        }
        public boolean isCaseSensitive() {
            return caseSensitive;
        }
        public void setCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            cachedPattern = null;
        }

        private synchronized Pattern getCachedPattern() {
            if (cachedPattern != null) {
                return cachedPattern;
            }
            Pattern p;
            if (pattern == null) {
                p = null;
            } else {
                int flags = Pattern.DOTALL;
                if (!caseSensitive) {
                    flags = flags | Pattern.CASE_INSENSITIVE
                            | Pattern.UNICODE_CASE;
                }
                p = Pattern.compile(pattern, flags);
            }
            cachedPattern = p;
            return p;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("applyTo", applyTo)
                    .append("value", value)
                    .append("pattern", pattern)
                    .append("caseSensitive", caseSensitive)
                    .toString();
        }
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof MinFrequency)) {
                return false;
            }
            MinFrequency castOther = (MinFrequency) other;
            return new EqualsBuilder()
                    .append(applyTo, castOther.applyTo)
                    .append(value, castOther.value)
                    .append(pattern, castOther.pattern)
                    .append(caseSensitive, castOther.caseSensitive)
                    .isEquals();
        }
        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                        .append(applyTo)
                        .append(value)
                        .append(pattern)
                        .append(caseSensitive)
                        .toHashCode();
        }
    }

    @Override
    public void loadFromXML(XML xml) {
        String smsXml = xml.getString("@sitemapSupport");
        if (StringUtils.isNotBlank(smsXml)) {
            SitemapSupport sms = SitemapSupport.getSitemapSupport(smsXml);
            if (sms == null) {
                LOG.warn("Unsupported sitemap support value: \"{}\". "
                        + "Will use default.", smsXml);
            }
            setSitemapSupport(sms);
        }

        List<MinFrequency> frequencies = new ArrayList<>();
        for (XML x : xml.getXMLList("minFrequency")) {
            MinFrequency f = new MinFrequency();
            f.setApplyTo(x.getString("@applyTo"));
            f.setCaseSensitive(x.getBoolean("@caseSensitive", false));
            f.setValue(x.getString("@value"));
            f.setPattern(x.getString("."));
            frequencies.add(f);
        }

        setMinFrequencies(frequencies);
    }
    @Override
    public void saveToXML(XML xml) {
        xml.setAttribute("sitemapSupport", sitemapSupport);
        for (MinFrequency mf : minFrequencies) {
            xml.addElement("minFrequency", mf.pattern)
                    .setAttribute("applyTo", mf.applyTo)
                    .setAttribute("value", mf.value)
                    .setAttribute("caseSensitive", mf.caseSensitive);
        }
    }

    @Override
    public boolean equals(final Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this,
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}