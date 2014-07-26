<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<spring:message code="menu.articles" var="menu_articles"/>
<spring:message code="menu.gallery" var="menu_gallery"/>
<%--<spring:message code="" var=""/>--%>

<div id="article" class="navbar">
    <div class="navbar-inner">
        <ul class="nav nav-pills">
            <li><a href="<c:url value="/article/"/>">${menu_articles}</a></li>
            <li><a href="<c:url value="/gallery/"/>">${menu_gallery}</a></li>
        </ul>
    </div>
</div>
