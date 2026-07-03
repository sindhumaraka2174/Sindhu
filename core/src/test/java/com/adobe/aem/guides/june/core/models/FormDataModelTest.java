package com.adobe.aem.guides.june.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

@ExtendWith(AemContextExtension.class)
class FormDataModelTest {

    // Fake AEM environment
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        // Create parent path
        context.create().resource("/content/contactform");

        // Create child nodes with properties
        context.create().resource("/content/contactform/item1",
                "name", "Sindhu",
                "email", "sindhu@test.com",
                "mobile", "9999999999");

        context.create().resource("/content/contactform/item2",
                "name", "June",
                "email", "june@test.com",
                "mobile", "8888888888");

        // Register Sling Model
        context.addModelsForClasses(FormDataModel.class);
    }

    @Test
    void testFormDataModelEntries() {

        // Adapt model from resource
        Resource resource = context.resourceResolver().getResource("/content/contactform/item1");
        FormDataModel model = resource.adaptTo(FormDataModel.class);

        assertNotNull(model);

        List<Map<String, String>> entries = model.getEntries();

        System.out.println("Form entries: " + entries);

        assertEquals(2, entries.size());

        assertEquals("Sindhu", entries.get(0).get("name"));
        assertEquals("june@test.com", entries.get(1).get("email"));
    }
}
