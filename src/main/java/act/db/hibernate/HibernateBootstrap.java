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
import act.inject.util.LoadConfig;
import act.util.LogSupport;
import org.hibernate.cfg.AvailableSettings;
import org.osgl.inject.annotation.Configuration;
import org.osgl.util.C;
import org.osgl.util.E;
import osgl.version.Version;
import osgl.version.Versioned;

import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Versioned
public class HibernateBootstrap extends LogSupport {

    public static final Version VERSION = Version.get();

    private EntityManagerFactory factory;

    @LoadConfig("META-INF/persistence.xml")
    private URL url1;

    @LoadConfig("persistence.xml")
    private URL url2;

    @Configuration("hibernate.unit_name")
    private String persistenceUnitName = "default";

    public EntityManager entityManager() {
        return sessionFactory().createEntityManager();
    }

    private synchronized EntityManagerFactory sessionFactory() {
        if (null == factory) {
            factory = createSessionFactory();
        }
        return factory;
    }

    private EntityManagerFactory createSessionFactory() {
        throw E.unsupport("Hibernate 5.1 not supported");
//        E.invalidConfigurationIf(null == url1 && null == url2, "persistence.xml not found");
//        URL url = null != url1 ? url1 : url2;
//        Map<String, ParsedPersistenceXmlDescriptor> map = PersistenceXmlParser.parse(url, PersistenceUnitTransactionType.JTA);
//        ParsedPersistenceXmlDescriptor descriptor = map.get(persistenceUnitName);
//        E.invalidConfigurationIf(null == descriptor, "cannot locate persistent configuration for " + persistenceUnitName);
//        EntityManagerFactoryBuilder builder = Bootstrap.getEntityManagerFactoryBuilder(descriptor, integration());
//        return builder.build();
    }

    private Map integration() {
        List<?> classLoaders = C.list(Act.app().classLoader());
        return C.Map(AvailableSettings.CLASSLOADERS, classLoaders);
    }

}
