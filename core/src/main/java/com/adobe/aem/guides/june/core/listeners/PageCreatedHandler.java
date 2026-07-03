package com.adobe.aem.guides.june.core.listeners;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.model.WorkflowModel;

import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Component(service = JobConsumer.class, property = {
        JobConsumer.PROPERTY_TOPICS + "=com/mycompany/pagecreated"
})
public class PageCreatedHandler implements JobConsumer {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public JobResult process(Job job) {

        String pagePath = job.getProperty("pagePath", String.class);

        if (pagePath == null) {
            return JobResult.CANCEL;
        }

        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "expiryService"))) {

            // 1. Get jcr:content
            Resource content = resolver.getResource(pagePath + "/jcr:content");
            if (content == null) {
                return JobResult.CANCEL;
            }

            ModifiableValueMap mvm = content.adaptTo(ModifiableValueMap.class);
            String template = mvm.get("cq:template", String.class);

            if (template == null) {
                return JobResult.OK;
            }

            // 2. Determine expiry date
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime previous = now.minusDays(1);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            boolean isMyProjectTemplate = template.startsWith("/conf/myproject");

            if (isMyProjectTemplate) {
                mvm.put("expiryDate", fmt.format(now));
            } else {
                mvm.put("expiryDate", fmt.format(previous));
            }

            resolver.commit();

            // 3. Start workflow
            WorkflowSession wfSession = resolver.adaptTo(WorkflowSession.class);
            WorkflowModel model = wfSession.getModel("/var/workflow/models/expiry-workflow");

            WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", pagePath);
            wfSession.startWorkflow(model, wfData);

        } catch (Exception e) {
            e.printStackTrace();
            return JobResult.FAILED;
        }

        return JobResult.OK;
    }
}
