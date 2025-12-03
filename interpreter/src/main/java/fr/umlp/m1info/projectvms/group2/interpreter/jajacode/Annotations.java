package fr.umlp.m1info.projectvms.group2.interpreter.jajacode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface JajaCodeInterpretFunction {
	Class<?> value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface JajaCodeEvalFunction {
	Class<?> value();
}
