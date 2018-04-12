package com.generater.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import com.generater.ErrorPOJO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Configuration
public class RequestParamValidAspect {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final ExecutableValidator methodValidator = factory.getValidator().forExecutables();
	private final Validator beanValidator = factory.getValidator();

	private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
		return methodValidator.validateParameters(obj, method, params);
	}

	private <T> Set<ConstraintViolation<T>> validBeanParams(T bean) {
		return beanValidator.validate(bean);
	}

	@Pointcut("execution(* com.generater.controller.*.*(..))")
	public void soaServiceBefore() {
	}

	@Around("soaServiceBefore()")
	public Object twiceAsOld1(ProceedingJoinPoint point) throws Throwable {
		Object target = point.getThis();
		Object[] args = point.getArgs();
		Method method = ((MethodSignature) point.getSignature()).getMethod();

		Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);

		Iterator<ConstraintViolation<Object>> violationIterator = validResult.iterator();

		ErrorPOJO result = null;
		List<ErrorPOJO> errorList = new ArrayList<>();
		while (violationIterator.hasNext()) {
			String message = violationIterator.next().getMessage();
			if (null == result) {
				result = new ErrorPOJO();
				result.setError_info(message);
			}
			ErrorPOJO currentError = new ErrorPOJO();
			currentError.setError_info(message);
			// 此处可以抛个异常提示用户参数输入格式不正确
			log.error("method check---------" + message);
		}

		// 校验以java bean对象 为方法参数的
		for (Object bean : args) {
			if (null != bean) {
				Set<ConstraintViolation<Object>> beanValidResult = validBeanParams(bean);
				Iterator<ConstraintViolation<Object>> beanValidIterator = beanValidResult.iterator();
				while (beanValidIterator.hasNext()) {
					final ConstraintViolation<Object> next = beanValidIterator.next();
					String message = next.getMessage();
					Path key = next.getPropertyPath();
					if (null == result) {
						result = new ErrorPOJO();
						result.setError_info(message);
						result.setError_key(key.toString());
					}
					ErrorPOJO currentError = new ErrorPOJO();
					currentError.setError_info(message);
					currentError.setError_key(key.toString());
					errorList.add(currentError);
					log.error("bean check-------" + key + message);
				}
			}
		}
		if (null != result) {

			Object object = Class.forName(method.getReturnType().getName()).newInstance();

			if (object instanceof ErrorPOJO) {
				((ErrorPOJO) object).setError_info(result.getError_info());
				((ErrorPOJO) object).setError_key(result.getError_key());
				((ErrorPOJO) object).setError_list(errorList);
			}

			return object;

		}
		return point.proceed();

	}
}
