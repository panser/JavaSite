<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf8" %>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<html>
<head>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <!-- Latest compiled and minified JavaScript -->
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

    <style type="text/css">
        /*body { margin: 0; padding: 0; }*/
        textarea{
            width: 30%;
            /*min-width: 20%;*/
            height: 100px;
        }
        .depth1{
            margin-left: 40px;
        }
        .depth2{
            margin-left: 80px;
        }
        .depth3{
            margin-left: 120px;
        }
        .depth4{
            margin-left: 160px;
        }
        .comment-block li{
            display: block;
        }
    </style>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

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