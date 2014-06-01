<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2><spring:message code="articleList.header"/></h2>
    <a href="<c:url value="/article/create"/>">Add article</a>

<c:forEach items="${articles}" var="article">

    <fieldset>
        <table>
            <tr>
                <td><h3>
                    <a href="<c:url value="/article/${article.id}"/>">${article.title}</a>
                </h3></td>
            </tr>
            <tr>
                <td><img src="<c:url value="/user/${article.author.login}/avatar"/>" width="24" height="24"/></td>
                <td><fmt:formatDate value="${article.createDate}" type="both" dateStyle="short" timeStyle="short"/></td>
            </tr>
            <tr>
                <td>${article.description}</td>
            </tr>
            <tr  style="color:coral">
                <td>${fn:length(article.visitors)}</td>
            </tr>
        </table>
    </fieldset>
</c:forEach>
