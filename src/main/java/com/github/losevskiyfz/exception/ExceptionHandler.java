package com.github.losevskiyfz.exception;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import static com.github.losevskiyfz.servlet.TennisScoreboardServlet.*;

@WebFilter("/*")
public class ExceptionHandler implements Filter {
    private static final Logger LOG = Logger.getLogger(ExceptionHandler.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Safe cast, because @WebFilter catch only http requests
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            chain.doFilter(request, response);
        } catch (BadPlayerNameException | BadScoreRequestException | PostScoreException | IllegalArgumentException |
                 BadUuidRequestException | GetMatchException e) {
            LOG.warning(getStackTraceAsString(e));
            req.setAttribute("error", e.getMessage());
            req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
            req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        } catch (Exception e){
            LOG.warning(getStackTraceAsString(e));
            req.setAttribute("error", "Internal server error");
            req.setAttribute("matchesUrl", ROOT_URL + ENDED_MATCHES_URL);
            req.setAttribute("homeUrl", ROOT_URL + WELCOME_URL);
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}