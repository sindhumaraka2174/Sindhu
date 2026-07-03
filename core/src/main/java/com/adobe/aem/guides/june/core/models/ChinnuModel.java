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
public class ChinnuModel {

    @ValueMapValue
    private String textfield;

    @Inject
    private Resource multifield;

    private List<Map<String, Object>> multiList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (multifield != null) {
            for (Resource fieldRes : multifield.getChildren()) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("name", fieldRes.getValueMap().get("name", String.class));
                fieldMap.put("age", fieldRes.getValueMap().get("age", String.class));

                // Nested multifield
                List<Map<String, Object>> nestedList = new ArrayList<>();
                Resource nestedMulti = fieldRes.getChild("nestedmultifield");
                if (nestedMulti != null) {
                    for (Resource nested : nestedMulti.getChildren()) {
                        Map<String, Object> nestedMap = new HashMap<>();
                        nestedMap.put("qualification", nested.getValueMap().get("qualification", String.class));
                        nestedMap.put("year", nested.getValueMap().get("year", String.class));
                        nestedList.add(nestedMap);
                    }
                }
                fieldMap.put("nestedmultifield", nestedList);
                multiList.add(fieldMap);
            }
        }
    }

    public String getTextfield() {
        return textfield;
    }

    public List<Map<String, Object>> getMultiList() {
        return multiList;
    }
}
