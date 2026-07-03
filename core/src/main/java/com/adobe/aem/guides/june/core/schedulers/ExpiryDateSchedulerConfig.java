package com.adobe.aem.guides.june.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Expiry Date Scheduler Configuration", description = "Scheduler to publish/unpublish pages based on expiryDate")
public @interface ExpiryDateSchedulerConfig {

    @AttributeDefinition(name = "Cron Expression", description = "Cron expression (example: */3 * * * * ?)")
    String scheduler_expression() default "*/3 * * * * ?";
}
