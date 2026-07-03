package com.adobe.aem.guides.june.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Page Publish Scheduler Configuration", description = "Scheduler configuration for publishing pages at a specific time")
public @interface PublishSchedulerConfig {

    @AttributeDefinition(name = "Cron Expression", description = "Enter cron expression for scheduler (e.g. */30 * * * * ?)")
    String cronExpression() default "0 0/1 * * * ?";

    @AttributeDefinition(name = "Page Path", description = "Root path from which pages will be published")
    String pagePath() default "/content";
}
