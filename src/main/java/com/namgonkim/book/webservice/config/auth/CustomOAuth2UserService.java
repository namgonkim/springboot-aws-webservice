package com.namgonkim.book.webservice.config.auth;

import com.namgonkim.book.webservice.config.auth.dto.OAuthAttributes;
import com.namgonkim.book.webservice.config.auth.dto.SessionUser;
import com.namgonkim.book.webservice.domain.user.Users;
import com.namgonkim.book.webservice.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // 1. 로그인 진행 중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 2. OAuth2 로그인 진행 시 키가 되는 필드값(일종의 Primary key). 구글의 경우 기본 코드는 'sub'
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        // 3. OAuthAttributes(OAuth 별 유저 정보를 담고 있는 일종의 Dto) - registrationId에 따라 어떤 OAuth 인지(구글, 네이버 등등)에 대한 유저 정보를 받는다.
        // OAuth2User에서 반환하는 사용자 정보는 Map 기반이기 때문에 값 하나하나를 변환해야 한다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        // 4. OAuth를 통해 로그인한 유저에 대한 저장 혹은 업데이트(OAuth 유저 정보가 변경되었을 경우)
        Users users = saveOrUpdate(attributes);
        // 5. 세션에 사용자 정보를 저장하기 위한 Dto 클래스인 SessionUser로 유저 정보를 담아 http session에 저장한다.
        // OAuthAttributes 객체를 통해 User 객체 생성이 끝났으면 같은 패키지에 SessionUser 클래스를 생성한다.
        httpSession.setAttribute("user", new SessionUser(users));
        // 6. DefaultOAuth2User(OAuth2 로그인을 통해 인증된 사용자에 대한 기본 구현체) 객체에 사용자의 권한(SimpleGrantedAuthority(user.getRoleKey())과 유저 정보(OAuthAttributes)를 담아 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(users.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 구글 사용자 정보가 업데이트 되었을 때를 대비해 update 기능도 같이 구현되었음.
    private Users saveOrUpdate(OAuthAttributes attributes) {
        Users users = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(users);
    }
}
