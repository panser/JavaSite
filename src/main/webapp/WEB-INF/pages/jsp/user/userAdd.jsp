<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <h2>Register:</h2>

    <sf:form name="f" method="POST" modelAttribute="user">
            <sf:input type="hidden" path="id" id="id"/>
            <sf:input type="hidden" path="version" id="version"/>

            <sf:label path="login"><spring:message code="label.login" /></sf:label>
            <sf:input path="login" id="login"/>
            <sf:errors path="login"/>
            <p/>
            <sf:label path="password"><spring:message code="label.password" /></sf:label>
            <sf:password path="password" id="password" showPassword="true"/>
            <sf:errors path="password"/>
            <p/>
            <br/>

            <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />" />
            <input name="commit" type="submit" value="<spring:message code="button.register" />" />
            <security:csrfInput />
    </sf:form>
