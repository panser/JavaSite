<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<spring:message code="label.login" var="label_login"/>
<spring:message code="label.password" var="label_password"/>
<spring:message code="checkbox.remember" var="checkbox_remember"/>
<spring:message code="button.login" var="button_login"/>
<spring:message code="button.back" var="button_back"/>
<spring:message code="button.register" var="button_register"/>
<%--<spring:message code="" var=""/>--%>


<%--
<c:if test="${not empty param.login_error}">
    <font color="red"> <spring:message code="springSecurity.loginerror" />
        : <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/> </font>
</c:if>
--%>
<font color="red">
    <c:if test="${not empty message_error}">
        <div id="message" class="${message_error.type}">${message_error.message}</div>
    </c:if>
</font>


<c:url var="authUrl" value="/j_spring_security_check" />
<form name="f" method="post" class="signin" action="${authUrl}">
        <table>
            <tr>
                <td><label path="login">${label_login}</label></td>
                <td><input id="login" type="text" name="j_username" /></td>
                <td><sf:errors path="login"/></td>
            </tr>
            <tr>
                <td><label path="password">${label_password}</label></td>
                <td><input type="password" name="j_password" /></td>
                <td><sf:errors path="password"/></td>
                <%--<small><a href="/account/resend_password">Forgot?</a></small>--%>
            </tr>
            <tr>
                <td>${checkbox_remember}</td>
                <td><input type="checkbox" name="_spring_security_remember_me" /></td>
            </tr>
            <tr>
                <td>
                    <input type="submit" value="${button_login}" />
                    <input type="button" class="back-button" onclick="history.back();" value="${button_back}" />
                    <a href="<c:url value="/user/register"/> ">${button_register}</a>
                </td>
            </tr>
        </table>
        <security:csrfInput />
</form>
