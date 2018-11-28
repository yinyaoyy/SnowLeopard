package com.thinkgem.jeesite.common.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Length;

@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}")
@Null
@Length(min = 0, max = 0)
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface Phone {

	String message() default "手机号校验错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
	
}
