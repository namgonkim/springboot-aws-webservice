package com.namgonkim.book.webservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 인터페이스 생성 후, JpaRepo<Entity클래스, PK타입> 상속 -> 기본적인 CRUD 메소드가 자동으로 생성
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
