<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--<c:set var="disabledEdit">false</c:set>--%>
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>

<c:if test="${!ajaxUpload}">
    <div>
        <a href="<c:url value="/gallery/${login}/"/>">
            <b>${login}</b>
        </a>
        /
        <b>${album.name}</b>
    </div>

    <div id="uploadInput">
        <c:if test="${disabledEdit=='false'}">
            <sf:form id="html5_upload" method="POST" enctype="multipart/form-data">
                <input type="file" name="files" id="files" multiple="multiple"/>
                <input type="submit" value="Upload"/>
            </sf:form>
        </c:if>
    </div>
</c:if>

<c:if test="${!ajaxUpload}">
    <script type="text/javascript">
        $(document).ready(function() {
            $('<input type="hidden" name="ajaxUpload" value="true" />').insertAfter($("#files"));
            $("#html5_upload").ajaxForm({ success: function(html) {
//                $("#fileuploadContent").replaceWith(html);
                html.after('#uploadInput');
            }

/*                $.ajax({
                        url: "test.html",
                        context: document.body
                    }).done(function() {
                        $( this ).addClass( "done" );
                    });*/
            });
        });
    </script>
</c:if>