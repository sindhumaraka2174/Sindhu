package com.adobe.aem.guides.june.core.workflows;

import java.util.Date;

import java.util.Calendar;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import org.osgi.service.component.annotations.Component;

@Component(service = WorkflowProcess.class, property = {
        "process.label=Add Expiry Date Process"
})
public class AddExpiryDateProcess implements WorkflowProcess {

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
            throws WorkflowException {

        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        String dateType = args.get("dateType", "CURRENT");

        ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);

        if (resolver != null) {
            Resource pageContent = resolver.getResource(payloadPath + "/jcr:content");

            if (pageContent != null) {
                ModifiableValueMap map = pageContent.adaptTo(ModifiableValueMap.class);
                if (map != null) {

                    // 🔥 VERY IMPORTANT: remove old property if it already exists
                    if (map.containsKey("expiryDate")) {
                        map.remove("expiryDate");
                    }

                    Calendar cal = Calendar.getInstance();

                    if (payloadPath.startsWith("/content/we-retail")) {
                        cal.add(Calendar.DATE, -1);
                    }

                    // Set expiryDate as DATE (not binary)
                    map.put("expiryDate", cal);

                    try {
                        resolver.commit();
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
