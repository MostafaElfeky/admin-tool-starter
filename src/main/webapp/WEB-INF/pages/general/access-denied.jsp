<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>


<section id="wrapper" class="error-page">
	<div class="error-box">
		<div class="error-body text-center">
			<h3 class="text-uppercase">غير مسموح</h3>
			<p class="text-muted m-t-30 m-b-30">يبدو انك غير مسموح لك بالولوج
				لهذا المحتوى</p>
			<a href="${pageContext.request.contextPath}/"
				class="btn btn-info btn-rounded waves-effect waves-light m-b-40">الرئيسية</a>
		</div>
	</div>
</section>