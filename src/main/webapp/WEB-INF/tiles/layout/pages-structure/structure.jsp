<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="rtl">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" errorPage="/WEB-INF/pages/error.jsp">
<title><tiles:getAsString name="title" /></title>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Tell the browser to be responsive to screen width -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<meta http-equiv="refresh" content="<%=session.getMaxInactiveInterval()%>;url=${pageContext.request.contextPath}/sessionexpired"/>

<!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/assets/images/favicon.png">


<tiles:insertAttribute name="app-styles" />
<tiles:insertAttribute name="app-scripts" />

<script id="resultSuccess" type="text/template">
<div class="alert alert-success alert-dismissible">
	<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	<strong>  نجح! </strong> {{message}}
</div>
</script>

<script id="resultFailure" type="text/template">
<div class="alert alert-danger alert-dismissible">
	<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	<strong>  خطأ! </strong> {{message}}
</div>
</script>
<script id="no-result-found" type="text/template">
<tr>
	<td colspan="20" class="p-0">
		<div class="card text-center m-b-0">
			<div class="card-body p-40" >
				<h4 class="card-title">لا يوجد بيانات</h4>
			</div>
		</div>
	</td>
</tr>
</script>

<script type="text/javascript">

var wiseLang = "${pageContext.response.locale}";
var userId = 1;
Dropzone.prototype.defaultOptions.dictRemoveFile = "أحذف";
Dropzone.prototype.defaultOptions.dictCancelUpload = "إلغاء";
Dropzone.prototype.defaultOptions.dictResponseError = "لقد تخطيت الحجم المسموح";
var headers = {
	"os" : "3",
	"key" : "md5",
	"version" : "1.0",
	"userId" : userId
};



jQuery(window).load(function () {
	$(".preloader").fadeOut();
});


function formatDate() {
	
	moment.locale('ar-sa');
	
	$(".formatDate").each(function (idx) {
      	if(moment( $( this ).text(), ['x'] ).isValid()) {
      		$( this ).text(moment( $( this ).text(), 'x' ).format("DD MMM YYYY"));
      	}
   });
	$(".formatCustomDate").each(function (idx) {
        if ($( this ).is(":input")) {
        	if(moment( $( this ).text(), ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD HH:mm:ss.S'], true ).isValid()) {
        		$( this ).val(moment( $( this ).val(), 'YYYY-MM-DD hh:mm:ss' ).format("DD MMM YYYY"));
        	}
        } else {
        	if(moment( $( this ).text(), ['YYYY-MM-DD HH:mm:ss', 'YYYY-MM-DD HH:mm:ss.S'], true ).isValid()) {
        		$( this ).text(moment( $( this ).text(), 'YYYY-MM-DD hh:mm:ss' ).format("DD MMM YYYY"));
        	}
        }
    });
}


function formatDateFromNow(locale) {
	
	if(locale == 'ar') {
		moment.locale('ar-sa');
	} else {
		moment.locale('en');
	}
	
	$(".formatDateFromNow").each(function (idx) {
        if ($( this ).is(":input")) {
        	if(moment( $( this ).text(), 'YYYY-MM-DD hh:mm:ss' ).isValid()) {
        		$( this ).val(moment( $( this ).val(), 'YYYY-MM-DD hh:mm:ss' ).fromNow());
        	}
        } else {
        	if(moment( $( this ).text(), 'YYYY-MM-DD hh:mm:ss' ).isValid()) {
        		$( this ).text(moment( $( this ).text(), 'YYYY-MM-DD hh:mm:ss' ).fromNow());
        	}
        }
    });
}

</script>

</head>
<body class="fix-header card-no-border">

 <!-- Preloader - style you can find in spinners.css -->
  <div class="preloader">
      <svg class="circular" viewBox="25 25 50 50">
          <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" /> </svg>
  </div>


 <div id="main-wrapper">
  <tiles:insertAttribute name="topbar" />
  <tiles:insertAttribute name="sidebar" />
  <tiles:insertAttribute name="content" />
  <tiles:insertAttribute name="footer" />
 </div>


</body>
</html>