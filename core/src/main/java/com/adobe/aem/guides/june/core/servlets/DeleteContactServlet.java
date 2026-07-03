package com.adobe.aem.guides.june.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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
        "sling.servlet.paths=/bin/deletecontact",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST
})
public class DeleteContactServlet extends SlingAllMethodsServlet {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String path = request.getParameter("path");

        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "contactService");

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
            Resource resource = resolver.getResource(path);
            if (resource != null) {
                resolver.delete(resource);
                resolver.commit();
                response.getWriter().write("Success");
            } else {
                response.getWriter().write("Error: Resource not found");
            }
        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
