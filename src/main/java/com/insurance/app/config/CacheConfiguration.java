package com.insurance.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.insurance.app.domain.Contract.class.getName());
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".cars");
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".drivers");
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".insuranceOffers");
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".documents");
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".accidentHistories");
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".parkings");
            createCache(cm, com.insurance.app.domain.Contract.class.getName() + ".burntStolenIncidents");
            createCache(cm, com.insurance.app.domain.Car.class.getName());
            createCache(cm, com.insurance.app.domain.Car.class.getName() + ".vehicleUsages");
            createCache(cm, com.insurance.app.domain.Car.class.getName() + ".vehicleAccessories");
            createCache(cm, com.insurance.app.domain.Car.class.getName() + ".blacklistedCars");
            createCache(cm, com.insurance.app.domain.Driver.class.getName());
            createCache(cm, com.insurance.app.domain.Driver.class.getName() + ".trafficViolations");
            createCache(cm, com.insurance.app.domain.TrafficViolation.class.getName());
            createCache(cm, com.insurance.app.domain.LocationRisk.class.getName());
            createCache(cm, com.insurance.app.domain.LocationRisk.class.getName() + ".cars");
            createCache(cm, com.insurance.app.domain.VehicleUsage.class.getName());
            createCache(cm, com.insurance.app.domain.Document.class.getName());
            createCache(cm, com.insurance.app.domain.InsuranceOffer.class.getName());
            createCache(cm, com.insurance.app.domain.AccidentHistory.class.getName());
            createCache(cm, com.insurance.app.domain.BlacklistedCar.class.getName());
            createCache(cm, com.insurance.app.domain.VehicleAccessory.class.getName());
            createCache(cm, com.insurance.app.domain.Parking.class.getName());
            createCache(cm, com.insurance.app.domain.BurntStolenIncident.class.getName());
            createCache(cm, com.insurance.app.domain.DocumentSinister.class.getName());
            createCache(cm, com.insurance.app.domain.DocumentSinister.class.getName() + ".accidentHistories");
            createCache(cm, com.insurance.app.domain.Client.class.getName());
            createCache(cm, com.insurance.app.domain.Client.class.getName() + ".contracts");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
