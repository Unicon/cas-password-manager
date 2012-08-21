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
    <form:form method="post" action="${formActionUrl}" class="fm-v clearfix" modelAttribute="changePasswordBean">
   
        <!-- Congratulations on bringing CAS online!  The default authentication handler authenticates where usernames equal passwords: go ahead, try it out.  -->
        <c:choose>
            <c:when test="${pwdChangeForced}">
                <h2><spring:message code="pm.changePassword.header.forced" /></h2>
            </c:when>
            <c:otherwise>
                <h2><spring:message code="pm.changePassword.header" /></h2>
            </c:otherwise>
        </c:choose>
        
        <c:if test="${not empty changePasswordValidationError}">
        <div class="errors" style="width:250px;">
            <spring:message code="pm.changePassword.generic-error" />
        </div>
        </c:if>
        
        <p class="note">
        <c:choose>
            <c:when test="${pwdChangeForced}">
                <spring:message code="pm.changePassword.heading-text.forced" />
            </c:when>
            <c:otherwise>
                <spring:message code="pm.changePassword.heading-text" />
                <c:if test="${flowRequestContext.currentState.id != 'setPassword'}">        
                    <spring:message code="pm.changePassword.heading-text.enter-current-pwd" />
                </c:if>
            </c:otherwise>
        </c:choose>
        </p>
        
        <div class="row fl-controls-left">
            <label class="fl-label" for="username"><spring:message code="pm.form.netid" /></label>
            <c:choose>
                <c:when test="${username != null}">
                    <form:input path="username" type="text" disabled="true" autocomplete="false" size="25" value="${username}" accesskey="n" tabindex="1" cssClass="required" id="username" />
                    <form:errors path="username" cssClass="error"/>
                </c:when>
                <c:otherwise>
                    <spring:message code="pm.form.netid.accesskey" var="netIdAccessKey" />
                    <form:input path="username" type="text" autocomplete="false" size="25" accesskey="${netIdAccessKey}" tabindex="1" cssClass="required" name="username" id="username" />
                    <form:errors path="username" cssClass="error"/>
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${flowRequestContext.currentState.id != 'setPassword'}">
        <div class="row fl-controls-left">
            <label class="fl-label" for="oldPassword"><spring:message code="pm.changePassword.form.password.old" /></label>
            <spring:message code="pm.changePassword.form.password.old.accesskey" var="oldPasswordAccessKey" />
            <form:input path="oldPassword" type="password" autocomplete="off" size="25" accesskey="${oldPasswordAccessKey}" tabindex="2" cssClass="required" id="oldPassword" />
            <form:errors path="oldPassword" cssClass="error"/>	            
        </div>
        </c:if>
        <div class="row fl-controls-left">
            <label class="fl-label" for="newPassword"><spring:message code="pm.changePassword.form.password.new" /></label>
            <spring:message code="pm.changePassword.form.password.new.accesskey" var="newPasswordAccessKey" />
            <form:input path="newPassword" type="password" autocomplete="off" size="25" accesskey="${newPasswordAccessKey}" tabindex="3" cssClass="required" id="newPassword" />
            <form:errors path="newPassword" cssClass="error"/>
        </div>
        <div class="row fl-controls-left">
            <label class="fl-label" for="confirmNewPassword"><spring:message code="pm.changePassword.form.password.confirm" /></label>
            <spring:message code="pm.changePassword.form.password.confirm.accesskey" var="confimAccessKey" />
            <form:input path="confirmNewPassword" type="password" autocomplete="off" size="25" accesskey="${confirmAccessKey}" tabindex="4" cssClass="required" id="confirmNewPassword" />
            <form:errors path="confirmNewPassword" cssClass="error"/>
        </div>
        <c:if test="${empty username}">
            <div>
                <label class="fl-label" for="recaptcha"><spring:message code="pm.recaptcha.prompt" /></label>
                <%
                    ReCaptcha c = ReCaptchaFactory.newSecureReCaptcha((String)pageContext.getAttribute("recaptchaPublicKey"),
                    		(String)pageContext.getAttribute("recaptchaPrivateKey"), true);
                    out.print(c.createRecaptchaHtml(null, null));
                %>
            </div>
        </c:if>
        <div class="row btn-row">
            <input type="hidden" value="${loginTicket}" name="lt">
            <input type="hidden" value="submitChangePassword" name="_eventId">
            <input type="hidden" value="${flowExecutionKey}" name="execution">
            <input type="submit" tabindex="5" value="<spring:message code="pm.form.submit" />" accesskey="<spring:message code="pm.form.submit.accesskey" />" name="submit" class="btn-submit">
            <input type="reset" tabindex="6" value="<spring:message code="pm.form.clear" />" accesskey="<spring:message code="pm.form.clear.accesskey" />" name="reset" class="btn-reset">
        </div>
    
    </form:form>
</div>

<jsp:directive.include file="includes/bottom.jsp" />