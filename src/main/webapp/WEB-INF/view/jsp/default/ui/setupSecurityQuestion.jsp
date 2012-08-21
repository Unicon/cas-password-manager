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
    <form:form method="post" action="${formActionUrl}" modelAttribute="securityQuestion" cssClass="fm-v clearfix">

        <h2><spring:message code="pm.setupSecurityQuestion.header" /></h2>
        <p class="note"><spring:message code="pm.setupSecurityQuestion.heading-text" /></p>
        <div class="row fl-controls-left">
            <label class="fl-label" for="username"><spring:message code="pm.form.netid" /></label>
            <input type="text" disabled="disabled" autocomplete="false" size="25" value="${username}" accesskey="<spring:message code="pm.form.netid.accesskey" />" tabindex="1" class="required" name="username" id="username"/>
        </div>
        <div class="row fl-controls-left">
            <label class="fl-label" for="questionText"><spring:message code="pm.setupSecurityQuestion.prompt.question" /></label>
            <spring:message code="pm.setupSecurityQuestion.prompt.question.accesskey" var="setupQuestionAccessKey"/>
            <form:input path="questionText" type="text" autocomplete="off" size="25" value="" accesskey="${setupQuestionAccessKey}" tabindex="2" cssClass="required" id="questionText"/>
            <form:errors path="questionText" cssClass="error"/>
        </div>
        <div class="row fl-controls-left">
            <label class="fl-label" for="responseText"><spring:message code="pm.setupSecurityQuestion.prompt.answer" /></label>
            <spring:message code="pm.setupSecurityQuestion.prompt.answer.accesskey" var="answerAccessKey"/>
            <form:input path="responseText" type="text" autocomplete="off" size="25" value="" accesskey="${answerAccessKey}" tabindex="3" cssClass="required" id="responseText"/>
            <form:errors path="responseText" cssClass="error"/>
        </div>
        <div class="row btn-row">
            <input type="hidden" name="lt" value="${loginTicket}"/>
            <input type="hidden" name="_eventId" value="setupSubmit"/>
            <input type="hidden" name="execution" value="${flowExecutionKey}"/>
            <input type="submit" tabindex="5" value="<spring:message code="pm.form.submit" />" accesskey="<spring:message code="pm.form.submit.accesskey" />" name="submit" class="btn-submit">
            <input type="reset" tabindex="6" value="<spring:message code="pm.form.clear" />" accesskey="<spring:message code="pm.form.clear.accesskey" />" name="reset" class="btn-reset">
        </div>
    
    </form:form>
</div>

<jsp:directive.include file="includes/bottom.jsp" />
