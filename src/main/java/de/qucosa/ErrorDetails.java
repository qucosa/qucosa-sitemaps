/*
 * Copyright 2019 Saxon State and University Library Dresden (SLUB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.qucosa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ErrorDetails {
    private String classname;
    private LocalDateTime date;
    private String errorMsg;
    private Exception exception;
    private String method;
    private String requestMethod;
    private String requestPath;
    private StackTraceElement[] stacktrace;
    private HttpStatus statuscode;

    public ErrorDetails(String classname, String method, String requestMethodAndPath, HttpStatus statuscode, String errorMsg, Exception exception) {
        this.date = LocalDateTime.now();
        this.classname = classname;
        this.method = method;
        this.statuscode = statuscode;
        this.errorMsg = (exception != null) ? exception.getMessage() : errorMsg;
        setException(exception);
        setRequestPath(requestMethodAndPath);
    }

    public ResponseEntity response() {
        return new ResponseEntity<>(this, this.statuscode);
    }

    public String getClassname() {
        return classname;
    }

    public String getStatuscode() {
        return statuscode.toString();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getRequestPath() {
        return requestPath;
    }

    private void setRequestPath(String requestMethodAndPath) {
        String[] req = requestMethodAndPath.split(":");
        this.requestPath = req[1];
        this.requestMethod = req[0];
    }

    public Exception getException() {
        return exception;
    }

    private void setException(Exception exception) {

        if (exception != null) {
            this.exception = exception;
            setStacktrace(this.exception.getStackTrace());
        }
    }

    public StackTraceElement[] getStacktrace() {
        return stacktrace;
    }

    private void setStacktrace(StackTraceElement[] stacktrace) {
        this.stacktrace = stacktrace;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestMethod() {
        return requestMethod;
    }
}
