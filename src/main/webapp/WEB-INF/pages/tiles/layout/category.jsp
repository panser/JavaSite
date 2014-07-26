<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<spring:message code="category.adminMenu" var="category_adminMenu"/>
<spring:message code="category.adminMenu.users" var="category_adminMenu_users"/>
<spring:message code="category.adminMenu.test" var="category_adminMenu_test"/>
<spring:message code="category.yourMenu" var="category_yourMenu"/>
<spring:message code="category.yourMenu.articles" var="category_yourMenu_articles"/>
<spring:message code="category.yourMenu.myGallery" var="category_yourMenu_myGallery"/>
<%--<spring:message code="" var=""/>--%>


<security:authorize access="hasRole('ROLE_ADMIN')">
    <b>${category_adminMenu}</b>
    <br/>
    <ul>
        <li><a href="<c:url value="/user/"/>">${category_adminMenu_users}</a></li>
        <li>${category_adminMenu_test}</li>
        <ul>
            <li><a href="<c:url value="/test/views"/>">views</a></li>
            <li><a href="<c:url value="/test/ajax"/>">ajax</a></li>
        </ul>
    </ul>
</security:authorize>

<p>
    <b>${category_yourMenu}</b>
</p>
<p class="sidebar-menu">
<ul>
    <li>
        <a href="<c:url value="/article/"/>">${category_yourMenu_articles}</a>
    </li>
</ul>
</p>


<security:authorize access="isAuthenticated()">
    <c:set var="login"><security:authentication property="principal.username" /></c:set>
    <p class="sidebar-menu">
        <ul>
    <li>
        <a href="<c:url value="/gallery/${login}/"/>">${category_yourMenu_myGallery}</a>
    </li>
        </ul>
    </p>
</security:authorize>

