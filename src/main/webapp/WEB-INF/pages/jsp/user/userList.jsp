<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title><spring:message code="userList.title" /></title>
</head>
<body>

<div id="login">
    <span style="float: right">
        <security:authorize access="!isAuthenticated()">
            <c:url value="/auth/login" var="url"/>
            <a href="<c:out value='${url}'/>">
                <spring:message code="href.login" />
            </a>
        </security:authorize>
        <security:authorize access="isAuthenticated()">
            <p>
                Hello,
                <a href="<c:url value="/user/edit"/>">
                    <security:authentication property="principal.username" />!
                </a>
            </p>
            <p>

            <a href="javascript:formSubmit()">
                <spring:message code="href.logout" />
            </a>
            <script>
                function formSubmit() {
                    document.getElementById("logoutForm").submit();
                }
            </script>
            <c:url value="/auth/logout" var="logoutUrl" />
            <form action="${logoutUrl}" method="post" id="logoutForm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
            </p>
        </security:authorize>
        <br/>
        <a href="?lang=en">en</a>
        |
        <a href="?lang=ru">ru</a>
    </span>
</div>

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
</body>
</html>
