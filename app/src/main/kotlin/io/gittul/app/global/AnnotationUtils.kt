package io.gittul.app.global

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.MethodSignature

object AopAnnotationUtils {

    /**
     * 메서드에서 지정된 어노테이션을 가져온다.
     */
    fun <T : Annotation> getMethodAnnotation(joinPoint: JoinPoint, annotationClass: Class<T>): T? {
        return runCatching {
            val method = (joinPoint.signature as? MethodSignature)?.method
            method?.getAnnotation(annotationClass)
        }.getOrNull()
    }

    /**
     * 클래스에서 지정된 어노테이션을 가져온다.
     */
    fun <T : Annotation> getClassAnnotation(joinPoint: JoinPoint, annotationClass: Class<T>): T? {
        return joinPoint.target::class.java.getAnnotation(annotationClass)
    }

    /**
     * 메서드 → 클래스 순서로 어노테이션을 가져온다 (우선순위: 메서드).
     */
    fun <T : Annotation> getAnnotation(joinPoint: JoinPoint, annotationClass: Class<T>): T? {
        return getMethodAnnotation(joinPoint, annotationClass)
            ?: getClassAnnotation(joinPoint, annotationClass)
    }
}
