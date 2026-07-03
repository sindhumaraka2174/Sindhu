package com.adobe.aem.guides.june.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SidebarModel {

    /* ---------------------- HEADER TAB ---------------------- */

    @ValueMapValue
    private String logopath;

    @ValueMapValue
    private String logomobile;

    @ValueMapValue
    private String logolink;

    @ValueMapValue
    private boolean enableswitch;

    public String getLogopath() {
        return logopath;
    }

    public String getLogomobile() {
        return logomobile;
    }

    public String getLogolink() {
        return logolink;
    }

    public boolean isEnableswitch() {
        return enableswitch;
    }

    /* ---------------------- HEADER LINKS TAB ---------------------- */
    @ChildResource(name = "linkmf")
    private List<HeaderLinkItem> linkList;

    public List<HeaderLinkItem> getLinkList() {
        return linkList;
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class HeaderLinkItem {
        @ValueMapValue
        private String name;

        @ValueMapValue
        private String image;

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }
    }

    /* ---------------------- SIDEBAR NAVIGATION ---------------------- */
    @ChildResource(name = "navigationmf")
    private List<NavItem> navigationList;

    public List<NavItem> getNavigationList() {
        return navigationList;
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class NavItem {

        @ValueMapValue
        private String desktop;

        @ValueMapValue
        private String mobile;

        @ChildResource(name = "nestedmf")
        private List<NestedItem> nested;

        public String getDesktop() {
            return desktop;
        }

        public String getMobile() {
            return mobile;
        }

        public List<NestedItem> getNested() {
            return nested;
        }
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class NestedItem {
        @ValueMapValue
        private String navigationurl;

        public String getNavigationurl() {
            return navigationurl;
        }
    }

    /* ---------------------- REGION ---------------------- */
    @ValueMapValue(name = "country")
    private String country;

    public String getCountry() {
        return country;
    }
}
