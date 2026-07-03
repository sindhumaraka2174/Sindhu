
package com.adobe.aem.guides.june.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PropertiesModel {

    @ChildResource(name = "multifield")
    private List<Resource> multifieldItems;

    private List<MultiFieldItem> items = new ArrayList<>();

    @PostConstruct
    protected void init() {

        if (multifieldItems != null) {
            for (Resource resource : multifieldItems) {

                String text = resource.getValueMap().get("text", String.class);
                String path = resource.getValueMap().get("path", String.class);

                // ------ Nested Multifield ------
                List<NestedItem> nestedList = new ArrayList<>();
                Resource nestedParent = resource.getChild("nestedmultifield");

                if (nestedParent != null) {
                    for (Resource nestedRes : nestedParent.getChildren()) {
                        String nestedText = nestedRes.getValueMap().get("textfield", String.class);
                        nestedList.add(new NestedItem(nestedText));
                    }
                }

                items.add(new MultiFieldItem(text, path, nestedList));
            }
        }
    }

    public List<MultiFieldItem> getItems() {
        return items;
    }

    // ---------------- Outer Multifield Item ----------------
    public static class MultiFieldItem {
        private String text;
        private String path;
        private List<NestedItem> nestedList;

        public MultiFieldItem(String text, String path, List<NestedItem> nestedList) {
            this.text = text;
            this.path = path;
            this.nestedList = nestedList;
        }

        public String getText() {
            return text;
        }

        public String getPath() {
            return path;
        }

        public List<NestedItem> getNestedList() {
            return nestedList;
        }
    }

    // ---------------- Nested Multifield Item ----------------
    public static class NestedItem {
        private String nestedText;

        public NestedItem(String nestedText) {
            this.nestedText = nestedText;
        }

        public String getNestedText() {
            return nestedText;
        }
    }
}
