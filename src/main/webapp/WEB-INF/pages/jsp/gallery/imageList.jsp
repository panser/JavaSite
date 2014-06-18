<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="errorsUpload">
    <font color="red">
        <c:forEach items="${errors}" var="entry">
            <p>
                ${entry.key}: ${entry.value}
            </p>
        </c:forEach>
    </font>
</div>
<div id="fileuploadContent">
    <c:forEach items="${images}" var="image">
        <a href="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}"/>">
        <img src="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}/full"/>" width="240" height="240"/>
        </a>
    </c:forEach>
</div>