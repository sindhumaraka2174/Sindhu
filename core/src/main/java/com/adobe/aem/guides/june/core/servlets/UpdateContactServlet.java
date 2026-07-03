package com.adobe.aem.guides.june.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/bin/updatecontact",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST
})
public class UpdateContactServlet extends SlingAllMethodsServlet {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String path = request.getParameter("path");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "contactService");

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
            Resource resource = resolver.getResource(path);
            if (resource != null) {
                ModifiableValueMap mvm = resource.adaptTo(ModifiableValueMap.class);
                if (mvm != null) {
                    mvm.put("name", name);
                    mvm.put("email", email);
                    mvm.put("mobile", mobile);
                    resolver.commit();
                    response.getWriter().write("Success");
                } else {
                    response.getWriter().write("Error: Could not adapt to ModifiableValueMap");
                }
            } else {
                response.getWriter().write("Error: Resource not found");
            }
        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
