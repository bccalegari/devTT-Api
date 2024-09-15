package br.com.devtt.enterprise.infrastructure.configuration.spring;

import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.OutputDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        writeResponse(response, getResponseMessage());
    }

    private String getResponseMessage() throws JsonProcessingException {
        var responseDto = new OutputDto("Acesso negado! Faça login para acessar o recurso.");
        return new ObjectMapper().writeValueAsString(responseDto);
    }

    private void writeResponse(HttpServletResponse response, String responseMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(responseMessage);
    }
}