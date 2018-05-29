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

import static act.db.jpa.JPAPlugin.*;
import static org.hibernate.jpa.AvailableSettings.LOADED_CLASSES;

import act.Act;
import act.app.App;
import act.db.jpa.JPAService;
import act.db.sql.DataSourceConfig;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.osgl.util.C;
import org.osgl.util.E;
import org.rythmengine.utils.S;

import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

public class HibernateService extends JPAService {

    public HibernateService(String dbId, App app, Map<String, String> config) {
        super(dbId, app, config);
    }

    @Override
    protected Properties processProperties(Properties properties, DataSourceConfig dataSourceConfig, boolean useExternalDataSource) {
        properties.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
        properties.put(LOADED_CLASSES, C.list(entityClasses()));
        properties.put("hibernate.dialect", HibernatePlugin.getDefaultDialect(config.rawConf, config.dataSourceConfig.driver));
        String s = config.rawConf.get(CONF_DDL);
        if (null == s) {
            s = Act.isDev() ? CONF_DDL_UPDATE : CONF_DDL_NONE;
        }
        if (S.ne(CONF_DDL_NONE, s)) {
            properties.setProperty("hibernate.hbm2ddl.auto", s);
        }
        // According to https://stackoverflow.com/questions/4880577
        // Hibernate doesn't come up with a decent connection pool
        // thus we don't bother with translating dataSourceConfig settings
        // into hibernate properties
        return super.processProperties(properties, dataSourceConfig, useExternalDataSource);
    }

    @Override
    protected Class<? extends PersistenceProvider> persistenceProviderClass() {
        return HibernatePersistenceProvider.class;
    }

    @Override
    protected void dataSourceProvided(DataSource dataSource, DataSourceConfig dataSourceConfig, boolean readonly) {
        E.unsupportedIf(null == dataSource, "Hibernate require external data source provider");
        super.dataSourceProvided(dataSource, dataSourceConfig, readonly);
    }

    @Override
    protected EntityManagerFactory createEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo) {
        Map<String, Object> configuration = C.Map();
        return new EntityManagerFactoryBuilderImpl(
                new PersistenceUnitInfoDescriptor(persistenceUnitInfo), configuration
        ).build();
    }
}
