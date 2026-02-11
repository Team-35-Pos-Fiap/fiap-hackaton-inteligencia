package br.com.fiap.inteligencia_service.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class FeignConfig {

    @Bean
    @ConditionalOnMissingBean
    public FeignHttpMessageConverters feignHttpMessageConverters(
        ObjectProvider<HttpMessageConverter<?>> messageConverters,
        ObjectProvider<HttpMessageConverterCustomizer> customizers) {

        var converters = new FeignHttpMessageConverters(messageConverters, customizers);

        converters.getConverters();

        return converters;
    }
}
