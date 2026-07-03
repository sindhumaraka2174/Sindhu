package com.adobe.aem.guides.june.core.models;

public class UserData {

    private String nodeName;
    private String name;
    private String email;
    private String mobile;

    public UserData(String nodeName, String name, String email, String mobile) {
        this.nodeName = nodeName;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
