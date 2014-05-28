<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8" %>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<html>
<head>
    <title><tiles:insertAttribute name="title" ignore="true"/></title>
    <style type="text/css">
        /*body { margin: 0; padding: 0; }*/
        textarea{
            width: 30%;
            /*min-width: 20%;*/
            height: 100px;
        }
    </style>
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

    <div id="content" style="background-color:#eeeeee;width:90%;float:left;">
        <tiles:insertAttribute name="body"/>
    </div>
    <div id="category" style="background-color:lightcyan;width:10%;float:left;">
        <tiles:insertAttribute name="category"/>
    </div>

    <div id="footer" style="background-color:lightcyan;clear:both;text-align:center;">
        <tiles:insertAttribute name="footer"/>
    </div>

</div>

</body>
</html>