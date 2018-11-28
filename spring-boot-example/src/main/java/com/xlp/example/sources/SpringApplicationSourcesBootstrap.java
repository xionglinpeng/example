package com.xlp.example.sources;

import com.xlp.example.banner.SpringBootBuddhaBanner;
import org.springframework.boot.Banner;
import org.springframework.boot.ImageBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileUrlResource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class SpringApplicationSourcesBootstrap {

    public static void main(String[] args) throws MalformedURLException {
        SpringApplication springApplication = new SpringApplication();
        Set<String> sources = new HashSet<>();
        sources.add(SpringApplicationConfiguration.class.getName());
        springApplication.setSources(sources);
        springApplication.setBanner(new SpringBootBuddhaBanner());
        springApplication.run(args);
    }

    @SpringBootApplication
    public static class SpringApplicationConfiguration{}
}
