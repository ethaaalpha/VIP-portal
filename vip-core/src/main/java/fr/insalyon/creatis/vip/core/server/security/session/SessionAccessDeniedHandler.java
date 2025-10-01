package fr.insalyon.creatis.vip.core.server.security.session;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.DeferredCsrfToken;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException instanceof InvalidCsrfTokenException) {
            InvalidCsrfTokenException ex = (InvalidCsrfTokenException) accessDeniedException;

            DeferredCsrfToken deferredCsrfToken = new CookieCsrfTokenRepository().loadDeferredToken(request, response);

            logger.debug("Failed to authenticate request: " + accessDeniedException.getMessage());
            logger.debug("Excepted: " + deferredCsrfToken.get().getToken());
        }
    }
}
