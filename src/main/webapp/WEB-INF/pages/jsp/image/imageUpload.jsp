<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="disabledEdit">false</c:set>
<%--
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${user.login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>
--%>

<sf:form name="f" method="POST" modelAttribute="image" enctype="multipart/form-data">
    <%--<sf:input type="hidden" path="id" />--%>
    <%--<sf:input type="hidden" path="version"/>--%>

    <table>
        <tr>
            <td><sf:label path="name">Name: </sf:label></td>
            <td><sf:input path="name" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="name"/></td>
        </tr>
        <tr>
            <td><sf:label path="description">Description: </sf:label></td>
            <td><sf:input path="description" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="description"/></td>
        </tr>
        <tr>
            <td><sf:label path="multipartFile">Upload image</sf:label></td>
            <td><sf:input type="file" path="multipartFile" disabled="${disabledEdit}"/></td>
                <%--<input id="multipartFile" name="multipartFile" type="file" value=""/>--%>
            <td><sf:errors path="multipartFile"/>
        </tr>
    </table>

    <br/>
    <input name="commit" type="submit" value="Upload"/>
    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
    <security:csrfInput/>

</sf:form>
