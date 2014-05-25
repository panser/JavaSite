<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8"%>


<div id="container">
    <h2><spring:message code="userList.header" /></h2>
    <table>
        <c:forEach items="${users}" var="user">
            <tr>
                <%--<td><img src="/user/avatar/${user.login}" width="48" height="48" /></td>--%>
                <td><img src="<c:url value="/user/avatar/${user.login}"/>" width="48" height="48" /></td>
                <td>${user.id}</td>
                <td>${user.login}</td>
                <td>${user.email}</td>
                <td>${user.password}</td>
                <td><fmt:formatDate value="${user.birthDay}" pattern="dd.MM.yyyy" /></td>
            </tr>
        </c:forEach>
    </table>
</div>
