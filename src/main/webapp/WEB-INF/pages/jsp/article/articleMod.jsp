<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="container">

    <h2>Article:</h2>
    <sf:form name="f" method="POST" modelAttribute="article">
        <sf:input type="hidden" path="id" id="id"/>
        <sf:input type="hidden" path="version" id="version"/>

        <sf:label path="title"><spring:message code="article.title" /></sf:label>
        <sf:input path="title" id="title"/>
        <sf:errors path="title"/>
        <p/>
        <sf:label path="text"><spring:message code="article.text" /></sf:label>
        <sf:textarea path="text" id="text"/>
        <sf:errors path="text"/>
        <p/>
        <br/>

        <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />" />
        <input name="commit" type="submit" value="<spring:message code="button.save" />" />
        <security:csrfInput />
    </sf:form>

</div>