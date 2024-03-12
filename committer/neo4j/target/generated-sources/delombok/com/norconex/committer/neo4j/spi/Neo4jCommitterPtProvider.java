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
package com.norconex.committer.neo4j.spi;

import static org.apache.commons.collections4.MultiMapUtils.newListValuedHashMap;

import org.apache.commons.collections4.MultiValuedMap;

import com.norconex.committer.core.Committer;
import com.norconex.committer.neo4j.Neo4jCommitter;
import com.norconex.commons.lang.bean.BeanMapper;
import com.norconex.commons.lang.bean.spi.PolymorphicTypeProvider;

/**
 * <p>
 * For auto registering in {@link BeanMapper}.
 * </p>
 */
public class Neo4jCommitterPtProvider
        implements PolymorphicTypeProvider {

    @Override
    public MultiValuedMap<Class<?>, Class<?>> getPolymorphicTypes() {
        MultiValuedMap<Class<?>, Class<?>> map = newListValuedHashMap();
        map.put(Committer.class, Neo4jCommitter.class);
        return map;
    }
}
