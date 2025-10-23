package fr.umlp.m1info.projectvms.group2.interpreter.minijaja;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface InterpretFunction {
	Class<?> value(); // The AST node type this method handles
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface EvalFunction {
	Class<?> value(); // The AST node type this method evaluates
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface FreeFunction {
	Class<?> value(); // The AST node type this method frees
}
