<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="disabledEdit">false</c:set>
<div>
    <c:forEach items="${images}" var="image">
        <c:if test="${image.album.publicAccess}">
            <a href="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}"/>">
                <img src="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}/full"/>" width="240" height="240"/>
            </a>
        </c:if>
    </c:forEach>
</div>
<div id="nextId">
    <a href="next">Next Page</a>
    <ul>
        <li><a href="?page=0">Page 0</a></li>
        <li><a href="?page=1">Page 1</a></li>
        <li><a href="?page=2">Page 2</a></li>
        <li><a href="?page=3">Page 3</a></li>
    </ul>
</div>
