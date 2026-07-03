package com.adobe.aem.guides.june.core.servlets;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

/**
 * Path-based servlet available at /bin/randomGenerator
 * Query parameter: type=Number | Letters | Random
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Random generator servlet (numbers, letters, mixed)",
        "sling.servlet.paths=/bin/randomGenerator",
        "sling.servlet.methods=GET"
})
public class RandomGeneratorServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        // parameter name we read. Accepts values "Number", "Letters", "Random"
        String type = request.getParameter("type");
        if (type == null) {
            // fallback: check if any of the parameter-names "Number"/"Letters"/"Random" are
            // present (optional)
            if (request.getParameter("Number") != null)
                type = "Number";
            else if (request.getParameter("Letters") != null)
                type = "Letters";
            else if (request.getParameter("Random") != null)
                type = "Random";
        }

        if (type == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Missing parameter 'type' (expected values: Number, Letters, Random).");
            return;
        }

        String result;
        switch (type.trim().toLowerCase()) {
            case "number":
            case "numbers":
                result = generateNumbers(6);
                break;
            case "letters":
            case "letter":
                result = generateLetters(6);
                break;
            case "random":
                result = generateMixed(3, 3); // 3 digits + 3 letters
                break;
            default:
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Invalid type. Use Number, Letters, or Random.");
                return;
        }

        // return result as plain text (or JSON if you prefer)
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(result);
    }

    // helper: generate `count` digits as contiguous string, e.g. "394021"
    private String generateNumbers(int count) {
        StringBuilder sb = new StringBuilder(count);
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            sb.append(rnd.nextInt(0, 10)); // 0..9
        }
        return sb.toString();
    }

    // helper: generate `count` uppercase letters, e.g. "ABCDXZ"
    private String generateLetters(int count) {
        StringBuilder sb = new StringBuilder(count);
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            char c = (char) ('A' + rnd.nextInt(0, 26));
            sb.append(c);
        }
        return sb.toString();
    }

    // helper: generate digitsCount digits followed by lettersCount letters, e.g.
    // "345IJH"
    private String generateMixed(int digitsCount, int lettersCount) {
        StringBuilder sb = new StringBuilder(digitsCount + lettersCount);
        sb.append(generateNumbers(digitsCount));
        sb.append(generateLetters(lettersCount));
        return sb.toString();
    }
}
