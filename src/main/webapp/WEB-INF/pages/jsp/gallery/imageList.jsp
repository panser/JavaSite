<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="imageList.upload" var="imageList_upload"/>
<%--<spring:message code="" var=""/>--%>


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
                <input type="submit" value="${imageList_upload}"/>
            </sf:form>
        </c:if>
    </div>
</c:if>

<%----%>
<%--images list--%>
<%----%>


<div id="errorsUpload">
    <font color="red">
        <c:forEach items="${errors}" var="entry">
            <p>
                ${entry.key}: ${entry.value}
            </p>
        </c:forEach>
    </font>
</div>


    <c:forEach items="${images}" var="image">
        <a href="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}"/>">
        <img id="image-"${image.id} class="image" src="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}/full"/>" width="240" height="240"/>
        </a>
    </c:forEach>

<c:if test="${!ajaxUpload}">
    <script type="text/javascript">
        $(document).ready(function() {
            /*
            $('<input type="hidden" name="ajaxUpload" value="true" />').insertAfter($("#files"));
            $("#html5_upload").ajaxForm({ success: function(html) {
                ${".image"}.after(html);
                }
            });
*/
        });
    </script>
</c:if>

