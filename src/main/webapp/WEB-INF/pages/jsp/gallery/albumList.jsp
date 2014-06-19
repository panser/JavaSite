<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>


<%--<c:set var="disabledEdit">false</c:set>--%>
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>


Album List of user: <b>${login}</b>

<c:if test="${fn:length(albums) != 0}">
    <table border="1">
        <tr>
            <td></td>
            <td>Name</td>
            <td>Description</td>
            <td>PublicAccess</td>
            <td>Count images</td>
            <td>CreateDate</td>
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
                    <td><a href="<c:url value="/gallery/${login}/${album.name}?delete"/>">Delete</a>   </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${disabledEdit=='false'}">
    <fieldset>
        <sf:form name="f" method="POST" modelAttribute="albumNew">
            <p>
                <sf:label path="name">Name: </sf:label>
                <sf:input path="name"/>
                <sf:errors path="name"/>
            </p>
            <security:csrfInput/>
            <input name="commit" type="submit" value="Add album"/>
        </sf:form>
    </fieldset>
</c:if>

