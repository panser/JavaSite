<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>
<spring:message code="date_format_patternJS" var="dateFormatPatternJS"/>
<spring:message code="userEdit.new" var="userEdit_new"/>
<spring:message code="userEdit.update" var="userEdit_update"/>
<spring:message code="button.save" var="button_save"/>
<spring:message code="button.add" var="button_add"/>
<spring:message code="button.back" var="button_back"/>
<spring:message code="userEdit.title" var="userEdit_title"/>
<spring:message code="label.login" var="label_login"/>
<spring:message code="label.email" var="label_email"/>
<spring:message code="label.password" var="label_password"/>
<spring:message code="label.birthDay" var="label_birthDay"/>
<spring:message code="label.avatarImage" var="label_avatarImage"/>
<spring:message code="radiobutton.sex" var="radiobutton_sex"/>
<spring:message code="radiobutton.sexMale" var="radiobutton_sexMale"/>
<spring:message code="radiobutton.sexFemale" var="radiobutton_sexFemale"/>
<spring:message code="radiobutton.newsletter" var="radiobutton_newsletter"/>
<spring:message code="radiobutton.newsletterYes" var="radiobutton_newsletterYes"/>
<spring:message code="radiobutton.newsletterNo" var="radiobutton_newsletterNo"/>
<spring:message code="userEdit.secInformation" var="userEdit_secInformation"/>
<spring:message code="label.role" var="label_role"/>
<spring:message code="radiobutton.enabled" var="radiobutton_enabled"/>
<spring:message code="radiobutton.enabledYes" var="radiobutton_enabledYes"/>
<spring:message code="radiobutton.enabledNo" var="radiobutton_enabledNo"/>
<%--<spring:message code="" var=""/>--%>



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

<spring:eval expression="user.id == null ? userEdit_new:userEdit_update" var="formTitle"/>
<spring:eval expression="user.id == null ? button_add:button_save" var="sfButtonNew"/>
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

            <legend>${userEdit_title}</legend>
            <form:label path="login">
                ${label_login} <form:errors path="login" cssClass="error" />
            </form:label>
            <c:choose>
                <c:when test="${user['new']}">
            <form:input path="login" disabled="${disabledEdit}" />
                </c:when>
                <c:otherwise>
                    <form:label path="login">
                        ${user.login}
                    </form:label>
                    <br/>
                </c:otherwise>
            </c:choose>

            <form:label path="email">
                ${label_email} <form:errors path="email" cssClass="error" />
            </form:label>
            <form:input path="email" disabled="${disabledEdit}" />

            <form:label path="password">
                ${label_password} <form:errors path="password" cssClass="error" />
            </form:label>
            <form:input path="password" disabled="${disabledEdit}" />

            <form:label path="birthDay">
                ${label_birthDay} <form:errors path="birthDay" cssClass="error" />
            </form:label>
            <%--<c:set var="formattedStartDate"><fmt:formatDate value="${article.createDate}" pattern="${dateFormatPattern}"/></c:set>--%>
            <%--<input path="${formattedStartDate}" disabled="${disabledEdit}" />--%>
            <form:input path="birthDay" disabled="${disabledEdit}" />

            <form:label path="avatarImage">
                ${label_avatarImage} <form:errors path="avatarImage" cssClass="error" />
            </form:label>
            <form:input type="file" path="avatarImage" disabled="${disabledEdit}" />

            <fieldset>
                <legend>${radiobutton_sex}</legend>
                <label><form:radiobutton path="sex" value="Male" />${radiobutton_sexMale}</label>
                <label><form:radiobutton path="sex" value="Female" /> ${radiobutton_sexFemale}</label>
            </fieldset>
        </fieldset>


        <fieldset>
            <legend>${radiobutton_newsletter}</legend>
            <label><form:radiobutton path="receiveNewsletter" value="1"/> ${radiobutton_newsletterYes}</label>
            <label><form:radiobutton path="receiveNewsletter" value="0"/> ${radiobutton_newsletterNo}</label>
        </fieldset>

        <security:authorize access="hasRole('ROLE_ADMIN')">
            <fieldset>
                <legend>${userEdit_secInformation}</legend>

                <label>${label_role}</label>
                <form:select path="role">
                    <form:options items="${roleList}" />
                </form:select>

                <fieldset>
                    <legend>${radiobutton_enabled}</legend>
                    <label><form:radiobutton path="enabled" value="1" />${radiobutton_enabledYes}</label>
                    <label><form:radiobutton path="enabled" value="0" /> ${radiobutton_enabledNo}</label>
                </fieldset>
            </fieldset>
        </security:authorize>

        <br/>
        <input name="commit" type="submit" value="${sfButtonNew}"/>
        <input type="button" class="back-button" onclick="history.back();" value="${button_back}"/>
        <security:csrfInput/>

</form:form>
