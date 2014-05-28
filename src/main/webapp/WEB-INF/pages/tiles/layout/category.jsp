<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <b>Admin menu:</b>
    <br/>
    <ul>
        <li><a href="<c:url value="/user/"/>">users</a></li>
    </ul>
</security:authorize>
