package de.qucosa.rest;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

public abstract class ControllerAbstract {
    @Value("${server.port}")
    protected int serverPort;

    protected String serverUrl(HttpServletRequest request, int port) {
        return request.getScheme() + "://" + request.getServerName() + ":" + port;
    }
}
