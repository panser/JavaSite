<%@ page session="false" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title><spring:message code="userEdit.title" /></title>
</head>

<body onload="document.f.login.focus();">
<h2><spring:message code="userEdit.header" /></h2>
<div id="container">
    <font color = "red">${userFromFormError}</font>
    <sf:form name="f" method="PUT" modelAttribute="user" enctype="multipart/form-data">
            <sf:label path="login"><spring:message code="label.login" /></sf:label>
            <sf:input path="login" id="login"/>
            <sf:errors path="login"/>
            <p/>
            <sf:label path="email"><spring:message code="label.email" /></sf:label>
            <sf:input path="email" id="email"/>
            <sf:errors path="email"/>
            <p/>
            <sf:label path="password"><spring:message code="label.password" /></sf:label>
            <sf:password path="password" showPassword="true" id="password"/>
            <sf:errors path="password"/>
            <p/>
            <sf:label path="avatarImage"><spring:message code="label.avatarImage" /></sf:label>
            <input name="avatarImage" type="file">
            <%--<input id="multipartfile" name="multipartfile" type="file" value=""/>--%>
            <sf:errors path="avatarImage"/>
            <p/>
            <sf:label path="birthDay"><spring:message code="label.birthDay" /></sf:label>
            <form:input path="birthDay" id="birthDay" placeholder="dd.MM.yyyy"/>
            <sf:errors path="birthDay"/>
            <p/>

            <input name="commit" type="submit" value="<spring:message code="button.save" />" />
            <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />" />
            <security:csrfInput />
    </sf:form>


</div>

</body>
</html>
