package com.rem.redisson.test;

import com.rem.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class Lec02KeyValueObjectTest extends BaseTest {



    @Test
    public void keyValueObjectTest() {
        Student student = Student.builder()
                .name("rem")
                .age(30)
                .city("New York")
                .marks(List.of(90, 95, 100))
                .build();

        // JsonJacksonCodec.INSTANCE) is used to serialize and deserialize the value of the bucket.

        RBucketReactive<Student> bucket = this.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

}
