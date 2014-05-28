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
<p style="color: coral">
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

<br/>
<br/>
<br/>
<div id="comments">
    <c:set var="commentPutUrl"><c:url value="${article.id}/comment/"/></c:set>
    <sf:form method="post" modelAttribute="comment" action="${commentPutUrl}">
        <b>Add Comment</b>
        <br/>

        <sf:label path="name">Name: </sf:label>
        <sf:input path="name" id="name"/>
        <sf:errors path="name"/>
        <p/>

        <sf:label path="email">Email: </sf:label>
        <sf:input path="email" id="email"/>
        <sf:errors path="email"/>
        <p/>

        <sf:label path="text">Comment: </sf:label>
        <sf:textarea path="text" id="text"/>
        <sf:errors path="text"/>
        <p/>

        <input name="commit" type="submit" value="Send Comment"/>
        <security:csrfInput />
    </sf:form>

    <b>Comments:</b>
    <c:forEach items="${comments}" var="comment">
        <fieldset>
            <p>
                <div id="nameComment" style="width:50%;float:left;">
                    <label>Name: </label>
                    ${comment.name}
                </div>
                <div id="dateComment" style="width:50%;float:right;">
                    <label>Date: </label>
                        <fmt:formatDate value="${comment.createDate}" type="both" dateStyle="short" timeStyle="short"/>
                </div>
            </p>
            <p>
                ${comment.text}
            </p>
        </fieldset>
    </c:forEach>
</div>
