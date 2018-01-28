package act.db.hibernate;

/*-
 * #%L
 * ACT Hibernate
 * %%
 * Copyright (C) 2015 - 2018 ActFramework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import act.Act;
import org.osgl.inject.Module;

import javax.inject.Provider;
import javax.persistence.EntityManager;

public class HibernateModule extends Module {
    @Override
    protected void configure() {
        bind(EntityManager.class).to(new Provider<EntityManager>() {
            @Override
            public EntityManager get() {
                return Act.getInstance(HibernateBootstrap.class).entityManager();
            }
        });
    }
}
