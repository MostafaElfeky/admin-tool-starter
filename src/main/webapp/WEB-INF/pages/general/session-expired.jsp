<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>
 
<section id="wrapper" class="error-page">
 <div class="error-box">
  <div class="error-body text-center">
   <h3 class="text-uppercase"><spring:message code="sessionexpired.title" /></h3>
   <p class="text-muted m-t-30 m-b-30"><spring:message code="sessionexpired.message" /></p>
   <a href="${pageContext.request.contextPath}/" class="btn btn-info btn-rounded waves-effect waves-light m-b-40">
   <spring:message code="logbackin" /></a>
  </div>
 </div>
</section>