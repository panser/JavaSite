<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<h2><spring:message code="userEdit.header"/></h2>
<font color="red">${userFromFormError}</font>

<%--<c:set var="disabledEdit">false</c:set>--%>
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${user.login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>

<sf:form name="f" method="PUT" modelAttribute="user" enctype="multipart/form-data">
    <%--<sf:input type="hidden" path="id" id="id"/>--%>
    <%--<sf:input type="hidden" path="version" id="version"/>--%>

    <sf:label path="login"><spring:message code="label.login"/></sf:label>
    <sf:input path="login" id="login" disabled="true"/>
    <p/>
    <sf:label path="email"><spring:message code="label.email"/></sf:label>
    <sf:input path="email" id="email" disabled="${disabledEdit}"/>
    <sf:errors path="email"/>
    <p/>
    <sf:label path="password"><spring:message code="label.password"/></sf:label>
    <sf:password path="password" showPassword="true" id="password" disabled="${disabledEdit}"/>
    <sf:errors path="password"/>
    <p/>
    <sf:label path="avatarImage"><spring:message code="label.avatarImage"/></sf:label>
    <sf:input type="file" path="avatarImage" id="avatarImage" disabled="${disabledEdit}"/>
    <%--<input id="multipartfile" name="multipartfile" type="file" value=""/>--%>
    <sf:errors path="avatarImage"/>
    <p/>
    <sf:label path="birthDay"><spring:message code="label.birthDay"/></sf:label>
    <form:input path="birthDay" id="birthDay" placeholder="dd.MM.yyyy" disabled="${disabledEdit}"/>
    <sf:errors path="birthDay"/>
    <p/>

    <input name="commit" type="submit" value="<spring:message code="button.save" />"/>
    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
    <security:csrfInput/>
</sf:form>
