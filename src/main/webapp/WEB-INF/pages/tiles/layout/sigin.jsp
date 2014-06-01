<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<security:authorize access="!isAuthenticated()">
    <c:url value="/auth/login" var="url"/>
    <a href="<c:out value='${url}'/>">
        <spring:message code="href.login"/>
    </a>
</security:authorize>
<security:authorize access="isAuthenticated()">
    <p>
        <c:set var="login"><security:authentication property="principal.username" /></c:set>
        Hello,
        <a href="<s:url value="/user/${login}/profile"/>">
        ${login},
        </a>

        <a href="javascript:formSubmit()">
            <spring:message code="href.logout"/>
        </a>
        <script>
            function formSubmit() {
                document.getElementById("logoutForm").submit();
            }
        </script>
        <c:url value="/auth/logout" var="logoutUrl"/>
    <form action="${logoutUrl}" method="post" id="logoutForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    </p>
</security:authorize>
<br/>
<a href="?lang=en">en</a>
|
<a href="?lang=ru">ru</a>
