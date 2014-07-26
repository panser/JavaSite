<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="date_format_pattern_long" var="dateFormatPattern"/>
<spring:message code="articleRead.createDate" var="articleRead_createDate"/>
<spring:message code="articleRead.visits" var="articleRead_visits"/>
<spring:message code="articleRead.uniqueVisits" var="articleRead_uniqueVisits"/>
<spring:message code="articleRead.edit" var="articleRead_edit"/>
<spring:message code="articleRead.delete" var="articleRead_delete"/>
<%--<spring:message code="" var=""/>--%>


<c:set var="enabledEdit">false</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${article.author.login == username}" var="enabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="enabledEdit">true</c:set>
</security:authorize>

<p>
    <b>${article.title}</b>
</p>
<p>
    ${article.description}
</p>
<p>
    ${article.text}
</p>
<p>
    <div id="date" style="width:50%;float:left;color:coral">
        <label>${articleRead_createDate} </label>
        <fmt:formatDate value="${article.createDate}" pattern="${dateFormatPattern}"/>
    </div>
    <div id="countUniqueVisitors" style="width:25%;float:right;color:coral">
        <label>${articleRead_uniqueVisits} </label>
        ${countUniqueVisitors}
    </div>
    <div id="countVisitors" style="width:25%;float:right;color:coral">
        <label>${articleRead_visits} </label>
        ${countVisitors}
    </div>
</p>

<c:if test="${enabledEdit}">
    <td><a href="<c:url value="/article/${article.id}/edit"/>">${articleRead_edit}</a></td>
    <td><a href="<c:url value="/article/${article.id}/delete"/>">${articleRead_delete}</a></td>
</c:if>


