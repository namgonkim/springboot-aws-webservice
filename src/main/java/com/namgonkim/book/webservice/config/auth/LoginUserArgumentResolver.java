package com.namgonkim.book.webservice.config.auth;

import com.namgonkim.book.webservice.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * supportsParameter() 메소드의 판단에 따라 조건이 맞는 경우 메소드가 있다면,
 * HandlerMethodArgumentResolver 구현체 LoginUserArgumentResolver가 지정한 값으로
 * 해당 메소드의 파라미터의 파라미터로 넘긴다
 */
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    // http 세션을 파라미터 어노테이션으로 관리하기 위한 불변 객체
    private final HttpSession httpSession;

    /**
     * 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단한다
     * @param parameter: 메소드의 파라미터
     * @return 판단한 결과를 true or false 반환
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        // 로그인 어노테이션이 있으면서(and) 해당 파라미터 객체 타입이 유저 세션 객체일 경우
        return isLoginUserAnnotation && isUserClass;
    }

    /**
     * 파라미터에 전달할 객체를 생성한다. -> 세션에서 객체를 가져온다
     * @param parameter 메소드 파라미터
     * @param mavContainer 모델뷰 컨테이너
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return httpSession.getAttribute("user");
    }
}
