package com.adobe.aem.guides.june.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;

import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Collections;

@Component(service = Runnable.class, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PublishSchedulerConfig.class)
public class PagePublishScheduler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PagePublishScheduler.class);

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    private String cronExpression;
    private String pagePath;

    @Activate
    @Modified
    protected void activate(PublishSchedulerConfig config) {
        this.cronExpression = config.cronExpression();
        this.pagePath = config.pagePath();

        ScheduleOptions options = scheduler.EXPR(cronExpression);
        options.name("Page Publish Scheduler");
        options.canRunConcurrently(false);

        scheduler.schedule(this, options);

        log.info("Page Publish Scheduler Activated → cron: {}, path: {}", cronExpression, pagePath);
    }

    @Override
    public void run() {
        log.info("Scheduler Triggered…");

        try (ResourceResolver resolver = getServiceResolver()) {

            Resource root = resolver.getResource(pagePath);
            if (root == null) {
                log.error("Invalid path provided: {}", pagePath);
                return;
            }

            Session session = resolver.adaptTo(Session.class);

            root.getChildren().forEach(child -> {

                String page = child.getPath();

                try {
                    replicator.replicate(
                            session,
                            ReplicationActionType.ACTIVATE,
                            page);

                    log.info("Published Page Successfully → {}", page);

                } catch (Exception e) {
                    log.error("Failed to publish {} → {}", page, e.getMessage());
                }

            });

        } catch (Exception e) {
            log.error("Scheduler execution failed: {}", e.getMessage());
        }
    }

    private ResourceResolver getServiceResolver() throws org.apache.sling.api.resource.LoginException {
        return resolverFactory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "scheduler"));
    }
}
