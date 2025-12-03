package fr.umlp.m1info.projectvms.group2.checker;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface TypeCheckFunction {
	Class<?> value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface TypeEvalFunction {
	Class<?> value();
}
