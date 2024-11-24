package net.shoreline.client.api.event.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventListener {
   int priority() default 0;

   boolean receiveCanceled() default true;
}
