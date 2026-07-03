package com.adobe.aem.guides.june.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultiFieldModel {

    @Inject
    private List<FirstMultiFieldModel> firstMF;

    public List<FirstMultiFieldModel> getFirstMF() {
        return firstMF;
    }

    public void setFirstMF(List<FirstMultiFieldModel> firstMF) {
        this.firstMF = firstMF;
    }

}