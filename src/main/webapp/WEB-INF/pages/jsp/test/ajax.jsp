<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="pageContentId1">

<p>
    <b>Test AJAX</b>
</p>

<p>
    <ul>
        <li>
            <a id="getPlainTextLinkId1" class="getPlainTextLink" href="<c:url value="/test/ajax/getPlainText" />">getPlainText</a>
        </li>
        <li>
            <a id="getJsonLinkId1" class="getJsonLink" href="<c:url value="/test/ajax/getJson" />">getJson</a>
        </li>
        <li>
            <a id="getXmlLinkId1" class="getXmlLink" href="<c:url value="/test/ajax/getXml" />">getXml</a>
        </li>
        <li>
            <a id="getFormUrlencodedLinkId1" href="<c:url value="/test/ajax/getFormUrlencoded" />">getFormUrlencoded</a>
        </li>
        <li>
            <a id="getAtomLinkId1" href="<c:url value="/test/ajax/getAtom" />">getAtom</a>
        </li>
        <li>
            <a id="getRssLinkId1" href="<c:url value="/test/ajax/getRss" />">getRss</a>
        </li>


        <li>
            <form id="sendPlainTextFormId1" class="sendPlainTextForm" action="<c:url value="/test/ajax/sendPlainText" />" method="post">
                <input id="requestBodySubmit" type="submit" value="sendPlainTextForm" />
            </form>
        </li>
        <li>
            <form id="sendFormUrlencodedFormId1" action="<c:url value="/test/ajax/sendFormUrlencoded" />" method="post">
                <input id="readFormSubmit" type="submit" value="sendFormUrlencodedForm" />
            </form>
        </li>
        <li>
            <form id="sendJsonFormId1" class="sendJsonForm" action="<c:url value="/test/ajax/sendJson" />" method="post">
                <input id="byConsumesSubmit" type="submit" value="sendJsonForm" />
            </form>
        </li>
        <li>
            <form id="sendJsonFormId2" class="sendJsonForm invalid" action="<c:url value="/test/ajax/sendJson" />" method="post">
                <input id="readInvalidJsonSubmit" type="submit" value="sendJsonForm invalid (400 response code)" />
            </form>
        </li>
        <li>
            <form id="sendXmlFormId1" class="sendXmlForm" action="<c:url value="/test/ajax/sendXml" />" method="post">
                <input id="readXmlSubmit" type="submit" value="sendXmlForm" />
            </form>
        </li>
        <li>
            <form id="sendAtomFormId1" action="<c:url value="/test/ajax/sendAtom" />" method="post">
                <input id="readAtomSubmit" type="submit" value="sendAtomForm" />
            </form>
        </li>
        <li>
            <form id="sendRssFormId1" action="<c:url value="/test/ajax/sendRss" />" method="post">
                <input id="readRssSubmit" type="submit" value="sendRss" />
            </form>
        </li>
        <li>
            <fieldset>
                <legend>sendPostForm</legend>
                <c:if test="${not empty message}">
                    <div id="messageId1" class="success">${message}</div>
                </c:if>
                <c:set var="sendPostFormUrl"><c:url value="/test/ajax/sendPostForm"/></c:set>
                <form:form id="sendPostFormId1" method="post" modelAttribute="user" action="${sendPostFormUrl}">
                    <div class="header">
                        <spring:bind path="*">
                            <c:if test="${status.error}">
                                <div id="messageId1" class="error">Form has errors</div>
                            </c:if>
                        </spring:bind>
                    </div>
                    <div>
                        <form:label path="login">
                            <spring:message code="label.login"/> <form:errors path="login" cssClass="error" />
                        </form:label>
                        <form:input path="login"/>
                        <br/>

                        <form:label path="email">
                            <spring:message code="label.email"/> <form:errors path="email" cssClass="error" />
                        </form:label>
                        <form:input path="email"/>
                        <br/>

                        <form:label path="password">
                            <spring:message code="label.password"/> <form:errors path="password" cssClass="error" />
                        </form:label>
                        <form:input path="password"/>
                        <br/>


                        <p><button type="submit">Submit</button></p>
                    </div>
                </form:form>
            </fieldset>
        </li>
    </ul>


</p>



<script>
    MvcUtil = {};
    MvcUtil.showSuccessResponse = function (text, element) {
        MvcUtil.showResponse("success", text, element);
    };
    MvcUtil.showErrorResponse = function showErrorResponse(text, element) {
        MvcUtil.showResponse("error", text, element);
    };
    MvcUtil.showResponse = function(type, text, element) {
        var responseElementId = element.attr("id") + "Response";
        var responseElement = $("#" + responseElementId);
        if (responseElement.length == 0) {
            responseElement = $('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>').insertAfter(element);
        } else {
            responseElement.replaceWith('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>');
            responseElement = $("#" + responseElementId);
        }
        responseElement.fadeIn("slow");
    };
    MvcUtil.xmlencode = function(xml) {
        //for IE
        var text;
        if (window.ActiveXObject) {
            text = xml.xml;
        }
        // for Mozilla, Firefox, Opera, etc.
        else {
            text = (new XMLSerializer()).serializeToString(xml);
        }
        return text.replace(/\&/g,'&'+'amp;').replace(/</g,'&'+'lt;')
                .replace(/>/g,'&'+'gt;').replace(/\'/g,'&'+'apos;').replace(/\"/g,'&'+'quot;');
    };
</script>

<script type="text/javascript">
    $(document).ready(function() {

        $("a.getPlainTextLink").click(function(){
            var link = $(this);
            $.ajax({ url: link.attr("href"), dataType: "text",
//            $.ajax({ url: link.attr("href"),
                success: function(text) {
                    MvcUtil.showSuccessResponse(text, link);
                },
                error: function(xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("a.getJsonLink").click(function() {
            var link = $(this);
            $.ajax({ url: this.href,
                beforeSend: function(req) {
                    if (!this.url.match(/\.json$/)) {
                        req.setRequestHeader("Accept", "application/json");
                    }
                },
                success: function(json) {
                    MvcUtil.showSuccessResponse(JSON.stringify(json), link);
                },
                error: function(xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }});
            return false;
        });

        $("a.getXmlLink").click(function() {
            var link = $(this);
            $.ajax({ url: link.attr("href"),
                beforeSend: function(req) {
                    if (!this.url.match(/\.xml$/)) {
                        req.setRequestHeader("Accept", "application/xml");
                    }
                },
                success: function(xml) {
                    MvcUtil.showSuccessResponse(MvcUtil.xmlencode(xml), link);
                },
                error: function(xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("#getFormUrlencodedLinkId1").click(function() {
            var link = $(this);
            $.ajax({ url: this.href, dataType: "text",
                beforeSend: function(req) {
                    req.setRequestHeader("Accept", "application/x-www-form-urlencoded");
                },
                success: function(form) {
                    MvcUtil.showSuccessResponse(form, link);
                },
                error: function(xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("#getAtomLinkId1").click(function() {
            var link = $(this);
            $.ajax({ url: link.attr("href"),
                beforeSend: function(req) {
                    req.setRequestHeader("Accept", "application/atom+xml");
                },
                success: function(feed) {
                    MvcUtil.showSuccessResponse(MvcUtil.xmlencode(feed), link);
                },
                error: function(xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });

        $("#getRssLinkId1").click(function() {
            var link = $(this);
            $.ajax({ url: link.attr("href"),
                beforeSend: function(req) {
                    req.setRequestHeader("Accept", "application/rss+xml");
                },
                success: function(feed) {
                    MvcUtil.showSuccessResponse(MvcUtil.xmlencode(feed), link);
                },
                error: function(xhr) {
                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });



        $("form.sendPlainTextForm").submit(function(event) {
            var form = $(this);
            var button = form.children(":first");
            var data = "foo";
            $.ajax({ type: "POST", url: form.attr("action"), data: data, contentType: "text/plain", dataType: "text", success: function(text) { MvcUtil.showSuccessResponse(text, button); }, error: function(xhr) { MvcUtil.showErrorResponse(xhr.responseText, button); }});
            return false;
        });

        $("#sendFormUrlencodedFormId1").submit(function() {
            var form = $(this);
            var button = form.children(":first");
            var data = "foo=bar&fruit=apple";
            $.ajax({ type: "POST", url: form.attr("action"), data: data, contentType: "application/x-www-form-urlencoded", dataType: "text", success: function(text) { MvcUtil.showSuccessResponse(text, button); }, error: function(xhr) { MvcUtil.showErrorResponse(xhr.responseText, button); }});
            return false;
        });

        $("form.sendJsonForm").submit(function() {
            var form = $(this);
            var button = form.children(":first");
            var data = form.hasClass("invalid") ?
                    "{ \"foo\": \"bar\" }" :
                    "{ \"foo\": \"bar\", \"fruit\": \"apple\" }";
            $.ajax({ type: "POST", url: form.attr("action"), data: data, contentType: "application/json", dataType: "text", success: function(text) { MvcUtil.showSuccessResponse(text, button); }, error: function(xhr) { MvcUtil.showErrorResponse(xhr.responseText, button); }});
            return false;
        });

        $("form.sendXmlForm").submit(function() {
            var form = $(this);
            var button = form.children(":first");
            var data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><javaBean><foo>bar</foo><fruit>apple</fruit></javaBean>";
            $.ajax({ type: "POST", url: form.attr("action"), data: data, contentType: "application/xml", dataType: "text", success: function(text) { MvcUtil.showSuccessResponse(text, button); }, error: function(xhr) { MvcUtil.showErrorResponse(xhr.responseText, button); }});
            return false;
        });

        $("#sendAtomFormId1").submit(function() {
            var form = $(this);
            var button = form.children(":first");
            var data = '<?xml version="1.0" encoding="UTF-8"?> <feed xmlns="http://www.w3.org/2005/Atom"><title>My Atom feed</title></feed>';
            $.ajax({ type: "POST", url: form.attr("action"), data: data, contentType: "application/atom+xml", dataType: "text", success: function(text) { MvcUtil.showSuccessResponse(text, button); }, error: function(xhr) { MvcUtil.showErrorResponse(xhr.responseText, button); }});
            return false;
        });

        $("#sendRssFormId1").submit(function() {
            var form = $(this);
            var button = form.children(":first");
            var data = '<?xml version="1.0" encoding="UTF-8"?> <rss version="2.0"><channel><title>My RSS feed</title></channel></rss>';
            $.ajax({ type: "POST", url: form.attr("action"), data: data, contentType: "application/rss+xml", dataType: "text", success: function(text) { MvcUtil.showSuccessResponse(text, button); }, error: function(xhr) { MvcUtil.showErrorResponse(xhr.responseText, button); }});
            return false;
        });

/*
        $("#sendPostFormId1").submit(function() {
            $.post($(this).attr("action"), $(this).serialize(), function(html) {
                $("#pageContentId1").replaceWith(html);
                $('html, body').animate({ scrollTop: $("#messageId1").offset().top }, 500);
            });
            return false;
        });
*/


        // Include CSRF token as header in JQuery AJAX requests
        // See http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/#csrf-include-csrf-token-ajax
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

});
</script>

</div>