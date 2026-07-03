package com.adobe.aem.guides.june.core.models;

import com.adobe.cq.export.json.ComponentExporter;;

public interface TextCheckbox extends ComponentExporter {

    String getText();

    boolean getIsChecked();

}
