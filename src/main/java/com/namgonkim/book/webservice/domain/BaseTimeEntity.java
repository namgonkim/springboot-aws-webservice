package com.namgonkim.book.webservice.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
/*
@MappedSuperclass :
JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들(createdDate, modifiedDate)도 컬럼으로 인식하도록 한다.

@EntityListeners(AuditingEntityListener.class) :
BaseTimeEntity 클래스에 Auditing 기능을 포함시킨다.

@CreatedDate :
엔티티가 생성되어 저장될 때 시간이 자동 저장된다.

@LastModifiedDate :
조회한 엔터티의 값을 변경할 때 시간이 자동 저장된다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
