<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <b>Admin menu:</b>
    <br/>
    <ul>
        <li><a href="<c:url value="/user/"/>">users</a></li>
        <li>test:</li>
        <ul>
            <li><a href="<c:url value="/test/views"/>">views</a></li>
        </ul>
    </ul>
</security:authorize>

<security:authorize access="isAuthenticated()">
    <c:set var="login"><security:authentication property="principal.username" /></c:set>
    <p>
        <b>Your menu:</b>
    </p>
    <p>
        <ul>
            <li>
                <a href="<c:url value="/gallery/${login}/"/>">gallery</a>
            </li>
            <li>
                <a href="<c:url value="/article/"/>">articles</a>
            </li>
        </ul>
    </p>
</security:authorize>
