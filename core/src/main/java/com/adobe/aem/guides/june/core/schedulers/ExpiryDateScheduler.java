package com.adobe.aem.guides.june.core.schedulers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import org.apache.sling.commons.scheduler.Scheduler;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.search.QueryBuilder;
import com.day.cq.search.Query;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;

@Component(service = Runnable.class)
@Designate(ocd = ExpiryDateSchedulerConfig.class)
public class ExpiryDateScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ExpiryDateScheduler.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private Scheduler scheduler;

    private String schedulerId;

    @Override
    public void run() {

        LOG.info("ExpiryDate Scheduler triggered");

        Map<String, Object> authParams = new HashMap<>();
        authParams.put(ResourceResolverFactory.SUBSERVICE, "workflow-service");

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authParams)) {

            Session session = resolver.adaptTo(Session.class);

            /* ================= QUERY BUILDER ================= */

            Map<String, String> params = new HashMap<>();
            params.put("path", "/content"); // 🔒 VERY IMPORTANT
            params.put("type", "cq:PageContent");
            params.put("property", "expiryDate");
            params.put("p.limit", "200"); // 🔒 SAFE LIMIT

            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(params), session);

            SearchResult result = query.getResult();

            Calendar now = Calendar.getInstance();

            for (Hit hit : result.getHits()) {

                Resource content = hit.getResource();
                ValueMap props = content.getValueMap();

                Calendar expiryDate = props.get("expiryDate", Calendar.class);
                if (expiryDate == null) {
                    continue;
                }

                String pagePath = content.getPath().replace("/jcr:content", "");

                if (!expiryDate.after(now)) {
                    // PREVIOUS date → UNPUBLISH
                    LOG.info("Unpublishing page: {}", pagePath);
                    replicator.replicate(session, ReplicationActionType.DEACTIVATE, pagePath);
                } else {
                    // CURRENT/FUTURE → PUBLISH
                    LOG.info("Publishing page: {}", pagePath);
                    replicator.replicate(session, ReplicationActionType.ACTIVATE, pagePath);
                }
            }

        } catch (Exception e) {
            LOG.error("Error in ExpiryDateScheduler", e);
        }
    }
}
