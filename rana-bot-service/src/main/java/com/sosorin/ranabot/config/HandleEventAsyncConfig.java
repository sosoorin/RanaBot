package com.sosorin.ranabot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rana-bot
 * @since 2025/6/27  12:45
 */
@Configuration
public class HandleEventAsyncConfig {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    @Bean("handleEventAsync")
    public Executor getAsyncExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(null, r, "handleEventAsync-" + POOL_NUMBER.getAndIncrement(), 0);
            }
        };
        return new ThreadPoolExecutor(
                2,
                4,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100),
                threadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }
}
