<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="label.login" var="smLogin"/>
<spring:message code="label.email" var="smEmail" />
<spring:message code="label.password" var="smPassword" />
<spring:message code="button.register" var="smButton_register"/>
<spring:message code="button.back" var="smButton_back"/>

    <h2>Register:</h2>

    <sf:form name="f" method="POST" modelAttribute="user">
            <sf:input type="hidden" path="id" id="id"/>
            <sf:input type="hidden" path="version" id="version"/>

            <sf:label path="login">${smLogin}</sf:label>
            <sf:input path="login" id="login"/>
            <sf:errors path="login"/>
            <p/>
            <sf:label path="email">${smEmail}</sf:label>
            <sf:input path="email" id="email"/>
            <sf:errors path="email"/>
            <p/>
            <sf:label path="password">${smPassword}</sf:label>
            <sf:password path="password" id="password" showPassword="true"/>
            <sf:errors path="password"/>
            <p/>
            <br/>

            <input name="commit" type="submit" value="${smButton_register}" />
            <input type="button" class="back-button" onclick="history.back();" value="${smButton_back}" />
            <security:csrfInput />
    </sf:form>
