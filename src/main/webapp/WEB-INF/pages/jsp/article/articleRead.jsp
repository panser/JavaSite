<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="enabledEdit">false</c:set>
<security:authorize access="isAuthenticated()">
    <c:set var="username"><security:authentication property="principal.username"/></c:set>
    <c:if test="${article.author.login == username}" var="enabledEdit" />
</security:authorize>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="enabledEdit">true</c:set>
</security:authorize>

<p>
    <b>${article.title}</b>
</p>
<p>
    ${article.description}
</p>
<p>
    ${article.text}
</p>
<p>
    <div id="date" style="width:50%;float:left;color:coral">
        <label>Create date: </label>
        <fmt:formatDate value="${article.createDate}" type="both" dateStyle="short" timeStyle="short"/>
    </div>
    <div id="countUniqueVisitors" style="width:25%;float:right;color:coral">
        <label>Unique visitors: </label>
        ${countUniqueVisitors}
    </div>
    <div id="countVisitors" style="width:25%;float:right;color:coral">
        <label>Views: </label>
        ${countVisitors}
    </div>
</p>

<c:if test="${enabledEdit}">
    <td><a href="<c:url value="/article/${article.id}/edit"/>">Edit</a></td>
    <td><a href="<c:url value="/article/${article.id}/delete"/>">Delete</a></td>
</c:if>

<br/>
<br/>
<br/>
<div id="comments">
    <%--<div id="addCommentDiv">--%>
    <div id="addCommentDiv" hidden="true">
        <fieldset>
        <c:set var="commentPutUrl"><c:url value="${article.id}/comment"/></c:set>
        <sf:form method="post" modelAttribute="comment" action="${commentPutUrl}?parentComment=${parentComment}" parent="${parentComment}">
            <h2>Add Comment</h2>

            <ul class="comment-block">
                <li>
                <sf:label path="name">Name: </sf:label>
                <sf:input path="name" id="name"/>
                <sf:errors path="name"/>
                </li>
                <li>
                <sf:label path="email">Email: </sf:label>
                <sf:input path="email" id="email"/>
                <sf:errors path="email"/>
                </li>
                <li>
                <sf:label path="text">Comment: </sf:label>
                <sf:textarea path="text" id="text"/>
                <sf:errors path="text"/>
                </li>
            </ul>

            <input name="commit" type="submit" value="Send Comment"/>
            <security:csrfInput />
        </sf:form>
        </fieldset>
    </div>

    <input type="submit" id="addComment" value="Add Comment" commentIndex="0"/><br/>

    <b>Comments:</b>
    <c:forEach items="${comments}" var="comment">
        <%--<fieldset>--%>
        <fieldset class="depth${comment.depth} parent-${comment.parent.id}">
            <div class="comment comment-${comment.id}">
                <p>
                    <div id="nameComment" style="width:50%;float:left;">
                        <label>Name: </label>
                        ${comment.name}
                    </div>
                    <div id="dateComment" style="width:50%;float:right;">
                        <label>Date: </label>
                            <fmt:formatDate value="${comment.createDate}" type="both" dateStyle="short" timeStyle="short"/>
                    </div>
                </p>
                <p>
                    ${comment.text}
                </p>
                </div>
                <input type="submit" id="addComment" value="Add Comment" commentIndex="${comment.id}"/><br/><br/>
        </fieldset>
    </c:forEach>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        var error = hasValidError();
        $("input[id*='addComment']").click(function() {
                    $('div#addCommentDiv').hide();
                    var index = $(this).attr('commentIndex');
                    var html = $("#addCommentDiv").clone();
                    var attrAction = $(html.find('#comment')).attr('action');
                    if(!error){
                        $(html.find('#comment')).attr('action', attrAction+index);
                        $(html.find('#comment')).attr('parent', index);
                    }
                    $(html).show();

                    $(this).after(html);
                }
        );
        if(error){
            var parent = $('#addCommentDiv').find('form[id="comment"]').attr('parent');
            $('input[type="submit"][commentindex="'+parent+'"]').click();
        }
    });

    function hasValidError(){
        var result = false;
        var objs = $('#addCommentDiv').find('span[id*="errors"]');
        $(objs).each(function(){
            if($(this).text() != ""){
                result =  true;
            }
        });

        return result;
    }
</script>