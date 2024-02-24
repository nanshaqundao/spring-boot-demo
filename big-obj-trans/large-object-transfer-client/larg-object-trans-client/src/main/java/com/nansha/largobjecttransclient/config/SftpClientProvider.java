package com.nansha.largobjecttransclient.config;

import com.nansha.largobjecttransclient.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SftpClientProvider {
    @Bean
    public SftpClient sftpClient() {
        return new SftpClient("sftpClient");
    }
}
