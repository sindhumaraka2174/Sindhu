package com.adobe.aem.guides.june.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class)
public class FormDataModel {

    @Inject
    private ResourceResolver resourceResolver;

    private List<Map<String, String>> entries;

    @PostConstruct
    protected void init() {
        entries = new ArrayList<>();
        Resource parent = resourceResolver.getResource("/content/contactform");
        if (parent != null) {
            for (Resource child : parent.getChildren()) {
                Map<String, String> entry = new HashMap<>();
                entry.put("name", child.getValueMap().get("name", String.class));
                entry.put("email", child.getValueMap().get("email", String.class));
                entry.put("mobile", child.getValueMap().get("mobile", String.class));
                entry.put("path", child.getPath());
                entries.add(entry);
            }
        }
    }

    public List<Map<String, String>> getEntries() {
        return entries;
    }
}
