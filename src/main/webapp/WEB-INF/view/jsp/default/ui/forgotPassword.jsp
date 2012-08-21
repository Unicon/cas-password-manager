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
<jsp:directive.page import="net.tanesha.recaptcha.ReCaptcha" />
<jsp:directive.page import="net.tanesha.recaptcha.ReCaptchaFactory" />
<c:set var="recaptchaPublicKey" scope="page" value="${recaptchaPublicKey}"/>
<c:set var="recaptchaPrivateKey" scope="page" value="${recaptchaPrivateKey}"/>

<c:url value="/login" var="formActionUrl" />

<div id="admin" class="useradmin">
    <form:form method="post" action="${formActionUrl}" cssClass="fm-v clearfix" modelAttribute="netIdBean">
   
        <h2><spring:message code="pm.forgotPassword.header" /></h2>
        <c:if test="${not empty forgotPasswordValidationError}">
        <div class="errors" style="width:250px;">
            <spring:message code="pm.forgotPassword.generic-error" />
        </div>
        </c:if>
        <p class="note"><spring:message code="pm.forgotPassword.heading-text" /></p>
        <div class="row fl-controls-left">
            <label class="fl-label" for="username"><spring:message code="pm.form.netid" /></label>
            <spring:message code="pm.form.netid.accesskey" var="netIdAccessKey" />
            <form:input path="netId" type="text" autocomplete="false" size="25" accesskey="${netIdAccessKey}" tabindex="1" cssClass="required" id="netId"/>
            <form:errors path="netId" cssClass="error"/>
        </div>
        
        <div>
            <label class="fl-label" for="recaptcha"><spring:message code="pm.recaptcha.prompt" /></label>
            <%
            ReCaptcha c = ReCaptchaFactory.newSecureReCaptcha((String)pageContext.getAttribute("recaptchaPublicKey"),
                    (String)pageContext.getAttribute("recaptchaPrivateKey"), true);
            out.print(c.createRecaptchaHtml(null, null));
            %>
        </div>
        
        <div class="row btn-row">
            <input type="hidden" name="execution" value="${flowExecutionKey}"/>
            <input type="hidden" name="_eventId" value="submitId">
            <input type="hidden" name="lt" value="${loginTicket}"/>
            <input type="submit" tabindex="2" value="<spring:message code="pm.form.submit" />" accesskey="<spring:message code="pm.form.submit.accesskey" />" name="submit" class="btn-submit">
            <input type="reset" tabindex="3" value="<spring:message code="pm.form.clear" />" accesskey="<spring:message code="pm.form.clear.accesskey" />" name="reset" class="btn-reset">
        </div>
    
    </form:form>
</div>

<jsp:directive.include file="includes/bottom.jsp" />