package com.adobe.aem.guides.june.core.listeners;

import org.osgi.service.component.annotations.Component;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Reference;
import java.util.Map;
import java.util.HashMap;

@Component(service = EventHandler.class, immediate = true, property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
})
public class PagePublishEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PagePublishEventHandler.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void handleEvent(Event event) {
        // Check if the event is a replication event
        if (ReplicationAction.fromEvent(event) != null) {
            ReplicationAction action = ReplicationAction.fromEvent(event);

            // Check if the action is ACTIVATE (Publish)
            if (ReplicationActionType.ACTIVATE.equals(action.getType())) {
                String path = action.getPath();
                if (path != null) {
                    LOG.info("Page published: {}", path);
                    try {
                        Map<String, Object> param = new HashMap<>();
                        param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
                        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
                            Resource resource = resolver.getResource(path + "/jcr:content");
                            if (resource != null) {
                                ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
                                if (map != null) {
                                    map.put("changed", true);
                                    resolver.commit();
                                    LOG.info("Property 'changed' set to true for {}", path);
                                }
                            }
                        }
                    } catch (LoginException | PersistenceException e) {
                        LOG.error("Error updating property for page {}", path, e);
                    }
                }
            }
        }
    }
}
