package ua.org.gostroy.web.util;

import org.springframework.web.context.request.WebRequest;

/**
 * Created by panser on 6/15/2014.
 */
public class AjaxUtils {

    public static boolean isAjaxRequest(WebRequest webRequest) {
        String requestedWith = webRequest.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }

    public static boolean isAjaxUploadRequest(WebRequest webRequest) {
        return webRequest.getParameter("ajaxUpload") != null;
    }

    private AjaxUtils() {}

}