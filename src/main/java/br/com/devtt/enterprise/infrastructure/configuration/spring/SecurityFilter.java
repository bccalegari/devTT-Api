package br.com.devtt.enterprise.infrastructure.configuration.spring;

import br.com.devtt.core.auth.abstractions.application.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityFilter extends OncePerRequestFilter {
    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    @Qualifier("JwtTokenService")
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var bearerToken = getBearerToken(request);

        if (bearerToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var subject = tokenService.extractSubject(bearerToken);
        var name = tokenService.extractName(bearerToken);
        var role = tokenService.extractRole(bearerToken);
        setRequestAttribute(request, subject, name, role);
        filterChain.doFilter(request, response);
    }

    private String getBearerToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.replace("Bearer ", "") : "";
    }

    private void setRequestAttribute(HttpServletRequest request, String subject, String name, String role) {
        if (!subject.isEmpty()) {
            request.setAttribute("idUser", subject);
            request.setAttribute("name", name);
            request.setAttribute("role", role);
            setSpringSecurityContext(subject, Collections.singletonList(getGrant(role)));
        }
    }

    private SimpleGrantedAuthority getGrant(String role) {
        return new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase());
    }

    private void setSpringSecurityContext(String subject, List<SimpleGrantedAuthority> grants) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(subject, null, grants);
            SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}