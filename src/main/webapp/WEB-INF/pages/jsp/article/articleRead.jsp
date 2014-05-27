<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    <div id="date" style="width:50%;float:left;">
        <label>Create date: </label>
        <fmt:formatDate value="${article.createDate}" type="both" dateStyle="short" timeStyle="short"/>
    </div>
    <div id="countUniqueVisitors" style="width:25%;float:right;">
        <label>Unique visitors: </label>
        ${countUniqueVisitors}
    </div>
    <div id="countVisitors" style="width:25%;float:right;">
        <label>Views: </label>
        ${countVisitors}
    </div>
</p>

<c:if test="${enabledEdit}">
    <td><a href="<c:url value="/article/${article.id}/edit"/>">Edit</a></td>
    <td><a href="<c:url value="/article/${article.id}/delete"/>">Delete</a></td>
</c:if>
