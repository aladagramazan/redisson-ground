package com.rem.redisson.test;

import com.rem.redisson.test.config.RedissonConfig;
import com.rem.redisson.test.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class Lec08LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentsMap;


    @BeforeAll
    public void setUpClient() {
        RedissonConfig config = new RedissonConfig();
        RedissonClient redissonClient = config.getRedissonClient();

        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.NONE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);

        this.studentsMap = redissonClient.getLocalCachedMap(
                "students",
                new TypedJsonJacksonCodec(Integer.class, Student.class),
                mapOptions);
    }

    @Test
    public void appServer1() {
        Student student1 = Student.builder()
                .name("rem")
                .age(30)
                .city("New York")
                .marks(List.of(90, 95, 100))
                .build();

        Student student2 = Student.builder()
                .name("sam")
                .age(40)
                .city("Los Angeles")
                .marks(List.of(80, 85, 90))
                .build();

        this.studentsMap.put(1, student1);
        this.studentsMap.put(2, student2);

        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println(i + "==>" + this.studentsMap.get(1)))
                .subscribe();

        sleep(600000);
    }

    @Test
    public void appServer2() {
        Student student1 = Student.builder()
                .name("rem-updated")
                .age(30)
                .city("New York")
                .marks(List.of(90, 95, 100))
                .build();
        this.studentsMap.put(1, student1);
    }

    // LocalCachedMapOptions.SyncStrategy.UPDATE --> when run second test, the first test will be updated (rem-updated)
    // LocalCachedMapOptions.ReconnectionStrategy.NONE --> when server is up, the cache will not be updated, go on with the old data
    // LocalCachedMapOptions.SyncStrategy.NONE --> when run second test, the first test will not be updated (rem),set rem-updated on redis server
    // LocalCachedMapOptions.ReconnectionStrategy.CLEAR --> when server is up, the cache will be updated, go on with the new data

}
