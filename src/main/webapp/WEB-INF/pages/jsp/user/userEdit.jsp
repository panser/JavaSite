<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>
<spring:message code="date_format_patternJS" var="dateFormatPatternJS"/>
<spring:message code="label_contact_new" var="labelContactNew"/>
<spring:message code="label_contact_update" var="labelContactUpdate"/>
<spring:message code="button.save" var="sfButton_save"/>
<spring:message code="button.add" var="sfButton_add"/>

<spring:eval expression="user.id == null ? labelContactNew:labelContactUpdate" var="formTitle"/>
<spring:eval expression="user.id == null ? sfButton_add:sfButton_save" var="sfButtonNew"/>


<script type="text/javascript">
    $(function(){
        $('#birthDay').datepicker({
            dateFormat: '${dateFormatPatternJS}',
            changeYear: true
        });
    });
</script>

<h2>${formTitle}</h2>
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

    <table>
        <tr>
            <td><sf:label path="login"><spring:message code="label.login"/></sf:label></td>
            <td><sf:input path="login" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="login"/></td>
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
            <td><sf:input path="birthDay" id="birthDay" placeholder="${dateFormatPattern}" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="birthDay"/></td>
        </tr>
        <tr>
            <td>Sex : </td>
            <td>
                <sf:radiobutton path="sex" value="Male" disabled="${disabledEdit}"/>Male
                <sf:radiobutton path="sex" value="Female" disabled="${disabledEdit}"/>Female
            </td>
            <td><sf:errors path="sex" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Subscribe to newsletter?  </td>
            <td><sf:checkbox path="receiveNewsletter" /></td>
            <td><sf:errors path="receiveNewsletter" cssClass="error" /></td>
        </tr>


        <security:authorize access="hasRole('ROLE_ADMIN')">
            <tr>
                <td>Role : </td>
                <td>
                    <sf:select path="role">
                        <%--<sf:option value="NONE" label="--- Select ---"/>--%>
                        <sf:options items="${roleList}" />
                    </sf:select>
                </td>
                <td><sf:errors path="role" cssClass="error" /></td>
            </tr>
            <tr>
                <td>Enabled?  </td>
                <td><sf:checkbox path="enabled" /></td>
                <td><sf:errors path="enabled" cssClass="error" /></td>
            </tr>
        </security:authorize>
    </table>

    <br/>
    <input name="commit" type="submit" value="${sfButtonNew}"/>
    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
    <security:csrfInput/>

</sf:form>
