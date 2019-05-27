package com.hse.myjunit.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    class NoException extends Throwable {}

    @NotNull String ignore() default "";
    Class<? extends Throwable> expected() default NoException.class;
}
