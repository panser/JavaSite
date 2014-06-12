<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<p>
    <b>Test Views:</b>
</p>

<p>
    <ul>
        <li>
            <a href="<c:url value="/article/list.xml"/>">XML</a>
        </li>
        <li>
            <a href="<c:url value="/article/list.json"/>">JSON</a>
        </li>
    <li>
        <a href="<c:url value="/article/list.atom"/>">ATOM</a>
    </li>
    </ul>
</p>

</body>
</html>
