<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="register.register" var="register_register"/>
<spring:message code="label.login" var="label_login"/>
<spring:message code="label.email" var="label_email" />
<spring:message code="label.password" var="label_password" />
<spring:message code="button.register" var="button_register"/>
<spring:message code="button.back" var="button_back"/>

    <h2>${register_register}</h2>

    <sf:form name="f" method="POST" modelAttribute="user">
            <sf:input type="hidden" path="id" id="id"/>
            <sf:input type="hidden" path="version" id="version"/>

            <sf:label path="login">${label_login}</sf:label>
            <sf:input path="login" id="login"/>
            <sf:errors path="login"/>
            <p/>
            <sf:label path="email">${label_email}</sf:label>
            <sf:input path="email" id="email"/>
            <sf:errors path="email"/>
            <p/>
            <sf:label path="password">${label_password}</sf:label>
            <sf:password path="password" id="password" showPassword="true"/>
            <sf:errors path="password"/>
            <p/>
            <br/>

            <input name="commit" type="submit" value="${button_register}" />
            <input type="button" class="back-button" onclick="history.back();" value="${button_back}" />
            <security:csrfInput />
    </sf:form>
