package de.qucosa.rest;

import javax.servlet.http.HttpServletRequest;

public abstract class ControllerAbstract {

    protected String serverUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
