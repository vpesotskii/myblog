package com.vpesotskii.configuration;

import com.vpesotskii.repository.CommentRepository;
import com.vpesotskii.repository.JdbcCommentRepository;
import com.vpesotskii.repository.JdbcPostRepository;
import com.vpesotskii.repository.PostRepository;
import com.vpesotskii.service.CommentService;
import com.vpesotskii.service.PostService;
import org.h2.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource(
            // Настройки соединения возьмём из Environment
            @Value("${myblog.datasource.url}") String url,
            @Value("${myblog.datasource.username}") String username,
            @Value("${myblog.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    @EventListener
    public void populate(ContextRefreshedEvent event) {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);
    }

    @Bean
    public PostRepository postRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcPostRepository(jdbcTemplate);
    }


    @Bean
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCommentRepository(jdbcTemplate);
    }

    @Bean
    public PostService postService(PostRepository postRepository, CommentRepository commentRepository) {
        return new PostService(postRepository, commentRepository);
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository) {
        return new CommentService(commentRepository);
    }
}
