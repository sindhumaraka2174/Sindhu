package com.adobe.aem.guides.june.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.adobe.cq.export.json.ComponentExporter;
import org.apache.sling.models.annotations.Exporter;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class, adapters = {
        ComponentExporter.class }, resourceType = "June/components/header", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(name = "jackson", extensions = "json")

public class HeaderModel implements ComponentExporter {

    @ValueMapValue
    private String pathfield;

    @ValueMapValue
    private String textfield;

    @ValueMapValue
    private boolean checkbox;

    @ChildResource(name = "multifield")
    private List<Resource> multifieldItems;

    private List<MultiFieldItem> items = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (multifieldItems != null) {
            for (Resource resource : multifieldItems) {
                String text = resource.getValueMap().get("text", String.class);
                String date = resource.getValueMap().get("date", String.class);
                items.add(new MultiFieldItem(text, date));
            }
        }
    }

    public String getPathfield() {
        return pathfield;
    }

    public String getTextfield() {
        return textfield;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public List<MultiFieldItem> getItems() {
        return items;
    }

    // Inner class to hold multifield data
    public static class MultiFieldItem {
        private String text;
        private String date;

        public MultiFieldItem(String text, String date) {
            this.text = text;
            this.date = date;
        }

        public String getText() {
            return text;
        }

        public String getDate() {
            return date;
        }
    }

    @Override
    public String getExportedType() {
        return "June/components/header";
    }

}
