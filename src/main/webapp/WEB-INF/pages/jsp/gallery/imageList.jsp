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

<div>
    <a href="<c:url value="/gallery/${login}/"/>">
        <b>${login}</b>
    </a>
    /
    <b>${album.name}</b>
</div>

<div>
    <c:forEach items="${images}" var="image">
        <a href="<c:url value="/gallery/${login}/${album.name}/${image.name}"/>">
            <img src="<c:url value="/gallery/${login}/${album.name}/${image.name}/full"/>" width="240" height="240"/>
        </a>
    </c:forEach>
</div>

<br/>
<div>
    <c:if test="${disabledEdit=='false'}">
        <sf:form name="html5_upload" method="POST" enctype="multipart/form-data">
            <input type="file" name="files" id="files" multiple="multiple"/>
            <input type="submit"/>
        </sf:form>
    </c:if>
    <font color="red">
        <c:forEach items="${errorsMap}" var="entry">
            <p>
            ${entry.key}: ${entry.value}
            </p>
        </c:forEach>
    </font>
</div>
