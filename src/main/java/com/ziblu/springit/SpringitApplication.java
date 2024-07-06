package com.ziblu.springit;

import com.ziblu.springit.domain.Comment;
import com.ziblu.springit.domain.Link;
import com.ziblu.springit.repository.CommentRepository;
import com.ziblu.springit.repository.LinkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringitApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(LinkRepository linkRepository, CommentRepository commentRepository){
		return args -> {
			Link link = new Link("Getting started with Spring Boot 3", "https://therealdanvega.com/spring-boot-2");
			linkRepository.save(link);

			Comment comment = new Comment("This is link is awesome!", link);
			commentRepository.save(comment);
			link.addCommnet(comment);

		};
	}


}
