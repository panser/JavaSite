<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="date_format_pattern" var="dateFormatPattern"/>
<spring:message code="imageEdit.url" var="imageEdit_url"/>
<spring:message code="imageEdit.name" var="imageEdit_name"/>
<spring:message code="imageEdit.desc" var="imageEdit_desc"/>
<spring:message code="imageEdit.album" var="imageEdit_album"/>
<spring:message code="imageEdit.size" var="imageEdit_size"/>
<spring:message code="imageEdit.createDate" var="imageEdit_createDate"/>
<spring:message code="imageEdit.title" var="imageEdit_title"/>
<spring:message code="imageEdit.delete" var="imageEdit_delete"/>
<spring:message code="imageEdit.save" var="imageEdit_save"/>
<%--<spring:message code="" var=""/>--%>


<%--<c:set var="disabledEdit">false</c:set>--%>
<c:set var="disabledEdit">true</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${login != username}" var="disabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="disabledEdit">false</c:set>
</security:authorize>

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
            <td><label>${imageEdit_url} </label></td>
            <td>
                <a href="<c:url value="/gallery/${login}/${album.name}/${image.name}/full"/>">
                    <c:url value="${requestURL}/full"/><br/>
                    <%--<c:url value="${pageContext.request.serverName}/gallery/${login}/${album.name}/${image.name}/full"/><br/>--%>
                </a>
            </td>
        </tr>
        <tr>
            <td><sf:label path="name">${imageEdit_name} </sf:label></td>
            <td><sf:input path="name" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="name"/></td>
        </tr>
        <tr>
            <td><sf:label path="description">${imageEdit_desc} </sf:label></td>
            <td><sf:input path="description" disabled="${disabledEdit}"/></td>
            <td><sf:errors path="description"/></td>
        </tr>
        <tr>
            <td>${imageEdit_album} </td>
            <td>
                <sf:select path="album" disabled="${disabledEdit}">
                    <sf:options items="${albumList}"/>
                </sf:select>
            </td>
            <td><sf:errors path="album" cssClass="error" /></td>
        </tr>
        <tr>
            <td><sf:label path="size">${imageEdit_size} </sf:label></td>
            <td><fmt:formatNumber type="number" maxFractionDigits="3" value="${image.size}" />B</td>
        </tr>
        <tr>
            <td><sf:label path="createDate">${createDate} </sf:label></td>
            <td><fmt:formatDate value="${image.createDate}" pattern="${dateFormatPattern}"/>   </td>
        </tr>

        <c:if test="${disabledEdit=='false'}">
            <tr>
                <td>${imageEdit_title} </td>
                <td><sf:checkbox path="checkDefForAlbum"/></td>
                <td><sf:errors path="checkDefForAlbum" cssClass="error" /></td>
            </tr>

            <tr>
                <td>
                    <a href="<c:url value="/gallery/${login}/${album.name}/${image.name}?delete"/>">
                        ${imageEdit_delete}
                    </a>
                </td>
            </tr>

            <tr>
                <td>
    <input name="commit" type="submit" value="${imageEdit_save}"/>
    <security:csrfInput/>
                </td>
                <td>
                    <input type="button" class="back-button" onclick="history.back();" value="<spring:message code="button.back" />"/>
                </td>
            </tr>

        </c:if>
    </table>

</sf:form>

