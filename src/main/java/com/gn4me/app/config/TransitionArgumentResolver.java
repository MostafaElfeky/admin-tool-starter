package com.gn4me.app.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gn4me.app.entities.Transition;
import com.gn4me.app.util.UtilHandler;

public class TransitionArgumentResolver implements HandlerMethodArgumentResolver {

	UtilHandler utilHandler;

	public TransitionArgumentResolver(UtilHandler utilHandler) {
		this.utilHandler = utilHandler;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Transition.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Transition transition = null;
		
		Object reqTransition = webRequest.getAttribute("transition", RequestAttributes.SCOPE_REQUEST);
		
		
		if (reqTransition != null) {
			transition = (Transition) reqTransition;
		} else {
			transition = new Transition(utilHandler.validateLanguage(webRequest.getHeader("lang")));
			webRequest.setAttribute("transition", transition, RequestAttributes.SCOPE_REQUEST);
		}
		

		return transition;
	}

}
