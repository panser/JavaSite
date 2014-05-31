<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<h2><spring:message code="userEdit.header"/></h2>
<font color="red">${userFromFormError}</font>
<font color="red">${confirmRegistration}</font>
<br/>
<br/>

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
    <%--<sf:input type="hidden" path="id" />--%>
    <%--<sf:input type="hidden" path="version"/>--%>

    <tr>
        <td><sf:label path="login"><spring:message code="label.login"/></sf:label></td>
        <td><sf:input path="login" disabled="true"/></td>
    </tr>
    <tr>
        <td><sf:label path="email"><spring:message code="label.email"/></sf:label></td>
        <td><sf:input path="email" disabled="${disabledEdit}"/></td>
        <td><sf:errors path="email"/></td>
    </tr>
    <tr>
        <td><sf:label path="password"><spring:message code="label.password"/></sf:label></td>
        <td><sf:password path="password" showPassword="true" disabled="${disabledEdit}"/></td>
        <td><sf:errors path="password"/></td>
    </tr>
    <tr>
        <td><sf:label path="avatarImage"><spring:message code="label.avatarImage"/></sf:label></td>
        <td><sf:input type="file" path="avatarImage" disabled="${disabledEdit}"/></td>
        <%--<input id="multipartfile" name="multipartfile" type="file" value=""/>--%>
        <td><sf:errors path="avatarImage"/>
    </tr>
    <tr>
        <td><sf:label path="birthDay"><spring:message code="label.birthDay"/></sf:label></td>
        <td><sf:input path="birthDay" placeholder="dd.MM.yyyy" disabled="${disabledEdit}"/></td>
        <td><sf:errors path="birthDay"/></td>
    </tr>
    <tr>
        <td>Subscribe to newsletter? : </td>
        <td><sf:checkbox path="receiveNewsletter" /></td>
        <td><sf:errors path="receiveNewsletter" cssClass="error" /></td>
    </tr>

    <input name="commit" type="submit" value="<spring:message code="button.save" />"/>
    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
    <security:csrfInput/>
</sf:form>
