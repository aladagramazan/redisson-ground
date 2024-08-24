package com.rem.redisson.test;

import com.rem.redisson.test.dto.GeoLocation;
import com.rem.redisson.test.dto.Restaurant;
import com.rem.redisson.test.util.RestaurantUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec17GeoSpatialTest extends BaseTest {

    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @BeforeAll
    public void setGo() {
       this.geo = this.client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        this.map = this.client.getMap("us:texas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    @Test
    public void add(){
        Mono<Void> mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void search() {
        Mono<Void> mono = this.map.get("75205")
                .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude()).radius(5, GeoUnit.MILES))
                .flatMap(r -> this.geo.search(r))
                .flatMapIterable(Function.identity())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
