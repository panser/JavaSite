<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <h2>Article:</h2>
    <sf:form name="f" method="${formMethod}" modelAttribute="article">
        <%--<sf:input type="hidden" path="id" id="id"/>--%>
        <%--<sf:input type="hidden" path="version" id="version"/>--%>

        <sf:label path="title"><spring:message code="article.title" /></sf:label>
        <sf:errors path="title"/>
        <sf:input path="title" id="title"/>
        <p/>
        <sf:label path="description"><spring:message code="article.description" /></sf:label>
        <sf:errors path="description"/>
        <sf:textarea path="description" id="description"/>
        <p/>
        <br/>
        <p/>
        <sf:label path="text"><spring:message code="article.text" /></sf:label>
        <sf:errors path="text"/>
        <sf:textarea path="text" id="text"/>
        <p/>
        <br/>

        <input name="commit" type="submit" value="<spring:message code="button.save" />" />
        <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />" />
        <security:csrfInput />
    </sf:form>