<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8" %>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>

<c:if test="${!ajaxRequest}">
    <html>
    <head>

        <link rel='stylesheet' type='text/css' href='<c:url value="/webjars/bootstrap/3.1.1/css/bootstrap.css"/>'>
        <link rel='stylesheet' type='text/css' href='<c:url value="/webjars/jquery-ui/1.10.4/themes/base/jquery.ui.all.css"/>'>

        <link rel='stylesheet' type='text/css' href="<c:url value="/resources/css/thisProject.css"/>">
        <link rel='stylesheet' type='text/css' href="<c:url value="/resources/css/bootstrap_diana.css"/>">



        <script src="<c:url value="/webjars/jquery/2.1.1/jquery.js"/>"></script>
        <script src="<c:url value="/webjars/jquery-ui/1.10.4/ui/jquery-ui.js"/>"></script>
        <script src="<c:url value="/webjars/jquery-form/3.28.0-2013.02.06/jquery.form.js"/>"></script>
        <script src="<c:url value="/webjars/ckeditor/4.4.1/ckeditor.js"/>"></script>
        <script src="<c:url value="/webjars/ckeditor/4.4.1/adapters/jquery.js"/>"></script>


        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>

        <title><tiles:insertAttribute name="title" ignore="true"/></title>


    </head>
    <body>

    <div id="container" class="container-fluid">

    <!--Header-->
        <div id="header">
            <tiles:insertAttribute name="header"/>
        </div>

    <!--Login form-->
        <div id="sigin">
            <tiles:insertAttribute name="sigin"/>
        </div>

    <!--Navigation Bar-->
        <div id="menu">
            <tiles:insertAttribute name="menu"/>
        </div>
</c:if>

    <!--Body content-->
<div class="container">
        <div id="content" class="content">
            <tilesx:useAttribute id="list" name="body" classname="java.util.List" />
            <c:forEach var="item" items="${list}">
                <tiles:insertAttribute value="${item}" flush="true" />
                <br/>
            </c:forEach>
        </div>

<!--Sidebar content-->
<c:if test="${!ajaxRequest}">
        <div id="category" class="sidebar">
            <tiles:insertAttribute name="category"/>
        </div>

    <div class="page-buffer"></div>
    </div>

<!-- Footer -->
        <div id="footer">
            <tiles:insertAttribute name="footer" />
        </div>


 </div>
    </body>
    </html>
</c:if>
<%--
<script type="text/javascript">
    $(document).ready(function(){
        // Include CSRF token as header in JQuery AJAX requests
        // See http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/#csrf-include-csrf-token-ajax
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    });
</script>--%>
