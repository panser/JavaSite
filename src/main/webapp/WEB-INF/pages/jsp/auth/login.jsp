<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<c:if test="${not empty param.login_error}">
    <font color="red"> <spring:message code="springSecurity.loginerror" />
        : <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/> </font>
</c:if>

<c:url var="authUrl" value="/j_spring_security_check" />
<form name="f" method="post" class="signin" action="${authUrl}">
        <table>
            <tr>
                <td><label path="login"><spring:message code="label.login" /></label></td>
                <td><input id="login" type="text" name="j_username" /></td>
                <td><sf:errors path="login"/></td>
            </tr>
            <tr>
                <td><label path="password"><spring:message code="label.password" /></label></td>
                <td><input type="password" name="j_password" /></td>
                <td><sf:errors path="password"/></td>
                <%--<small><a href="/account/resend_password">Forgot?</a></small>--%>
            </tr>
            <tr>
                <td><spring:message code="checkbox.remember" /></td>
                <td><input type="checkbox" name="_spring_security_remember_me" /></td>
            </tr>
            <tr>
                <td>
                    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />" />
                    <a href="<c:url value="/user/register"/> "><spring:message code="button.register" /></a>
                    <input type="submit" value="<spring:message code="button.login" />" />
                </td>
            </tr>
        </table>
        <security:csrfInput />
</form>
