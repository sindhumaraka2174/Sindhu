package com.adobe.aem.guides.june.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ExporterConstants;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        TextCheckbox.class }, resourceType = TextCheckboxImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class TextCheckboxImpl implements TextCheckbox {

    @ValueMapValue
    private String text;

    @ValueMapValue
    private boolean checked;

    static final String RESOURCE_TYPE = "June/components/reactcheck";

    @Override
    public String getText() {
        return text;

    }

    @Override
    public boolean getIsChecked() {
        return checked;
    }

    @Override
    public String getExportedType() {
        return TextCheckboxImpl.RESOURCE_TYPE;
    }

}
