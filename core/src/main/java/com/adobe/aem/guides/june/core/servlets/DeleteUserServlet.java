package com.adobe.aem.guides.june.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/bin/deleteuser",
        "sling.servlet.methods=GET"
})
public class DeleteUserServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");

        String nodeName = request.getParameter("node");

        if (nodeName == null || nodeName.isEmpty()) {
            response.getWriter().write("Node name missing");
            return;
        }

        try {
            ResourceResolver resolver = request.getResourceResolver();

            String path = "/content/contactform/" + nodeName;
            Resource userNode = resolver.getResource(path);

            if (userNode != null) {
                resolver.delete(userNode);
                resolver.commit();
                response.getWriter().write("Deleted Successfully");
            } else {
                response.getWriter().write("Node not found");
            }

        } catch (Exception e) {
            response.getWriter().write("Error while deleting data");
        }
    }
}
