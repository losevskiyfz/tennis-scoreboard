package com.github.losevskiyfz.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.losevskiyfz.dto.ErrorResponse;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

@WebFilter("/*")
public class ExceptionHandler implements Filter {
    private static final Logger LOG = Logger.getLogger(ExceptionHandler.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Safe cast, because @WebFilter catch only http requests
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (BadPlayerNameException | BadScoreRequestException | PostScoreException | IllegalArgumentException e) {
            LOG.warning(getStackTraceAsString(e));
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), ErrorResponse.builder().message(e.getMessage()).build());
        } catch (BadUuidRequestException | GetMatchException e){
            LOG.warning(getStackTraceAsString(e));
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            httpResponse.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), ErrorResponse.builder().message(e.getMessage()).build());
        } catch (Exception e){
            LOG.severe(getStackTraceAsString(e));
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(), ErrorResponse.builder().message("500").build());
        }
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}