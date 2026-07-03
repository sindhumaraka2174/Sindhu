package com.adobe.aem.guides.june.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/bin/contactform",
        "sling.servlet.methods=GET"
})
public class ContactFormServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        if (name == null || email == null || mobile == null) {
            response.getWriter().write("Missing parameters");
            return;
        }

        try {
            ResourceResolver resolver = request.getResourceResolver();

            Resource parent = resolver.getResource("/content/contactform");

            if (parent == null) {
                response.getWriter().write("Parent path not found");
                return;
            }
            String nodeName = request.getParameter("nodeName");

            /* ===== EDIT CASE ===== */
            if (nodeName != null && !nodeName.isEmpty()) {

                Resource userNode = resolver.getResource(parent.getPath() + "/" + nodeName);

                if (userNode != null) {
                    ModifiableValueMap map = userNode.adaptTo(ModifiableValueMap.class);
                    map.put("name", name);
                    map.put("email", email);
                    map.put("mobile", mobile);

                    resolver.commit();
                    response.getWriter().write("Updated Successfully");
                    return;
                } else {
                    response.getWriter().write("Node not found for update");
                    return; // ⭐ IMPORTANT
                }
            }

            /* ===== CREATE CASE ===== */
            Map<String, Object> props = new HashMap<>();
            props.put("name", name);
            props.put("email", email);
            props.put("mobile", mobile);

            String newNodeName = "user_" + System.currentTimeMillis();
            resolver.create(parent, newNodeName, props);
            resolver.commit();

            response.getWriter().write("Saved Successfully");

        } catch (Exception e) {
            response.getWriter().write("Error while saving data");
        }
    }
}
