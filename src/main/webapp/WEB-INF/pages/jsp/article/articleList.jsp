<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>
<spring:message code="articleList.header" var="articleList_header"/>
<spring:message code="articleList.visits" var="articleList_visits"/>
<spring:message code="articleList.addNew" var="articleList_addNew"/>
<%--<spring:message code="" var=""/>--%>


<div class="add-new-article"><a href="<c:url value="/article/create"/>">${articleList_addNew}</a></div>
<h2>${articleList_header}</h2>


<c:forEach items="${articles}" var="article">

    <fieldset>
        <table>
            <tr>
                <td class="article-title"><h3>
                    <a href="<c:url value="/article/${article.id}"/>">${article.title}</a>
                </h3></td>
            </tr>
            <tr>
                <td class="article-date"><fmt:formatDate value="${article.createDate}" pattern="${dateFormatPattern}"/></td>
                <%--<td class="article-date"><fmt:formatDate value="${article.createDate}" type="both" dateStyle="short" timeStyle="short"/></td>--%>
            </tr>
            <tr>
                <td class="article-image"><img src="<c:url value="/user/${article.author.login}/avatar"/>" width="24" height="24"/></td>
            </tr>
            <tr>
                <td class="article-description">${article.description}</td>
            </tr>
            <tr>
                <td class="article-visits">${articleList_visits} ${fn:length(article.visitors)}</td>
            </tr>
        </table>
    </fieldset>
</c:forEach>
