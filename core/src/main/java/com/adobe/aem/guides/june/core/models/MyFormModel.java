package com.adobe.aem.guides.june.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MyFormModel {

    @ValueMapValue
    private String textfield;

    @ValueMapValue
    private String url;

    @ValueMapValue
    private String path;

    @ValueMapValue
    private String date;

    @ValueMapValue
    private boolean checkbox;

    @ValueMapValue
    private String buttonlabel;

    @Inject
    private Resource multifield; // <-- new multifield resource

    private List<Map<String, Object>> multiList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        // Read multifield items if available
        if (multifield != null) {
            for (Resource child : multifield.getChildren()) {
                Map<String, Object> item = new HashMap<>();
                item.put("caption", child.getValueMap().get("caption", String.class));
                item.put("image", child.getValueMap().get("image", String.class));
                multiList.add(item);
                System.out.println("✅ Captured item: " + item); // Debug line

            }
        } else {
            System.out.println("⚠️ Multifield is NULL");
        }
    }

    public String getTextfield() {
        return textfield;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getDate() {
        return date;
    }

    public boolean getCheckbox() {
        return checkbox;
    }

    public String getButtonlabel() {
        return buttonlabel;
    }

    public List<Map<String, Object>> getMultiList() {
        return multiList;
    }

}
