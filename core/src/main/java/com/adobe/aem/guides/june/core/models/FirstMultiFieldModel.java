package com.adobe.aem.guides.june.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FirstMultiFieldModel {

    @ValueMapValue
    private String heading;

    @Inject
    private List<SecoundMultiFieldModel> secondMF;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<SecoundMultiFieldModel> getSecondMF() {
        return secondMF;
    }

    public void setSecondMF(List<SecoundMultiFieldModel> secondMF) {
        this.secondMF = secondMF;
    }

}
