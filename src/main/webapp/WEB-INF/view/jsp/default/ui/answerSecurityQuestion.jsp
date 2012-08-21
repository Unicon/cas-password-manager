<%--
  ~ Licensed to Jasig under one or more contributor license
  ~ agreements. See the NOTICE file distributed with this work
  ~ for additional information regarding copyright ownership.
  ~ Jasig licenses this file to you under the Apache License,
  ~ Version 2.0 (the "License"); you may not use this file
  ~ except in compliance with the License.  You may obtain a
  ~ copy of the License at the following location:
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>

<jsp:directive.include file="includes/top.jsp" />
<link type="text/css" rel="stylesheet" href="<c:url value="/css/cas-pm.css" />" />
<c:url var="formActionUrl" value="/login" />
<div id="admin" class="useradmin">
    <form method="post" action="${formActionUrl}" class="fm-v clearfix" id="">
        <h2><spring:message code="pm.answerSecurityQuestion.header" /></h2>
        <c:if test="${not empty securityQuestionValidationError}">
        <div class="errors" style="width:250px;">
            <spring:message code="pm.answerSecurityQuestion.error" />
        </div>
        </c:if>
        <!-- Verify your identity by answering the security question. -->
        <p class="note"><spring:message code="pm.answerSecurityQuestion.heading-text" /></p>
        <c:forEach items="${securityChallenge.questions}" var="question" varStatus="status">
	        <p class="sec_question"><c:out value="${question.questionText}"/></p>
	        <div class="row fl-controls-left">
	            <label class="fl-label" for="username"><spring:message code="pm.answerSecurityQuestion.prompt.answer" /></label>
	            <input type="text" autocomplete="false" size="25" value="" accesskey="n" tabindex="1" class="required" name="response${status.index}" id="username">
	        </div>
	    </c:forEach>
        <div class="row btn-row">
            <input type="hidden" value="submitAnswer" name="_eventId"/>
            <input type="hidden" name="lt" value="${loginTicket}"/>
            <input type="hidden" name="execution" value="${flowExecutionKey}"/>
            <input type="submit" tabindex="2" value="<spring:message code="pm.form.submit" />" accesskey="<spring:message code="pm.form.submit.accesskey" />" name="submit" class="btn-submit">
            <input type="reset" tabindex="3" value="<spring:message code="pm.form.clear" />" accesskey="<spring:message code="pm.form.clear.accesskey" />" name="reset" class="btn-reset">
        </div>
    </form>
</div>
<jsp:directive.include file="includes/bottom.jsp" />