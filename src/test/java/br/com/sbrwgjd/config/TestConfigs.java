package br.com.sbrwgjd.config;

public interface TestConfigs {

    int SERVER_PORT = 8888;
    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_LOCAL = "http://localhost:8080";
    String ORIGIN_PRONTUARIOS = "http://prontuarios-jaala.com";
    String ORIGIN_NO_ACCESS = "http://no-access.com";
}
