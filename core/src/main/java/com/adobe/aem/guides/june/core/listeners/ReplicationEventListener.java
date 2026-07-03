package com.adobe.aem.guides.june.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.event.EventConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;

@Component(service = EventHandler.class, immediate = true, property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
})
public class ReplicationEventListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReplicationEventListener.class);

    @Override
    public void handleEvent(Event event) {

        ReplicationAction action = ReplicationAction.fromEvent(event);

        if (action == null) {
            return;
        }

        String path = action.getPath();

        if (ReplicationActionType.ACTIVATE.equals(action.getType())) {
            LOG.info("Page Published: {}", path);
        } else if (ReplicationActionType.DEACTIVATE.equals(action.getType())) {
            LOG.info("Page Unpublished: {}", path);
        }
    }
}
