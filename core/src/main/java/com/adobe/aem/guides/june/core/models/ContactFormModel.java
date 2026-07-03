package com.adobe.aem.guides.june.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFormModel {

    @Self
    private SlingHttpServletRequest request;

    private String name;
    private String email;
    private String mobile;

    private List<UserData> users = new ArrayList<>();

    @PostConstruct
    protected void init() {
        String nodeName = request.getParameter("node");

        // --- Edit functionality: pre-fill form if nodeName exists ---
        if (nodeName != null && !nodeName.isEmpty()) {
            Resource node = request.getResourceResolver().getResource("/content/contactform/" + nodeName);
            if (node != null) {
                name = node.getValueMap().get("name", "");
                email = node.getValueMap().get("email", "");
                mobile = node.getValueMap().get("mobile", "");
            }
        }

        // --- Read functionality: populate all users for table ---
        Resource parent = request.getResourceResolver().getResource("/content/contactform");
        if (parent != null) {
            for (Resource child : parent.getChildren()) {
                String userName = child.getValueMap().get("name", "");
                String userEmail = child.getValueMap().get("email", "");
                String userMobile = child.getValueMap().get("mobile", "");

                users.add(new UserData(child.getName(), userName, userEmail, userMobile));
            }
        }
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public List<UserData> getUsers() {
        return users;
    }
}
