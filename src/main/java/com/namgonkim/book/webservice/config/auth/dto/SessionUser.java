package com.namgonkim.book.webservice.config.auth.dto;

import com.namgonkim.book.webservice.domain.user.Users;
import lombok.Getter;

import java.io.Serializable;

/**
 * SessionUser: 세션에 유저를 저장하기 위해 User 정보와 함께 Serializable을 구현한 구현체
 * @Note : 세션에 저장하기 위해서는 직렬화가 필요한데, User 클래스는 엔티티기 때문에 User 클래스에 Serializable을 구현하는 것은 고려해야할 사항이 있다.
 * 엔티티 클래스는 언제 다른 엔티티와 관계가 형성될 지 모르는데 만약 직렬화를 해버리면 자식 엔티티까지 영향을 끼칠 수 있어 성능 이슈나 사이드 이펙이 발생할 수 있다.
 * 따라서 직렬화 기능을 가진 세션 dto를 추가로 만드는 것이 좋다.
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(Users users) {
        this.name = users.getName();
        this.email = users.getEmail();
        this.picture = users.getPicture();
    }
}
