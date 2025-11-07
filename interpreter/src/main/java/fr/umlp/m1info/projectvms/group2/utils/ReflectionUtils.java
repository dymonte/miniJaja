package fr.umlp.m1info.projectvms.group2.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Utility class for reflection-based method lookup with annotations.
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	/**
	 * Finds a method in the given class (and its superclasses) annotated with the
	 * specified annotation class whose "value" attribute matches or is assignable
	 * from the given node class.
	 *
	 * @param targetClass     the class to search methods in
	 * @param annotationClass the annotation to look for
	 * @param nodeClass       the node class handled by the annotated method
	 * @return the annotated Method, or null if not found
	 */
	public static Method findAnnotatedMethod(Class<?> targetClass, Class<? extends Annotation> annotationClass,
			Class<?> nodeClass) {
		Class<?> cls = targetClass;
		while (cls != null) {
			for (Method method : cls.getDeclaredMethods()) {
				Annotation annotation = method.getAnnotation(annotationClass);
				if (annotation == null) {
					continue;
				}
				try {
					Class<?> handledType = (Class<?>) annotationClass.getMethod("value").invoke(annotation);
					if (handledType.isAssignableFrom(nodeClass)) {
						return method;
					}
				} catch (Exception ignored) {
					// Ignored: annotation might not have a "value" attribute or reflection failure
				}
			}
			cls = cls.getSuperclass();
		}
		return null;
	}
}
