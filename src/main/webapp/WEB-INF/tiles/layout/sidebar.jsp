<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>


<!-- Left Sidebar - style you can find in sidebar.scss  -->
<aside class="left-sidebar">
    <!-- Sidebar scroll-->
    <div class="scroll-sidebar">
        <!-- User profile -->
        <div class="user-profile p-t-20">
            <!-- User profile image -->
            <div class="profile-img"> 
                     <img src="${pageContext.request.contextPath}/resources/default-user.png" 
                     	onerror="this.src='${pageContext.request.contextPath}/resources/images/users/default-user.png'"
                     alt="user" /> 
                     <!-- this is blinking heartbit
                    <div class="notify setpos"> <span class="heartbit"></span> <span class="point"></span> </div>
                    -->
            </div>
            <!-- User profile text-->
            <div class="profile-text"> 
                
                <h5>Markarn Doe</h5>
                <a href="#" class="dropdown-toggle u-dropdown" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="true"><i class="mdi mdi-settings"></i></a>
                <!-- <a href="app-email.html" class="" data-toggle="tooltip" title="Email"><i class="mdi mdi-gmail"></i></a> -->

                <div class="dropdown-menu animated flipInY">
	                <!-- text--> 
	                <a href="#" class="dropdown-item">
	                    <i class="ti-user"></i> صفحة المستخدم 
                   </a>
	                <div class="dropdown-divider"></div>
	                <!-- text-->  
	                <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">
	                    <i class="fa fa-power-off"></i> خروج
	                </a>
	                <!-- text-->  
                </div>
            </div>
        </div>
        <!-- End User profile text-->
        <!-- Sidebar navigation-->
        <nav class="sidebar-nav">
            <ul id="sidebarnav">
                <li class="nav-devider"></li>
                <li class="active"> 
                    <a class="waves-effect waves-dark" href="${pageContext.request.contextPath}/" >
                        <i class="mdi mdi-gauge"></i><span class="hide-menu">الرئيسية</span>
                    </a>
                </li>
                <sec:authorize access="hasAuthority('CONTENT_VIEW')">
                   <li> 
	                    <a class="waves-effect waves-dark" href="${pageContext.request.contextPath}/content">
	                        <i class="mdi mdi-gauge"></i><span class="hide-menu">المحتوى</span>
	                    </a>
	                </li>  
                </sec:authorize>
            </ul>
        </nav>
        <!-- End Sidebar navigation -->
    </div>
    <!-- End Sidebar scroll-->
</aside>
