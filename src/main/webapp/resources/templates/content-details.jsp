
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>

<div class="">
    <ul class="nav nav-tabs customtab" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" data-toggle="tab" href="#tab1" role="tab" aria-expanded="false">
                <span class="hidden-sm-up"><i class="ti-home"></i></span> 
                <span class="hidden-xs-down"> عن المحتوى</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#tab2" role="tab" aria-expanded="false">
                <span class="hidden-sm-up"><i class="ti-user"></i></span> 
                <span class="hidden-xs-down">{{contentType.name}}</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#tab3" role="tab" aria-expanded="false">
                <span class="hidden-sm-up"><i class="ti-user"></i></span> 
                <span class="hidden-xs-down">صاحب المحتوى</span>
            </a>
        </li>
    </ul>
    
    <!-- Tab panes -->
    <div class="tab-content">
        <div class="tab-pane active" id="tab1" role="tabpanel"
            aria-expanded="true">
            <div class="table-responsive">
                <table class="table table-hover" >
                    
                    <tbody>
                        <tr>
                            <td>العنوان</td>
                            <td>{{title}}</td>
                        </tr>
                        <tr>
                            <td>القسم</td>
                            <td>{{category.name}}</td>
                        </tr>
                        <tr>
                            <td> الحالة</td>
                            <td>{{status.status}}</td>
                        </tr>
                        <tr>
                            <td> النوع</td>
                            <td>{{contentType.name}}</td>
                        </tr>
                        <tr>
                            <td>مميز</td>
                            <td><a href="javascript:void(0)" class="text-info"><i class="fa {{featuredIcon}}" ></i></a></td>
                        </tr>
                        <tr>
                            <td>تاريخ الإنشاء</td>
                            <td><span class="formatDate">{{insertDate}}</span></td>
                        </tr>
                        <tr>
                            <td>تاريخ النشر</td>
                            <td><span class="formatDate">{{publishDate}}</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="tab-pane" id="tab2" role="tabpanel" aria-expanded="true">
        
            <div class="card">
               <h3 class="m-b-0 p-t-10 p-b-10">{{contentType.name}}</h3>
               {{#isVideo}}
                  <iframe height="315" src="https://www.youtube.com/embed/{{video.videoId}}" 
                          frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" 
                          allowfullscreen>
                  </iframe>
               {{/isVideo}}
               {{^isVideo}}
                  {{#images}}
                      <p>{{name}}</p>
                      <img class="card-img-top" src="{{thumbnail}}"alt="Card image cap" style="max-height: 165px;">
                  {{/images}}
               {{/isVideo}}
               
            </div>

       </div>
        
        <div class="tab-pane" id="tab3" role="tabpanel" aria-expanded="true">
            {{#adminUser}}
            <div class="card">
                 <div class="card-body">
                     <div class="card-body little-profile text-center">
                      <img src="{{user.image}}" class="img-circle" width="150"
                           onerror="this.src='${pageContext.request.contextPath}/resources/images/users/default-user.png'">
                      <p>
	                      <h3 class="mytooltip tooltip-effect-1">
	                          <span class="tooltip-item2">{{admin.firstName}} {{admin.lastName}}</span>
	                          <span class="tooltip-content4 clearfix">
	                             <span class="tooltip-text2"><strong>مستخدم أدمن</strong> </span> 
	                          </span>
	                      </h3>
                      </p>
                      <p>{{admin.email}}</p>
                      </div>
                 </div>
            </div>
            {{/adminUser}}
            {{^adminUser}}
               <div class="card">
                  <div class="card-body little-profile text-center">
                       <img src="{{user.image}}" class="img-circle" width="150"
                            onerror="this.src='${pageContext.request.contextPath}/resources/images/users/default-user.png'">
                       <p>
                       <h3 class="mytooltip tooltip-effect-1">
                           <span class="tooltip-item2">{{user.firstName}} {{user.lastName}}</span>
                           <span class="tooltip-content4 clearfix">
                              <span class="tooltip-text2 text-center"><strong>مستخدم عادي</strong> </span> 
                           </span>
                       </h3>
                       </p>
                       <p>{{user.email}}</p>
                  </div>
               </div>
            {{/adminUser}}
				

		 </div>
        
        
    </div>
</div>