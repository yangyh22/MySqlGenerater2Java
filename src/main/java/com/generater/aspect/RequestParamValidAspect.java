package com.generater.aspect;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

@Aspect // 一个切面
@Configuration // spring boot 配置类
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

	/* * 通过连接点切入 */
	@Around("soaServiceBefore()")
	public Object twiceAsOld1(JoinPoint point) throws Exception {
		// 获得切入目标对象
		Object target = point.getThis();
		// 获得切入方法参数
		Object[] args = point.getArgs();
		// 获得切入的方法
		Method method = ((MethodSignature) point.getSignature()).getMethod();

		// 校验以基本数据类型 为方法参数的
		Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);

		Iterator<ConstraintViolation<Object>> violationIterator = validResult.iterator();
		while (violationIterator.hasNext()) {
			// 此处可以抛个异常提示用户参数输入格式不正确
			System.out.println("method check---------" + violationIterator.next().getMessage());
		}

		// 校验以java bean对象 为方法参数的
		for (Object bean : args) {
			if (null != bean) {
				validResult = validBeanParams(bean);
				violationIterator = validResult.iterator();
				while (violationIterator.hasNext()) {
					System.out.println("bean check-------" + violationIterator.next().getPropertyPath()
							+ violationIterator.next().getMessage());
				}
			}
		}
		return "qq";
	}
}
