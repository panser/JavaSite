<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<spring:message code="accessDeniedException.security" var="accessDeniedException_security"/>
<%--<spring:message code="" var=""/>--%>

<b>spring-security:</b> ${accessDeniedException_security}