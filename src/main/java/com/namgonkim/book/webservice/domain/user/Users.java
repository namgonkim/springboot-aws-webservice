package com.namgonkim.book.webservice.domain.user;

import com.namgonkim.book.webservice.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    // Enumerated : JPA를 이용해 데이터베이스로 저장할 때 Enum 값을 어떤 타입(String)으로 저장할지 결정
    // 기본적으로는 int 형으로 된 숫자가 저장되지만, 숫자로 저장되면 DB에서 확인할 때 이 값이 무슨 코드를 의미하는지 모르기에 String으로 처리함.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Users(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public Users update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
