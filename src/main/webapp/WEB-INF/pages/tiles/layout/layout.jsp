<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8" %>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${!ajaxRequest}">
    <html>
    <head>
        <link href="<c:url value="/resources/css/thisProject.css"/>" rel="stylesheet">

        <link rel='stylesheet' href='<c:url value="/webjars/bootstrap/3.1.1/css/bootstrap.min.css"/>'>
        <link rel='stylesheet' href='<c:url value="/webjars/jquery-ui/1.10.4/themes/base/jquery.ui.all.css"/>'>
        <script src="<c:url value="/webjars/jquery/2.1.1/jquery.js"/>"></script>
        <script src="<c:url value="/webjars/jquery-ui/1.10.4/ui/jquery-ui.js"/>"></script>
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

    <div id="container" style="width:100%">

        <div id="header" style="background-color:lightcyan;height:100px;width:90%;float:left;">
            <tiles:insertAttribute name="header"/>
        </div>
        <div id="sigin" style="background-color:lightcyan;height:100px;width:10%;float:left;">
            <tiles:insertAttribute name="sigin"/>
        </div>

        <div id="menu" style="background-color:green;width:100%;height:30px;float:left;">
            <tiles:insertAttribute name="menu"/>
        </div>
</c:if>

        <div id="content" style="background-color:#eeeeee;width:90%;float:left;">
            <tiles:insertAttribute name="body"/>
        </div>

<c:if test="${!ajaxRequest}">
        <div id="category" style="background-color:lightcyan;width:10%;float:left;">
            <tiles:insertAttribute name="category"/>
        </div>

        <div id="footer" style="background-color:lightcyan;clear:both;text-align:center;">
            <tiles:insertAttribute name="footer"/>
        </div>

    </div>

    </body>
    </html>
</c:if>