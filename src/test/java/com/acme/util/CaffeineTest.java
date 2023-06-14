package com.acme.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.FIVE_SECONDS;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import lombok.Value;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kiwiproject.base.UUIDs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class CaffeineTest {

    private static Cache<String, DataObject> cache;

    @BeforeAll
    static void beforeAll() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    @Test
    void shouldNotFindWhenNotInCache() {
        var key = "A";
        var dataObject = cache.getIfPresent(key);

        assertThat(dataObject).isNull();;
    }

    @Test
    void canAddToCache() {
        var key = UUIDs.randomUUIDString();
        var dataObject = DataObject.newInstance("some data");
        cache.put(key, dataObject);

        var cachedDataObject = cache.getIfPresent(key);
        assertThat(cachedDataObject).isSameAs(dataObject);
    }

    @Test
    void canAddFallbackValuesToCache() {
        var key = UUIDs.randomUUIDString();
        var dataObject = cache.get(key, k -> DataObject.newInstance("Data for key " + key));

        assertThat(dataObject).isNotNull();
        assertThat(dataObject.getData()).isEqualTo("Data for key " + key);
    }

    @Test
    void canInvalidateCachedValues() {
        var key = UUIDs.randomUUIDString();
        var dataObject = DataObject.newInstance("some data");
        cache.put(key, dataObject);

        cache.invalidate(key);

        assertThat(cache.getIfPresent(key)).isNull();
    }

    @Test
    void canLoadSynchronously() {
        LoadingCache<String, DataObject> loadingCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(k -> DataObject.newInstance("Data for " + k));

        var key = UUIDs.randomUUIDString();
        var dataObject = loadingCache.get(key);
        assertThat(dataObject).isNotNull();
        assertThat(dataObject.getData()).isEqualTo("Data for " + key);

        Map<String, DataObject> dataObjectMap = loadingCache.getAll(List.of("A", "B", "C"));
        assertThat(dataObjectMap).hasSize(3).containsOnlyKeys("A", "B", "C");
    }

    @Test
    void canLoadAsynchronously() {
        AsyncLoadingCache<String, DataObject> asyncLoadingCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(k -> DataObject.newInstance("Data for " + k));

        var key = "A";

        asyncLoadingCache.get(key).thenAccept(dataObject -> {
            assertThat(dataObject).isNotNull();
            assertThat(dataObject.getData()).isEqualTo("Data for " + key);
        });

        asyncLoadingCache.getAll(List.of("D", "E", "F"))
                .thenAccept(dataObjectMap -> assertThat(dataObjectMap).hasSize(3));
    }

    @Test
    void canEvictBasedOnSize() {
        LoadingCache<String, DataObject> loadingCache = Caffeine.newBuilder()
                .maximumSize(1)
                .build(k -> DataObject.newInstance("Data for " + k));

        assertThat(loadingCache.estimatedSize()).isZero();

        loadingCache.get("Z");

        assertThat(loadingCache.estimatedSize()).isOne();

        loadingCache.get("B");

        // cache eviction is async (unless force using cleanUp)
        await().atMost(FIVE_SECONDS).untilAsserted(() -> assertThat(loadingCache.estimatedSize()).isOne());
    }

    @Value
    public static class DataObject {
        String data;

        public static DataObject newInstance(String data) {
            return new DataObject(data);
        }
    }
}
