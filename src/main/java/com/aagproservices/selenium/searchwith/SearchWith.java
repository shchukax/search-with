package com.aagproservices.selenium.searchwith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface SearchWith {
    String page() default "";
    String locatorsFile() default "";
    String name() default "";
}
