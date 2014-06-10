<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>

<c:set var="disabledEdit">false</c:set>
<%--
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>
--%>

<div>
    <a href="<c:url value="/gallery/${login}/"/>">
        <b>${login}</b>
    </a>
    /
    <a href="<c:url value="/gallery/${login}/${album.name}/"/>">
        <b>${album.name}</b>
    </a>
    /
    <b>${image.name}</b>
</div>

<sf:form name="f" method="PUT" modelAttribute="image">
    <table>
        <tr>
            <td>
                <a href="<c:url value="/gallery/${login}/${album.name}/${image.name}/full"/>">
                    <img src="<c:url value="/gallery/${login}/${album.name}/${image.name}/full"/>" width="240" height="240"/>
                </a>
            </td>
        </tr>
        <tr>
            <td><label>URL: </label></td>
            <td>
                <a href="<c:url value="/gallery/${login}/${album.name}/${image.name}/full"/>">
                    <c:url value="${requestURL}/full"/><br/>
                    <%--<c:url value="${pageContext.request.serverName}/gallery/${login}/${album.name}/${image.name}/full"/><br/>--%>
                </a>
            </td>
        </tr>
        <tr>
            <td><sf:label path="name">Name: </sf:label></td>
            <td><sf:input path="name" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="name"/></td>
        </tr>
        <tr>
            <td><sf:label path="description">Description: </sf:label></td>
            <td><sf:input path="description" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="description"/></td>
        </tr>
        <tr>
            <td>Album : </td>
            <td>
                <sf:select path="album" disabled="${disabledEdit}">
                    <sf:options items="${albumList}"/>
                </sf:select>
<%--
                <sf:select path="album.id" disabled="${disabledEdit}">
                    <sf:options items="${albumList}" itemValue="id" itemLabel="name" />
                </sf:select>
--%>
            </td>
            <td><sf:errors path="album" cssClass="error" /></td>
        </tr>
        <tr>
            <td><sf:label path="size">Size: </sf:label></td>
            <td><fmt:formatNumber type="number" maxFractionDigits="3" value="${image.size}" />B</td>
            <%--<td>${image.size}</td>--%>
        </tr>
        <tr>
            <td><sf:label path="createDate">CreateDate: </sf:label></td>
            <td><fmt:formatDate value="${image.createDate}" pattern="${dateFormatPattern}"/>   </td>
        </tr>

        <c:if test="${disabledEdit=='false'}">
            <%--<c:if test="${not empty image.defAlbum}" var="isDefForAlbum"/>--%>
            <%--<h1>isDefForAlbum: <c:out value="${isDefForAlbum}"/></h1><br/>--%>
            <%--<h1>album: <c:out value="${album}"/></h1><br/>--%>
            <tr>
                <td>Use like title for album: </td>
                <td><sf:checkbox path="checkDefForAlbum"/></td>
                <%--<td><sf:checkbox path="defAlbum"  value="${album}"/></td>--%>
                <%--<td><sf:errors path="defAlbum" cssClass="error" /></td>--%>
            </tr>

            <tr>
                <td>
                    <a href="<c:url value="/gallery/${login}/${album.name}/${image.name}/delete"/>">
                        Delete
                    </a>
                </td>
            </tr>
        </c:if>
    </table>

    <br/>
    <input name="commit" type="submit" value="Save"/>
    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
    <security:csrfInput/>

</sf:form>

