// Generated by delombok at Fri Mar 08 16:25:39 MST 2024
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
package com.norconex.importer.handler.transformer.impl;

import com.norconex.commons.lang.convert.DimensionConverter;
import com.norconex.commons.lang.text.TextMatcher;
import com.norconex.importer.ImporterConfig;
import com.norconex.importer.handler.CommonMatchers;
import com.norconex.importer.handler.CommonRestrictions;

/**
 * <p>
 * Transforms an image using common image operations.
 * </p>
 * <p>
 * This class should only be used as a pre-parsing handler, on image files.
 * It may also be appropriate to disable parsing of those images if you
 * want to keep the transformed version intact. This can be done with
 * {@link ParseConfig}, obtained via the {@link ImporterConfig}.
 * </p>
 *
 * <h3>Content-types</h3>
 * <p>
 * By default, this filter is restricted to (applies only to) documents matching
 * the restrictions returned by
 * {@link CommonRestrictions#imageIOStandardContentTypes(String)}.
 * You can specify your own content types if you know they represent a supported
 * image.
 * </p>
 *
 * <h3>Image dimension format</h3>
 * <p>
 * For a list of supported image dimension formats, refer to
 * {@link DimensionConverter}.
 * </p>
 *
 * {@nx.xml.usage
 * <handler class="com.norconex.importer.handler.transformer.impl.ImageTransformer"
 *      targetFormat="(jpg, png, gif, bmp, wbmp, or other supported format)">
 *
 *   {@nx.include com.norconex.importer.handler.AbstractImporterHandler#restrictTo}
 *
 *   <scale
 *       stretch="[false|true]"
 *       factor="(decimal value ratio factor, default is 1)"
 *       dimension="(target dimension, in pixels, format: [width]x[height])" />
 *
 *   <rotate degrees="(-360 to 360)"/>
 *
 *   <crop
 *       x="(top-left x-axis, default 0)"
 *       y="(top-left y-axis, default 0)"
 *       dimension="(crop dimension, in pixels, format: [width]x[height])"/>
 * </handler>
 * }
 *
 * {@nx.xml.example
 * <handler class="ImageTransformer" targetFormat="png">
 *   <scale dimension="400x250" />
 * </handler>
 * }
 * <h4>Usage example:</h4>
 * <p>
 * The above example converts images to PNG while scaling it to a maximum
 * dimension of 400 pixels wide and 250 pixel high.
 * </p>
 *
 * @see ExternalHandler
 */
@SuppressWarnings("javadoc")
public class ImageTransformerConfig {
    public static final String DEFAULT_TARGET_FORMAT = "png";
    //TODO Maybe: default == null == keep same as source,
    // derived from detected content-type
    private String targetFormat = DEFAULT_TARGET_FORMAT;
    private Double rotation;
    private final Scale scale = new Scale();
    private final Crop crop = new Crop();
    /**
     * The matcher of content types to apply transformation on. No attempt to
     * transform documents of any other content types will be made. Default is
     * {@link CommonMatchers#IMAGE_IO_CONTENT_TYPES}.
     * @param contentTypeMatcher content type matcher
     */
    private final TextMatcher contentTypeMatcher = CommonMatchers.imageIOStandardContentTypes();

    /**
     * The matcher of content types to apply transformation on. No attempt to
     * transform documents of any other content types will be made. Default is
     * {@link CommonMatchers#IMAGE_IO_CONTENT_TYPES}.
     * @param contentTypeMatcher content type matcher
     * @return this
     */
    public ImageTransformerConfig setContentTypeMatcher(TextMatcher matcher) {
        contentTypeMatcher.copyFrom(matcher);
        return this;
    }


    public static class Scale {
        private boolean stretch;
        private Double factor;
        private Integer height;
        private Integer width;

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Scale() {
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean isStretch() {
            return this.stretch;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Double getFactor() {
            return this.factor;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Integer getHeight() {
            return this.height;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Integer getWidth() {
            return this.width;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Scale setStretch(final boolean stretch) {
            this.stretch = stretch;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Scale setFactor(final Double factor) {
            this.factor = factor;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Scale setHeight(final Integer height) {
            this.height = height;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Scale setWidth(final Integer width) {
            this.width = width;
            return this;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof ImageTransformerConfig.Scale)) return false;
            final ImageTransformerConfig.Scale other = (ImageTransformerConfig.Scale) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            if (this.isStretch() != other.isStretch()) return false;
            final java.lang.Object this$factor = this.getFactor();
            final java.lang.Object other$factor = other.getFactor();
            if (this$factor == null ? other$factor != null : !this$factor.equals(other$factor)) return false;
            final java.lang.Object this$height = this.getHeight();
            final java.lang.Object other$height = other.getHeight();
            if (this$height == null ? other$height != null : !this$height.equals(other$height)) return false;
            final java.lang.Object this$width = this.getWidth();
            final java.lang.Object other$width = other.getWidth();
            if (this$width == null ? other$width != null : !this$width.equals(other$width)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof ImageTransformerConfig.Scale;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.isStretch() ? 79 : 97);
            final java.lang.Object $factor = this.getFactor();
            result = result * PRIME + ($factor == null ? 43 : $factor.hashCode());
            final java.lang.Object $height = this.getHeight();
            result = result * PRIME + ($height == null ? 43 : $height.hashCode());
            final java.lang.Object $width = this.getWidth();
            result = result * PRIME + ($width == null ? 43 : $width.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "ImageTransformerConfig.Scale(stretch=" + this.isStretch() + ", factor=" + this.getFactor() + ", height=" + this.getHeight() + ", width=" + this.getWidth() + ")";
        }
    }


    public static class Crop {
        private int x;
        private int y;
        private Integer height;
        private Integer width;

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Crop() {
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int getX() {
            return this.x;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int getY() {
            return this.y;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Integer getHeight() {
            return this.height;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public Integer getWidth() {
            return this.width;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Crop setX(final int x) {
            this.x = x;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Crop setY(final int y) {
            this.y = y;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Crop setHeight(final Integer height) {
            this.height = height;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public ImageTransformerConfig.Crop setWidth(final Integer width) {
            this.width = width;
            return this;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof ImageTransformerConfig.Crop)) return false;
            final ImageTransformerConfig.Crop other = (ImageTransformerConfig.Crop) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            if (this.getX() != other.getX()) return false;
            if (this.getY() != other.getY()) return false;
            final java.lang.Object this$height = this.getHeight();
            final java.lang.Object other$height = other.getHeight();
            if (this$height == null ? other$height != null : !this$height.equals(other$height)) return false;
            final java.lang.Object this$width = this.getWidth();
            final java.lang.Object other$width = other.getWidth();
            if (this$width == null ? other$width != null : !this$width.equals(other$width)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof ImageTransformerConfig.Crop;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getX();
            result = result * PRIME + this.getY();
            final java.lang.Object $height = this.getHeight();
            result = result * PRIME + ($height == null ? 43 : $height.hashCode());
            final java.lang.Object $width = this.getWidth();
            result = result * PRIME + ($width == null ? 43 : $width.hashCode());
            return result;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @lombok.Generated
        public java.lang.String toString() {
            return "ImageTransformerConfig.Crop(x=" + this.getX() + ", y=" + this.getY() + ", height=" + this.getHeight() + ", width=" + this.getWidth() + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public ImageTransformerConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getTargetFormat() {
        return this.targetFormat;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Double getRotation() {
        return this.rotation;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Scale getScale() {
        return this.scale;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public Crop getCrop() {
        return this.crop;
    }

    /**
     * The matcher of content types to apply transformation on. No attempt to
     * transform documents of any other content types will be made. Default is
     * {@link CommonMatchers#IMAGE_IO_CONTENT_TYPES}.
     * @return content type matcher
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public TextMatcher getContentTypeMatcher() {
        return this.contentTypeMatcher;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public ImageTransformerConfig setTargetFormat(final String targetFormat) {
        this.targetFormat = targetFormat;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public ImageTransformerConfig setRotation(final Double rotation) {
        this.rotation = rotation;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof ImageTransformerConfig)) return false;
        final ImageTransformerConfig other = (ImageTransformerConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$rotation = this.getRotation();
        final java.lang.Object other$rotation = other.getRotation();
        if (this$rotation == null ? other$rotation != null : !this$rotation.equals(other$rotation)) return false;
        final java.lang.Object this$targetFormat = this.getTargetFormat();
        final java.lang.Object other$targetFormat = other.getTargetFormat();
        if (this$targetFormat == null ? other$targetFormat != null : !this$targetFormat.equals(other$targetFormat)) return false;
        final java.lang.Object this$scale = this.getScale();
        final java.lang.Object other$scale = other.getScale();
        if (this$scale == null ? other$scale != null : !this$scale.equals(other$scale)) return false;
        final java.lang.Object this$crop = this.getCrop();
        final java.lang.Object other$crop = other.getCrop();
        if (this$crop == null ? other$crop != null : !this$crop.equals(other$crop)) return false;
        final java.lang.Object this$contentTypeMatcher = this.getContentTypeMatcher();
        final java.lang.Object other$contentTypeMatcher = other.getContentTypeMatcher();
        if (this$contentTypeMatcher == null ? other$contentTypeMatcher != null : !this$contentTypeMatcher.equals(other$contentTypeMatcher)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof ImageTransformerConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $rotation = this.getRotation();
        result = result * PRIME + ($rotation == null ? 43 : $rotation.hashCode());
        final java.lang.Object $targetFormat = this.getTargetFormat();
        result = result * PRIME + ($targetFormat == null ? 43 : $targetFormat.hashCode());
        final java.lang.Object $scale = this.getScale();
        result = result * PRIME + ($scale == null ? 43 : $scale.hashCode());
        final java.lang.Object $crop = this.getCrop();
        result = result * PRIME + ($crop == null ? 43 : $crop.hashCode());
        final java.lang.Object $contentTypeMatcher = this.getContentTypeMatcher();
        result = result * PRIME + ($contentTypeMatcher == null ? 43 : $contentTypeMatcher.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "ImageTransformerConfig(targetFormat=" + this.getTargetFormat() + ", rotation=" + this.getRotation() + ", scale=" + this.getScale() + ", crop=" + this.getCrop() + ", contentTypeMatcher=" + this.getContentTypeMatcher() + ")";
    }
}