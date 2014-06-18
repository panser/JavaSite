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



<script type="text/javascript">
    $(function(){
        $('#birthDay').datepicker({
            dateFormat: '${dateFormatPatternJS}',
            changeYear: true
        });
    });
</script>


<%--<c:set var="disabledEdit">false</c:set>--%>
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${user.login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>

<spring:eval expression="user.id == null ? labelContactNew:labelContactUpdate" var="formTitle"/>
<spring:eval expression="user.id == null ? sfButton_add:sfButton_save" var="sfButtonNew"/>
<c:choose>
    <c:when test="${user['new']}">
        <c:set var="method" value="post"/>
    </c:when>
    <c:otherwise>
        <c:set var="method" value="put"/>
    </c:otherwise>
</c:choose>

<form:form method="${method}" modelAttribute="user" enctype="multipart/form-data" cssClass="cleanform">
        <div class="header">
            <h2>${formTitle}</h2>
            <font color="red">${confirmRegistration}</font>
            <br/>
            <br/>

            <spring:bind path="*">
                <c:if test="${status.error}">
                    <div id="message" class="error">Form has errors</div>
                </c:if>
            </spring:bind>
        </div>
        <fieldset>
            <%--<form:input type="hidden" path="id" />--%>
            <%--<form:input type="hidden" path="version"/>--%>

            <legend>Personal Info</legend>
            <form:label path="login">
                <spring:message code="label.login"/> <form:errors path="login" cssClass="error" />
            </form:label>
            <form:input path="login" disabled="${disabledEdit}" />

            <form:label path="email">
                <spring:message code="label.email"/> <form:errors path="email" cssClass="error" />
            </form:label>
            <form:input path="email" disabled="${disabledEdit}" />

            <form:label path="password">
                <spring:message code="label.password"/> <form:errors path="password" cssClass="error" />
            </form:label>
            <form:input path="password" disabled="${disabledEdit}" />

            <form:label path="birthDay">
                <spring:message code="label.birthDay"/> <form:errors path="birthDay" cssClass="error" />
            </form:label>
            <form:input path="birthDay" disabled="${disabledEdit}" />

            <form:label path="avatarImage">
                <spring:message code="label.avatarImage"/> <form:errors path="avatarImage" cssClass="error" />
            </form:label>
            <form:input type="file" path="avatarImage" disabled="${disabledEdit}" />

            <fieldset>
                <legend>Sex:</legend>
                <label><form:radiobutton path="sex" value="Male" />Male</label>
                <label><form:radiobutton path="sex" value="Female" /> Female</label>
            </fieldset>
        </fieldset>


        <fieldset>
            <legend>Subscribe to Newsletter?</legend>
            <label><form:radiobutton path="receiveNewsletter" value="1"/> Yes</label>
            <label><form:radiobutton path="receiveNewsletter" value="0"/> No</label>
        </fieldset>

        <security:authorize access="hasRole('ROLE_ADMIN')">
            <fieldset>
                <legend>Security information:</legend>

                <label>Role:</label>
                <form:select path="role">
                    <form:options items="${roleList}" />
                </form:select>

                <fieldset>
                    <legend>Enabled:</legend>
                    <label><form:radiobutton path="enabled" value="1" />enabled</label>
                    <label><form:radiobutton path="enabled" value="0" /> disabled</label>
                </fieldset>
            </fieldset>
        </security:authorize>

        <br/>
        <input name="commit" type="submit" value="${sfButtonNew}"/>
        <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
        <security:csrfInput/>

</form:form>
