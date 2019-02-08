<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>
<style>
.logo {
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 5px;
    width: 150px;
}
</style>
<section id="wrapper">
	<div class="login-register"
		style="background-image:url(${pageContext.request.contextPath}/resources/assets/images/background/login-register.jpg);">
		<div class="login-box card">
			<div class="card-body">

				<!-- Status Message -->
				
					 <c:if test="${param.error != null}">  
						 <div class="alert alert-danger alert-dismissible bubble">
	                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>     
					        ! كلمة المرور او اسم المستخدم غير صحيح
					    </div>
				    </c:if>
			
				
				<form:form class="form-horizontal form-material" id="loginform"
					method="post"
					action="${pageContext.request.contextPath}/login" name="form" >
					<div class="login-logo text-center p-20">
						<img class="logo" src="${pageContext.request.contextPath}/resources/images/logo-small.png" />
					</div>
					
					<div class="form-group ">
						<div class="col-xs-12">
							<input class="form-control" type="text" placeholder="البريد الألكتروني" name="username">
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<input class="form-control" type="password"
								placeholder="كلمة المرور"
								name="password">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-md-12 font-14">
							<div class="checkbox checkbox-primary pull-left p-t-0">
								<input id="checkbox-signup" type="checkbox" name="remember-me" value="true">
								<label for="checkbox-signup">تذكرني</label>
							</div>
						</div>
					</div>
					<div class="form-group text-center m-t-20">
						<div class="col-xs-12">
							<button class="btn btn-danger btn-lg btn-block waves-effect waves-light" type="submit">
								تسجيل الدخول
							</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</section>


<script type="text/javascript">
   (function() {

      "use strict";
	/*
      $('#loginform').formValidation({
         framework : 'bootstrap',
         fields : {
            password : {
               validators : {
                  notEmpty : {
                     message : 'كلمة المرور مطلوبة'
                  }
               }
            },
            username : {
               validators : {
                  notEmpty : {
                     message : 'البريد الالكتروني مطلوب'
                  }
               }
            }
         }
      }).on('success.form.fv', function(e) {
         
         //e.preventDefault(e)
         console.log( $("#loginform").serialize())
      });
      
*/
   })();
</script>

<script id="signResultFailure" type="text/template">
<div class="alert alert-danger alert-dismissible">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>خطأ</strong> حدث خطأ اثناء التسجيل في عربولة
</div>
</script>
