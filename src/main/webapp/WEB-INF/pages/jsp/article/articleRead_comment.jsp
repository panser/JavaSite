<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="date_format_pattern_long" var="dateFormatPattern"/>
<spring:message code="articleRead_comment.add" var="articleRead_comment_add"/>
<spring:message code="articleRead_comment.name" var="articleRead_comment_name"/>
<spring:message code="articleRead_comment.addEmail" var="articleRead_comment_addEmail"/>
<spring:message code="articleRead_comment.addText" var="articleRead_comment_addText"/>
<spring:message code="articleRead_comment.addSubbmit" var="articleRead_comment_addSubbmit"/>
<spring:message code="articleRead_comment.comments" var="articleRead_comment_comments"/>
<spring:message code="articleRead_comment.commentsDate" var="articleRead_comment_commentsDate"/>
<%--<spring:message code="" var=""/>--%>


<div id="comments">
    <div id="addCommentDiv" hidden="true">
        <fieldset>
            <c:set var="commentPutUrl"><c:url value="${article.id}/comment"/></c:set>
            <sf:form method="post" modelAttribute="comment" action="${commentPutUrl}?parentComment=${parentComment}" parent="${parentComment}">
                <h2>${articleRead_comment_add}</h2>

                <ul class="comment-block">
                    <li>
                        <sf:label path="name">${articleRead_comment_name} </sf:label>
                        <sf:input path="name" id="name"/>
                        <sf:errors path="name"/>
                    </li>
                    <li>
                        <sf:label path="email">${articleRead_comment_addEmail} </sf:label>
                        <sf:input path="email" id="email"/>
                        <sf:errors path="email"/>
                    </li>
                    <li>
                        <sf:label path="text">${articleRead_comment_addText} </sf:label>
                        <sf:textarea path="text" id="text"/>
                        <sf:errors path="text"/>
                    </li>
                </ul>

                <input name="commit" type="submit" value="${articleRead_comment_addSubbmit}"/>
                <security:csrfInput />
            </sf:form>
        </fieldset>
    </div>

    <input type="submit" id="addComment1" value="${articleRead_comment_add}" commentIndex="0"/><br/>

    <b>${articleRead_comment_comments}</b>
    <c:forEach items="${comments}" var="comment">
        <%--<fieldset>--%>
        <fieldset class="depth${comment.depth} parent-${comment.parent.id}">
            <div class="comment comment-${comment.id}">
                <p>
                <div id="nameComment" style="width:50%;float:left;">
                    <label>${articleRead_comment_name} </label>
                    ${comment.name}
                </div>
                <div id="dateComment" style="width:50%;float:right;">
                    <label>${articleRead_comment_commentsDate} </label>
                    <fmt:formatDate value="${comment.createDate}" pattern="${dateFormatPattern}"/>
                </div>
                </p>
                <p>
                    ${comment.text}
                </p>
            </div>
            <input type="submit" id="addComment2" value="${articleRead_comment_add}" commentIndex="${comment.id}"/><br/><br/>
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