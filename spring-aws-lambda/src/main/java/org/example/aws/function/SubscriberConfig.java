package org.example.aws.function;

import org.example.aws.model.Subscriber;
import org.example.aws.service.SubscriberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class SubscriberConfig {

    private final SubscriberService subscriberService;

    public SubscriberConfig(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Bean
    public Supplier<List<Subscriber>> findAll() {
        return () -> subscriberService.findAll();
    }

    @Bean
    public Consumer<String> create() {
        return (email) -> subscriberService.create(email);
    }
}
