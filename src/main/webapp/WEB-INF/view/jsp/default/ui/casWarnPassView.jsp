<jsp:directive.include file="includes/top.jsp" />
<c:set var="ticketArg"  value="${serviceTicketId}" scope="page"/>
<c:if test="${fn:length(ticketArg) > 0}">
  <c:set var="ticketArg"  value="ticket=${serviceTicketId}"/>
</c:if>
<c:url var="changePasswordUrl" value="/login">
  <c:param name="execution" value="${flowExecutionKey}"/>
  <c:param name="_eventId" value="changePassword"/>
</c:url>
<c:url var="ignoreUrl" value="/login">
  <c:param name="execution" value="${flowExecutionKey}"/>
  <c:param name="_eventId" value="ignore"/>
</c:url>

<div class="errors">
  <p>
    <c:if test="${expireDays == 0}">
      <h2><spring:message code="screen.warnpass.heading.today" /></h2>
    </c:if>
    <c:if test="${expireDays == 1}">
      <h2><spring:message code="screen.warnpass.heading.tomorrow" /></h2>
    </c:if>
    <c:if test="${expireDays > 1}">
      <h2><spring:message code="screen.warnpass.heading.other" arguments="${expireDays}" /></h2>
    </c:if>
  </p>

  <c:choose>
    <c:when test="${empty passwordPolicyUrl}">
      <p>
        <spring:message code="screen.warnpass.message.line1" arguments="${changePasswordUrl}" />
      </p>
      <p>
        <spring:message code="screen.warnpass.message.line2" arguments="${ignoreUrl}" />
      </p>
    </c:when>
    <c:otherwise>
      <p>
        <spring:message code="screen.warnpass.message.line1" arguments="${passwordPolicyUrl}" />
      </p>
      <p>
        <spring:message code="screen.warnpass.message.line2" arguments="${fn:escapeXml(param.service)}${fn:indexOf(param.service, '?') eq -1 ? '?' : '&'}${ticketArg}" />
      </p>
    </c:otherwise>
  </c:choose>
</div>
<script type="text/javascript">
<!--

  function redirectTo(URL) {
    window.location = URL ;
  }
  setTimeout("redirectTo('${param.service}${fn:indexOf(param.service, '?') eq -1 ? '?' : '&'}${ticketArg}')", 10000);

</script>
