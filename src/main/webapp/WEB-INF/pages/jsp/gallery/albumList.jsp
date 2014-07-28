<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:message code="date_format_pattern_long" var="dateFormatPattern"/>
<spring:message code="albumList.header" var="albumList_header"/>
<spring:message code="albumList.name" var="albumList_name"/>
<spring:message code="albumList.desc" var="albumList_desc"/>
<spring:message code="albumList.publicAccess" var="albumList_publicAccess"/>
<spring:message code="albumList.countImage" var="albumList_countImage"/>
<spring:message code="albumList.createDate" var="albumList_createDate"/>
<spring:message code="albumList.delete" var="albumList_delete"/>
<spring:message code="albumList.addName" var="albumList_addName"/>
<spring:message code="albumList.addAlbum" var="albumList_addAlbum"/>
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


${albumList_header} <b>${login}</b>

<c:if test="${fn:length(albums) != 0}">
    <table border="1">
        <tr>
            <td></td>
            <td>${albumList_name}</td>
            <td>${albumList_desc}</td>
            <td>${albumList_publicAccess}</td>
            <td>${albumList_countImage}</td>
            <td>${albumList_createDate}</td>
            <c:if test="${disabledEdit=='false'}">
                <td></td>
            </c:if>
        </tr>
        <c:forEach items="${albums}" var="album">
            <c:url var="imgHref" value="/resources/image/template/albumDir.png"/>
            <c:if test="${not empty album.defImage}">
                <c:url var="imgHref" value="/gallery/${login}/${album.name}/${album.defImage.name}/full"/>
            </c:if>

            <tr>
                <td>
                    <a href="<c:url value="/gallery/${login}/${album.name}/"/>">
                        <img src="${imgHref}" width="48" height="48"/>
                    </a>
                </td>
                <td>${album.name}   </td>
                <td>${album.description}   </td>
                <td>${album.publicAccess}   </td>
                <td>${fn:length(album.images)}</td>
                <td><fmt:formatDate value="${album.createDate}" pattern="${dateFormatPattern}"/>   </td>
                <c:if test="${disabledEdit=='false'}">
                    <td><a href="<c:url value="/gallery/${login}/${album.name}?delete"/>">${albumList_delete}</a>   </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${disabledEdit=='false'}">
    <fieldset>
        <sf:form name="f" method="POST" modelAttribute="albumNew">
            <p>
                <sf:label path="name">${albumList_addName} </sf:label>
                <sf:input path="name"/>
                <sf:errors path="name"/>
            </p>
            <security:csrfInput/>
            <input name="commit" type="submit" value="${albumList_addAlbum}"/>
        </sf:form>
    </fieldset>
</c:if>

