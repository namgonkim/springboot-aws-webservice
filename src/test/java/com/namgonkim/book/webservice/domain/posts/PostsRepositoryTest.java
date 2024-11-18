package com.namgonkim.book.webservice.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(
                Posts.builder()
                    .title(title)
                    .content(content)
                    .author("zxcv9455@naver.com")
                    .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록() {

        // given

        // 시간을 제외한 현재 날짜 받기
        // LocalDate onlyDate = LocalDate.now();

        // 시간 직접 입력
        //LocalDateTime now = LocalDateTime.of(2021,7,23,0,0,0);

        // 현재 시간 입력
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        System.out.println(">>> createdDate=" + posts.getCreatedDate()+", modifiedDate=" + posts.getModifiedDate());
        posts.update("modified-title", "modified-content");
        System.out.println(">>> createdDate=" + posts.getCreatedDate()+", modifiedDate=" + posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
