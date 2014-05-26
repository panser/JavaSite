<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<h2><spring:message code="userList.header"/></h2>

<table>
    <c:forEach items="${users}" var="user">
        <%--<c:set var="enabledEdit">true</c:set>--%>
        <c:set var="enabledEdit">false</c:set>
        <security:authorize access="hasRole('ROLE_ADMIN')">
            <c:set var="enabledEdit">true</c:set>
        </security:authorize>

        <tr>
            <td><a href="<c:url value="/user/${user.login}"/>">
                <img src="<c:url value="/user/${user.login}/avatar"/>" width="48" height="48"/>
            </a></td>
            <td>${user.id}</td>
            <td>${user.login}</td>
            <td>${user.email}</td>
            <td>${user.password}</td>
            <td><fmt:formatDate value="${user.birthDay}" pattern="dd.MM.yyyy"/></td>
            <c:if test="${enabledEdit}">
                <td><a href="<c:url value="/user/${user.id}/delete"/>">Delete</a></td>
            </c:if>

        </tr>
    </c:forEach>
</table>
