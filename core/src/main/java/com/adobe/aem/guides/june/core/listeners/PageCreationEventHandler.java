package com.adobe.aem.guides.june.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.model.WorkflowModel;

@Component(service = EventHandler.class, immediate = true, property = {
        "event.topics=org/apache/sling/api/resource/Resource/ADDED"
})
public class PageCreationEventHandler implements EventHandler {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void handleEvent(Event event) {

        String path = (String) event.getProperty("path");

        if (path == null) {
            return;
        }

        String dateType = null;

        if (path.startsWith("/content/June")) {
            dateType = "CURRENT";
        } else if (path.startsWith("/content/we-retail")) {
            dateType = "PREVIOUS";
        } else {
            return; // ignore other paths
        }

        try {
            Map<String, Object> params = new HashMap<>();
            params.put(ResourceResolverFactory.SUBSERVICE, "workflow-service");

            ResourceResolver resolver = resolverFactory.getServiceResourceResolver(params);

            Resource resource = resolver.getResource(path);

            if (resource != null &&
                    resource.isResourceType("cq:Page")) {

                Session session = resolver.adaptTo(Session.class);
                WorkflowSession wfSession = resolver.adaptTo(WorkflowSession.class);
                WorkflowModel model = wfSession.getModel("/var/workflow/models/add-expiry-date");

                Map<String, Object> metaData = new HashMap<>();
                metaData.put("dateType", dateType);
                com.adobe.granite.workflow.exec.WorkflowData workflowData = wfSession.newWorkflowData("JCR_PATH", path);

                workflowData.getMetaDataMap().putAll(metaData);

                wfSession.startWorkflow(model, workflowData);

            }

            resolver.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
