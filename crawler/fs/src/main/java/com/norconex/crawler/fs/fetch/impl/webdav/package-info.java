@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(
            value=XMLAdapters.DurationAdapter.class, type=Duration.class)
})
package com.norconex.crawler.fs.fetch.impl.webdav;

import java.time.Duration;

import com.norconex.commons.lang.xml.XMLAdapters;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
