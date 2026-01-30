package com.example.ticket.infrastructure.batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * バッチ設定
 */
@Configuration
@EnableScheduling
@EnableAsync
public class BatchConfiguration {
}
