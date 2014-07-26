<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>
<spring:message code="userList.header" var="userList_header"/>
<spring:message code="userList.add" var="userList_add"/>
<spring:message code="userList.login" var="userList_login"/>
<spring:message code="userList.email" var="userList_email"/>
<spring:message code="userList.password" var="userList_password"/>
<spring:message code="userList.birtday" var="userList_birtday"/>
<spring:message code="userList.delete" var="userList_delete"/>
<%--<spring:message code="" var=""/>--%>



<h2>${userList_header}</h2>

<c:if test="${not empty message}">
    <div id="message" class="success">${message}</div>
</c:if>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <a href="<c:url value="/user/add"/>">${userList_add}</a>
</security:authorize>

<table border="1">
    <tr>
        <td></td>
        <td>${userList_login}</td>
        <td>${userList_email}</td>
        <td>${userList_password}</td>
        <td>${userList_birtday}</td>
        <td></td>
    </tr>
    <c:forEach items="${users}" var="user">
        <%--<c:set var="enabledEdit">true</c:set>--%>
        <c:set var="enabledEdit">false</c:set>
        <security:authorize access="hasRole('ROLE_ADMIN')">
            <c:set var="enabledEdit">true</c:set>
        </security:authorize>

        <tr>
            <td>
                <a href="<c:url value="/user/${user.login}/profile"/>">
                    <img src="<c:url value="/user/${user.login}/avatar"/>" width="48" height="48"/>
                </a>
            </td>
            <td>${user.login}   </td>
            <td>${user.email}   </td>
            <td>${user.password}   </td>
            <td><fmt:formatDate value="${user.birthDay}" pattern="${dateFormatPattern}"/>   </td>
            <c:if test="${enabledEdit}">
                <td><a href="<c:url value="/user/${user.login}/delete"/>">${userList_delete}</a>   </td>
            </c:if>

        </tr>
    </c:forEach>
</table>
