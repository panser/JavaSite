<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div page="${page}" class="page">
    <c:forEach items="${images}" var="image">
        <c:if test="${image.album.publicAccess}">
            <a href="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}"/>">
                <img src="<c:url value="/gallery/${image.user.login}/${image.album.name}/${image.name}/full"/>" width="240" height="240"/>
            </a>
        </c:if>
    </c:forEach>
</div>
<input id="pagecount" type="hidden" value="1"/>
<div id="nextId">
    <a class="next" href="?page=">Next Page</a>
    <ul>
        <li><a href="?page=0">Page 0</a></li>
        <li><a href="?page=1">Page 1</a></li>
        <li><a href="?page=2">Page 2</a></li>
        <li><a href="?page=3">Page 3</a></li>
    </ul>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $(window).scroll(function () {
            scrollWindow();
        });

        function scrollWindow() {
            console.log($(window).scrollTop() + ">="+ $(document).height() + "-" + ($(window).height()) + " && " + document.location.toString().indexOf("endOfPage") + "== -1");
            if (($(window).scrollTop() >= $(document).height() - $(window).height())) {
                var pagecount = $('#pagecount').val();
                loadNewData(pagecount);
            }
        }

        function loadNewData (pagecount){
            console(pagecount);
        }

        var obj = $('div.page').find('LAST');
        var index = $(obj).attr('page');

        $("a.next").click(function(){
            var link = $(this);
            $.ajax({ url: link.attr("href"), dataType: "text",
                success: function(text) {
//                    MvcUtil.showSuccessResponse(text, link);
                },
                error: function(xhr) {
//                    MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });


    });
</script>