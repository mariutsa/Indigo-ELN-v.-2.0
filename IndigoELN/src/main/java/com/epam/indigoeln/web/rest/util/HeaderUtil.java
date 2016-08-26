package com.epam.indigoeln.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 */
public class HeaderUtil {

    private static final String SUCCESS_ALERT = "X-indigoeln-success-alert";
    private static final String ERROR_ALERT = "X-indigoeln-error-alert";
    private static final String WARNING_ALERT = "X-indigoeln-warning-alert";
    private static final String INFO_ALERT = "X-indigoeln-info-alert";
    private static final String FAILURE_ALERT = "X-indigoeln-error";
    private static final String ALERT_PARAMS = "X-indigoeln-params";

    private HeaderUtil() {
    }

    public static HttpHeaders createSuccessAlert(String message, String param) {
        return createAlert(SUCCESS_ALERT, message, param);
    }

    public static HttpHeaders createErrorAlert(String message, String param) {
        return createAlert(ERROR_ALERT, message, param);
    }

    public static HttpHeaders createWarningAlert(String message, String param) {
        return createAlert(WARNING_ALERT, message, param);
    }

    public static HttpHeaders createInfoAlert(String message, String param) {
        return createAlert(INFO_ALERT, message, param);
    }

    public static HttpHeaders createFailureAlert(String message, String entityName) {
        return createAlert(FAILURE_ALERT, message, entityName);
    }

    private static HttpHeaders createAlert(String type, String message, String params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(type, message);
        return headers;
    }

    public static HttpHeaders createAttachmentDescription(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        return headers;
    }

    public static HttpHeaders createEntityCreateAlert(String entityName, String param) {
        String message;
        if (param != null) {
            message = "The " + entityName + " \"" + param + "\"" + " is created";
        } else {
            message = "The " + entityName + " is successfully created";
        }
        return createSuccessAlert(message, param);
    }

    public static HttpHeaders createEntityDeleteAlert(String entityName, String param) {
        String message;
        if (param != null) {
            message = "The " + entityName + " \"" + param + "\"" + " is deleted";
        } else {
            message = "The " + entityName + " is successfully deleted";
        }
        return createSuccessAlert(message, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        String message;
        if (param != null) {
            message = "The " + entityName + " \"" + param + "\"" + " is updated";
        } else {
            message = "The " + entityName + " is successfully updated";
        }
        return createSuccessAlert(message, param);
    }
}