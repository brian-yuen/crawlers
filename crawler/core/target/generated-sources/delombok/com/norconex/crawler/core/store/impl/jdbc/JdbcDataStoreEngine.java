// Generated by delombok at Fri Mar 08 16:24:33 MST 2024
/* Copyright 2021-2023 Norconex Inc.
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
package com.norconex.crawler.core.store.impl.jdbc;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.removeStartIgnoreCase;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norconex.commons.lang.config.Configurable;
import com.norconex.crawler.core.crawler.Crawler;
import com.norconex.crawler.core.store.DataStore;
import com.norconex.crawler.core.store.DataStoreEngine;
import com.norconex.crawler.core.store.DataStoreException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * <p>
 * Data store engine using a JDBC-compatible database for storing
 * crawl data.
 * </p>
 * <h3>Database JDBC driver</h3>
 * <p>
 * To use this data store engine, you need its JDBC database driver
 * on the classpath.
 * </p>
 * <h3>Database datasource configuration</h3>
 * <p>
 * This JDBC data store engine uses
 * <a href="https://github.com/brettwooldridge/HikariCP">Hikari</a> as the JDBC
 * datasource implementation, which provides efficient connection-pooling.
 * Refer to
 * <a href="https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby">
 * Hikari's documentation</a> for all configuration options.  The Hikari options
 * are passed as-is, via <code>datasource</code> properties as shown below.
 * </p>
 * <h3>Data types</h3>
 * <p>
 * This class only use a few data types to store its data in a generic way.
 * It will try to detect what data type to use for your database. If you
 * get errors related to field data types not being supported, you have
 * the option to redefined them.
 * </p>
 *
 * {@nx.xml.usage
 * <dataStoreEngine class="com.norconex.crawler.core.store.impl.jdbc.JdbcDataStoreEngine">
 *   <!-- Hikari datasource configuration properties: -->
 *   <datasource>
 *     <property name="(property name)">(property value)</property>
 *   </datasource>
 *   <tablePrefix>
 *     (Optional prefix used for table creation. Default is the collector
 *      id plus the crawler id, each followed by an underscore character.
 *      The value is first modified to convert spaces to underscores, and
 *      to strip unsupported characters. The supported
 *      characters are: alphanumeric, period, and underscore.
 *      )
 *   </tablePrefix>
 *   <!--
 *     Optionally overwrite default SQL data type used.  You should only
 *     use if you get data type-related errors.
 *     -->
 *   <dataTypes>
 *     <varchar   use="(equivalent data type for your database)" />
 *     <timestamp use="(equivalent data type for your database)" />
 *     <text      use="(equivalent data type for your database)" />
 *   </dataTypes>
 * </dataStoreEngine>
 * }
 *
 * {@nx.xml.example
 * <dataStoreEngine class="JdbcDataStoreEngine">
 *   <datasource>
 *     <property name="jdbcUrl">jdbc:mysql://localhost:33060/sample</property>
 *     <property name="username">dbuser</property>
 *     <property name="password">dbpwd</property>
 *     <property name="connectionTimeout">1000</property>
 *   </datasource>
 * </dataStoreEngine>
 * }
 * <p>
 * The above example contains basic settings for creating a MySQL data source.
 * </p>
 */
public class JdbcDataStoreEngine implements DataStoreEngine, Configurable<JdbcDataStoreEngineConfig> {
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(JdbcDataStoreEngine.class);
    private static final String STORE_TYPES_NAME = "_storetypes";
    // Non-configurable:
    private HikariDataSource datasource;
    private String tablePrefix;
    // table id field is store name
    private JdbcDataStore<String> storeTypes;
    private TableAdapter tableAdapter;
    private String resolvedSafeTablePrefix;
    private JdbcDataStoreEngineConfig configuration = new JdbcDataStoreEngineConfig();

    @Override
    public void init(Crawler crawler) {
        resolvedSafeTablePrefix = SqlUtil.safeTableName(isBlank(tablePrefix) ? crawler.getCrawlSession().getId() + "_" + crawler.getId() + "_" : tablePrefix);
        // create data source
        datasource = new HikariDataSource(new HikariConfig(configuration.getProperties().toProperties()));
        tableAdapter = resolveTableAdapter();
        // store types for each table
        storeTypes = new JdbcDataStore<>(this, STORE_TYPES_NAME, String.class);
    }

    private TableAdapter resolveTableAdapter() {
        return TableAdapter.detect(StringUtils.firstNonBlank(datasource.getJdbcUrl(), datasource.getDriverClassName())).withIdType(configuration.getVarcharType()).withModifiedType(configuration.getTimestampType()).withJsonType(configuration.getTextType());
    }

    @Override
    public boolean clean() {
        // the table storing the store types is not returned by getStoreNames
        // so we have to explicitly delete it.
        var names = getStoreNames();
        var hasStores = !names.isEmpty();
        if (hasStores) {
            names.stream().forEach(this::dropStore);
        }
        dropStore(STORE_TYPES_NAME);
        return hasStores;
    }

    @Override
    public void close() {
        if (datasource != null) {
            LOG.info("Closing JDBC data store engine datasource...");
            datasource.close();
            LOG.info("JDBC Data store engine datasource closed.");
            datasource = null;
        } else {
            LOG.info("JDBC Data store engine datasource already closed.");
        }
    }

    @Override
    public <T> DataStore<T> openStore(String storeName, Class<? extends T> type) {
        storeTypes.save(storeName, type.getName());
        return new JdbcDataStore<>(this, storeName, type);
    }

    @Override
    public boolean dropStore(String storeName) {
        var tableName = tableName(storeName);
        if (!tableExist(tableName)) {
            return false;
        }
        try (var conn = datasource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                stmt.executeUpdate("DROP TABLE %s".formatted(tableName));
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            throw new DataStoreException("Could not drop table \'" + tableName + "\'.", e);
        }
        if (STORE_TYPES_NAME.equals(storeName)) {
            storeTypes = null;
        } else {
            storeTypes.delete(storeName);
        }
        return true;
    }

    @Override
    public boolean renameStore(DataStore<?> dataStore, String newStoreName) {
        JdbcDataStore<?> jdbcStore = (JdbcDataStore<?>) dataStore;
        var oldStoreName = jdbcStore.getName();
        var existed = ((JdbcDataStore<?>) dataStore).rename(newStoreName);
        storeTypes.delete(oldStoreName);
        storeTypes.save(newStoreName, jdbcStore.getType().getName());
        return existed;
    }

    @JsonIgnore
    @Override
    public Set<String> getStoreNames() {
        Set<String> names = new HashSet<>();
        try (var conn = datasource.getConnection()) {
            try (var rs = conn.getMetaData().getTables(null, null, "%", new String[] {"TABLE"})) {
                while (rs.next()) {
                    var tableName = rs.getString(3);
                    if (startsWithIgnoreCase(tableName, resolvedSafeTablePrefix)) {
                        // only add if not the table holding store types
                        var storeName = removeStartIgnoreCase(tableName, resolvedSafeTablePrefix);
                        if (!STORE_TYPES_NAME.equalsIgnoreCase(storeName)) {
                            names.add(storeName);
                        }
                    }
                }
            }
            return names;
        } catch (SQLException e) {
            throw new DataStoreException("Could not get store names.", e);
        }
    }

    @JsonIgnore
    @Override
    public Optional<Class<?>> getStoreType(String storeName) {
        if (storeName == null) {
            return Optional.empty();
        }
        var typeStr = storeTypes.find(storeName);
        if (typeStr.isPresent()) {
            try {
                return Optional.ofNullable(ClassUtils.getClass(typeStr.get()));
            } catch (ClassNotFoundException e) {
                throw new DataStoreException("Could not determine type of: " + storeName, e);
            }
        }
        return Optional.empty();
    }

    @JsonIgnore
    TableAdapter getTableAdapter() {
        return tableAdapter;
    }

    @JsonIgnore
    Connection getConnection() {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            throw new DataStoreException("Could not get database connection.", e);
        }
    }

    String tableName(String storeName) {
        return SqlUtil.safeTableName(resolvedSafeTablePrefix + storeName);
    }

    boolean tableExist(String tableName) {
        try (var conn = datasource.getConnection()) {
            try (var rs = conn.getMetaData().getTables(null, null, "%", new String[] {"TABLE"})) {
                while (rs.next()) {
                    if (rs.getString(3).equalsIgnoreCase(tableName)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataStoreException("Could not check if table \'" + tableName + "\' exists.", e);
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof JdbcDataStoreEngine)) return false;
        final JdbcDataStoreEngine other = (JdbcDataStoreEngine) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$datasource = this.datasource;
        final java.lang.Object other$datasource = other.datasource;
        if (this$datasource == null ? other$datasource != null : !this$datasource.equals(other$datasource)) return false;
        final java.lang.Object this$tablePrefix = this.tablePrefix;
        final java.lang.Object other$tablePrefix = other.tablePrefix;
        if (this$tablePrefix == null ? other$tablePrefix != null : !this$tablePrefix.equals(other$tablePrefix)) return false;
        final java.lang.Object this$storeTypes = this.storeTypes;
        final java.lang.Object other$storeTypes = other.storeTypes;
        if (this$storeTypes == null ? other$storeTypes != null : !this$storeTypes.equals(other$storeTypes)) return false;
        final java.lang.Object this$tableAdapter = this.getTableAdapter();
        final java.lang.Object other$tableAdapter = other.getTableAdapter();
        if (this$tableAdapter == null ? other$tableAdapter != null : !this$tableAdapter.equals(other$tableAdapter)) return false;
        final java.lang.Object this$resolvedSafeTablePrefix = this.resolvedSafeTablePrefix;
        final java.lang.Object other$resolvedSafeTablePrefix = other.resolvedSafeTablePrefix;
        if (this$resolvedSafeTablePrefix == null ? other$resolvedSafeTablePrefix != null : !this$resolvedSafeTablePrefix.equals(other$resolvedSafeTablePrefix)) return false;
        final java.lang.Object this$configuration = this.getConfiguration();
        final java.lang.Object other$configuration = other.getConfiguration();
        if (this$configuration == null ? other$configuration != null : !this$configuration.equals(other$configuration)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof JdbcDataStoreEngine;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $datasource = this.datasource;
        result = result * PRIME + ($datasource == null ? 43 : $datasource.hashCode());
        final java.lang.Object $tablePrefix = this.tablePrefix;
        result = result * PRIME + ($tablePrefix == null ? 43 : $tablePrefix.hashCode());
        final java.lang.Object $storeTypes = this.storeTypes;
        result = result * PRIME + ($storeTypes == null ? 43 : $storeTypes.hashCode());
        final java.lang.Object $tableAdapter = this.getTableAdapter();
        result = result * PRIME + ($tableAdapter == null ? 43 : $tableAdapter.hashCode());
        final java.lang.Object $resolvedSafeTablePrefix = this.resolvedSafeTablePrefix;
        result = result * PRIME + ($resolvedSafeTablePrefix == null ? 43 : $resolvedSafeTablePrefix.hashCode());
        final java.lang.Object $configuration = this.getConfiguration();
        result = result * PRIME + ($configuration == null ? 43 : $configuration.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public java.lang.String toString() {
        return "JdbcDataStoreEngine(datasource=" + this.datasource + ", tablePrefix=" + this.tablePrefix + ", storeTypes=" + this.storeTypes + ", tableAdapter=" + this.getTableAdapter() + ", resolvedSafeTablePrefix=" + this.resolvedSafeTablePrefix + ", configuration=" + this.getConfiguration() + ")";
    }

    @java.lang.SuppressWarnings("all")
    @lombok.Generated
    public JdbcDataStoreEngineConfig getConfiguration() {
        return this.configuration;
    }
}
