// Generated by delombok at Fri Mar 08 16:24:20 MST 2024
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
package com.norconex.committer.sql;

import java.io.Serializable;
import com.norconex.committer.core.batch.BaseBatchCommitterConfig;
import com.norconex.commons.lang.collection.CollectionUtil;
import com.norconex.commons.lang.map.Properties;
import com.norconex.commons.lang.security.Credentials;

/**
 * <p>
 * SQL Committer configuration.
 * </p>
 */
public class SQLCommitterConfig extends BaseBatchCommitterConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Default SQL content field
     */
    public static final String DEFAULT_SQL_CONTENT_FIELD = "content";
    /**
     * Default multi-value join string
     */
    public static final String DEFAULT_MULTI_VALUES_JOINER = "|";
    private String driverPath;
    private String driverClass;
    private String connectionUrl;
    private final Credentials credentials = new Credentials();
    private final Properties properties = new Properties();
    private String tableName;
    private String primaryKey;
    private String createTableSQL;
    private String createFieldSQL;
    private boolean fixFieldNames;
    private boolean fixFieldValues;
    private String multiValuesJoiner = DEFAULT_MULTI_VALUES_JOINER;
    private String targetContentField = DEFAULT_SQL_CONTENT_FIELD;

    public Credentials getCredentials() {
        return credentials;
    }

    public SQLCommitterConfig setCredentials(Credentials credentials) {
        this.credentials.copyFrom(credentials);
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public SQLCommitterConfig setProperties(Properties properties) {
        CollectionUtil.setAll(this.properties, properties);
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig() {
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getDriverPath() {
        return this.driverPath;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getDriverClass() {
        return this.driverClass;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getConnectionUrl() {
        return this.connectionUrl;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getTableName() {
        return this.tableName;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getPrimaryKey() {
        return this.primaryKey;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getCreateTableSQL() {
        return this.createTableSQL;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getCreateFieldSQL() {
        return this.createFieldSQL;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isFixFieldNames() {
        return this.fixFieldNames;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean isFixFieldValues() {
        return this.fixFieldValues;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getMultiValuesJoiner() {
        return this.multiValuesJoiner;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public String getTargetContentField() {
        return this.targetContentField;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setDriverPath(final String driverPath) {
        this.driverPath = driverPath;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setDriverClass(final String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setConnectionUrl(final String connectionUrl) {
        this.connectionUrl = connectionUrl;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setTableName(final String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setPrimaryKey(final String primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setCreateTableSQL(final String createTableSQL) {
        this.createTableSQL = createTableSQL;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setCreateFieldSQL(final String createFieldSQL) {
        this.createFieldSQL = createFieldSQL;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setFixFieldNames(final boolean fixFieldNames) {
        this.fixFieldNames = fixFieldNames;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setFixFieldValues(final boolean fixFieldValues) {
        this.fixFieldValues = fixFieldValues;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setMultiValuesJoiner(final String multiValuesJoiner) {
        this.multiValuesJoiner = multiValuesJoiner;
        return this;
    }

    /**
     * @return {@code this}.
     */
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public SQLCommitterConfig setTargetContentField(final String targetContentField) {
        this.targetContentField = targetContentField;
        return this;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof SQLCommitterConfig)) return false;
        final SQLCommitterConfig other = (SQLCommitterConfig) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (!super.equals(o)) return false;
        if (this.isFixFieldNames() != other.isFixFieldNames()) return false;
        if (this.isFixFieldValues() != other.isFixFieldValues()) return false;
        final java.lang.Object this$driverPath = this.getDriverPath();
        final java.lang.Object other$driverPath = other.getDriverPath();
        if (this$driverPath == null ? other$driverPath != null : !this$driverPath.equals(other$driverPath)) return false;
        final java.lang.Object this$driverClass = this.getDriverClass();
        final java.lang.Object other$driverClass = other.getDriverClass();
        if (this$driverClass == null ? other$driverClass != null : !this$driverClass.equals(other$driverClass)) return false;
        final java.lang.Object this$connectionUrl = this.getConnectionUrl();
        final java.lang.Object other$connectionUrl = other.getConnectionUrl();
        if (this$connectionUrl == null ? other$connectionUrl != null : !this$connectionUrl.equals(other$connectionUrl)) return false;
        final java.lang.Object this$credentials = this.getCredentials();
        final java.lang.Object other$credentials = other.getCredentials();
        if (this$credentials == null ? other$credentials != null : !this$credentials.equals(other$credentials)) return false;
        final java.lang.Object this$properties = this.getProperties();
        final java.lang.Object other$properties = other.getProperties();
        if (this$properties == null ? other$properties != null : !this$properties.equals(other$properties)) return false;
        final java.lang.Object this$tableName = this.getTableName();
        final java.lang.Object other$tableName = other.getTableName();
        if (this$tableName == null ? other$tableName != null : !this$tableName.equals(other$tableName)) return false;
        final java.lang.Object this$primaryKey = this.getPrimaryKey();
        final java.lang.Object other$primaryKey = other.getPrimaryKey();
        if (this$primaryKey == null ? other$primaryKey != null : !this$primaryKey.equals(other$primaryKey)) return false;
        final java.lang.Object this$createTableSQL = this.getCreateTableSQL();
        final java.lang.Object other$createTableSQL = other.getCreateTableSQL();
        if (this$createTableSQL == null ? other$createTableSQL != null : !this$createTableSQL.equals(other$createTableSQL)) return false;
        final java.lang.Object this$createFieldSQL = this.getCreateFieldSQL();
        final java.lang.Object other$createFieldSQL = other.getCreateFieldSQL();
        if (this$createFieldSQL == null ? other$createFieldSQL != null : !this$createFieldSQL.equals(other$createFieldSQL)) return false;
        final java.lang.Object this$multiValuesJoiner = this.getMultiValuesJoiner();
        final java.lang.Object other$multiValuesJoiner = other.getMultiValuesJoiner();
        if (this$multiValuesJoiner == null ? other$multiValuesJoiner != null : !this$multiValuesJoiner.equals(other$multiValuesJoiner)) return false;
        final java.lang.Object this$targetContentField = this.getTargetContentField();
        final java.lang.Object other$targetContentField = other.getTargetContentField();
        if (this$targetContentField == null ? other$targetContentField != null : !this$targetContentField.equals(other$targetContentField)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof SQLCommitterConfig;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        result = result * PRIME + (this.isFixFieldNames() ? 79 : 97);
        result = result * PRIME + (this.isFixFieldValues() ? 79 : 97);
        final java.lang.Object $driverPath = this.getDriverPath();
        result = result * PRIME + ($driverPath == null ? 43 : $driverPath.hashCode());
        final java.lang.Object $driverClass = this.getDriverClass();
        result = result * PRIME + ($driverClass == null ? 43 : $driverClass.hashCode());
        final java.lang.Object $connectionUrl = this.getConnectionUrl();
        result = result * PRIME + ($connectionUrl == null ? 43 : $connectionUrl.hashCode());
        final java.lang.Object $credentials = this.getCredentials();
        result = result * PRIME + ($credentials == null ? 43 : $credentials.hashCode());
        final java.lang.Object $properties = this.getProperties();
        result = result * PRIME + ($properties == null ? 43 : $properties.hashCode());
        final java.lang.Object $tableName = this.getTableName();
        result = result * PRIME + ($tableName == null ? 43 : $tableName.hashCode());
        final java.lang.Object $primaryKey = this.getPrimaryKey();
        result = result * PRIME + ($primaryKey == null ? 43 : $primaryKey.hashCode());
        final java.lang.Object $createTableSQL = this.getCreateTableSQL();
        result = result * PRIME + ($createTableSQL == null ? 43 : $createTableSQL.hashCode());
        final java.lang.Object $createFieldSQL = this.getCreateFieldSQL();
        result = result * PRIME + ($createFieldSQL == null ? 43 : $createFieldSQL.hashCode());
        final java.lang.Object $multiValuesJoiner = this.getMultiValuesJoiner();
        result = result * PRIME + ($multiValuesJoiner == null ? 43 : $multiValuesJoiner.hashCode());
        final java.lang.Object $targetContentField = this.getTargetContentField();
        result = result * PRIME + ($targetContentField == null ? 43 : $targetContentField.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "SQLCommitterConfig(super=" + super.toString() + ", driverPath=" + this.getDriverPath() + ", driverClass=" + this.getDriverClass() + ", connectionUrl=" + this.getConnectionUrl() + ", credentials=" + this.getCredentials() + ", properties=" + this.getProperties() + ", tableName=" + this.getTableName() + ", primaryKey=" + this.getPrimaryKey() + ", createTableSQL=" + this.getCreateTableSQL() + ", createFieldSQL=" + this.getCreateFieldSQL() + ", fixFieldNames=" + this.isFixFieldNames() + ", fixFieldValues=" + this.isFixFieldValues() + ", multiValuesJoiner=" + this.getMultiValuesJoiner() + ", targetContentField=" + this.getTargetContentField() + ")";
    }
}
