
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>

<div class="page-wrapper">

	<!-- Bread crumb and right sidebar toggle -->
   <div class="row page-titles">
        <div class="col-md-5 align-self-center">
            <h3 class="text-themecolor">عرض المحتوى</h3>
        </div>
        <sec:authorize access="hasAuthority('CONTENT_ADD')">
	        <div class="col-md-7 text-right">
	            <a href="${pageContext.request.contextPath}/content/add"
					   class="btn btn-danger"> <i class="fa fa-plus"></i> إضافة محتوى
				   </a>
	        </div>
        </sec:authorize>
    </div>
		<!-- Container fluid  -->
		<div class="container-fluid">

			<!-- Start Page Content -->
			<div class="row">
				<div class="col-12">
					<div class="card">
						<div class="card-body">

							<div id="resultMessage"></div>
							
							<form:form id="filterForm">
								
								<div class="form-body p-t-20">
                          
                           <div class="row">
                               <div class="col-md-6">
                                    <div class="form-group row">
                                       <label class="control-label text-right col-md-3">كلمة البحث</label>
                                       <div class="col-md-9">
                                           <input type="text" class="form-control" id="key" placeholder="كلمة البحث" name="key">
                                       </div>
                                    </div>
                               </div>
                               <div class="col-md-6">
                                    <div class="form-group row">
                                       <label class="control-label text-right col-md-3">القسم</label>
                                       <div class="col-md-9">
                                          <select class="form-control custom-select" name="categoryId" id="categoryId">
                                              <option value="0">القسم</option>
                                              <c:forEach var="category" items="${categories}">
                                                 <option value="${category.value.id}" label="${category.value.name}" />
                                              </c:forEach>
                                          </select>
                                       </div>
                                    </div>
                               </div>
                               <!--/span-->
                           </div>
                           
                           <div class="row">
                               <div class="col-md-6">
                                   <div class="form-group row">
                                       <label class="control-label text-right col-md-3">الحالة</label>
                                       <div class="col-md-9">
                                            <select class="form-control custom-select" name="statusId" id="statusId">
                                                <option value="0">الحالة</option>
                                                <c:forEach var="status" items="${statusList}">
                                                   <option value="${status.id}" label="${status.status}" />
                                                </c:forEach>
                                            </select>
                                       </div>
                                   </div>
                               </div>
                               <div class="col-md-6">
                                   <div class="form-group row">
                                       <label class="control-label text-right col-md-3">النوع</label>
                                       <div class="col-md-9">
                                            <select class="form-control custom-select" name="contentTypeId" id="contentTypeId">
                                                <option value="0">النوع</option>
                                                <c:forEach var="type" items="${contentTypes}">
                                                   <option value="${type.value.id}" label="${type.value.name}" />
                                                </c:forEach>
                                            </select>
                                       </div>
                                   </div>
                               </div>
                           </div>
                           <!--/row-->
                           <div class="row">
                               <div class="col-md-6">
                                   <div class="form-group row">
                                       <label class="control-label text-right col-md-3">التاريخ من</label>
                                       <div class="col-md-9">
                                           <input type="date" class="form-control" name="startDate" id="startDate" placeholder="dd/mm/yyyy" />
                                       </div>
                                   </div>
                               </div>
                               <!--/span-->
                               <div class="col-md-6">
                                   <div class="form-group row">
                                       <label class="control-label text-right col-md-3">إلى</label>
                                       <div class="col-md-9">
                                           <input type="date" class="form-control" name="endDate" id="startDate" placeholder="dd/mm/yyyy" />
                                       </div>
                                   </div>
                               </div>
                               <!--/span-->
                           </div>
                           
                       </div>

								<hr>
								<div class="form-actions">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-md-offset-3 col-md-9">
                                            <button class="btn btn-danger" type="submit">بحـــــث</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6"> </div>
                            </div>
                        </div>
								
							</form:form>

						</div>
					</div>
				</div>
			</div>


			<!-- List with data tables start here -->
			<div class="card">
				<div class="card-body">
				
				   <div class="table-responsive p-t-20">
                   <table class="table color-table muted-table hover-table ">
                       <thead>
                           <tr>
                               <th>العنوان</th>
                               <th>القسم</th>
                               <th>الحالة</th>
                               <th>النوع</th>
                               <sec:authorize access="hasAuthority('CONTENT_FEATURED')">
                                 <th>مميز</th>
                               </sec:authorize>
                               <sec:authorize access="hasAuthority('CONTENT_PUBLISH_HIDE')">
                                 <th>نشر</th>
                               </sec:authorize>
                               <th>التاريخ</th>
                               <th>التحكم</th>
                           </tr>
                       </thead>
                       <tbody id="contentListId">
                           
                       </tbody>
                   </table>
               </div>
               <hr>
               <div>
                  <nav aria-label="page navigation">
	                    <ul class="pagination p-0" id="pagination" style="justify-content: center"></ul>
	               </nav>
               </div>
				</div>
			</div>
			<!-- List with end data table end here -->

		</div>

		<!-- End Of page -->
	</div>

<div class="modal fade" id="contentDetails" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" style="display: none;" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">
					<strong>تفاصيل المحتوى</strong>
				</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body p-10" id="contentDetailsBody"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-white waves-effect" data-dismiss="modal">
					إغلق
				</button>
			</div>
		</div>
	</div>
</div>

<div id="toolbar-options" class="hidden">
   <sec:authorize access="hasAuthority('CONTENT_DELETE')">
	   <a href="#" id="delete"><i class="fa fa-trash"></i></a>
	</sec:authorize>
	<sec:authorize access="hasAuthority('CONTENT_ACCEPT_REJECT')">   
	   <a href="#" id="accept" class="accept"><i class="fa fa-check"></i></a>
	   <a href="#" id="reject" class="reject"><i class="fa fa-close"></i></a>
   </sec:authorize>
</div>

<div id="toolbar-delete-options" class="hidden">
   <sec:authorize access="hasAuthority('CONTENT_DELETE')">
      <a href="#" id="delete"><i class="fa fa-trash"></i></a>
   </sec:authorize>
</div>

<script type="text/javascript">
(function() { "use strict";
	
	var start = 0;
	var count = 10;
	
	listContent(true);
	
	$("#filterForm").submit(function(e) {
		e.preventDefault();
		start = 0;
		listContent(true);
	});
	
	function listContent(includeCount) {

		$.ajax({
         type : "POST", 
         url : "${pageContext.request.contextPath}/content/list",
         data : $("#filterForm").serialize()+ "&start="+start+"&count=" + count + '&includeCount=' + includeCount,
         success : function(data) {
        	   console.log(data)
				if (data.status.code == 200) {
					var template = $('#contentListTemplate').html();
					$.each(data.contentList, function( index, value ) {
						  
						 value.index = index;
						  
						 if(value.status.code == 'REJECTED_CONTENT') {
							 value.rejected = true;
						 } 

						 (value.contentType.code == 'VIDEO') ? value.isVideo = true : value.isVideo = false;
						 (value.featured) ? value.featuredIcon = 'fa-star' : value.featuredIcon = 'fa-star-o';
						 (value.published) ? value.publishedIcon = 'fa-send' : value.publishedIcon = 'fa-send-o';
						 
						 value.data = JSON.stringify(data.contentList[index]);
					});
					
               var resultTemplate = Mustache.to_html(template, data);
               $('#contentListId').html(resultTemplate);  
               formatDate();

               $('.actions').each(function( index ) {
            	   
            	   if($(this).attr('status') == "PENDING") {
            		   $(this).toolbar({
                           content: '#toolbar-options',
                           style: 'dark',
                           position: 'right',
                           hideOnClick: true
                        });
            	   } else {
            		   $(this).toolbar({
                           content: '#toolbar-delete-options',
                           style: 'dark',
                           position: 'right',
                           hideOnClick: true
                     });
            	   }
            	   
            	   $(this).on('toolbarItemClick', function( event, buttonClicked ) {
            		      //console.log($(this).attr('contentId'));
            		      //console.log(buttonClicked.id)
            		      
            		      var contentId = $(this).attr('contentId');
            		      var $this = $(buttonClicked);
            		      
            		      switch(buttonClicked.id) {

            		         case 'delete':
								    	  deleteContent(contentId);
								        break;
								    case 'accept':
								    	  acceptContent(contentId, $this);
                                break;
                            case 'reject':
                            	  rejectContent(contentId, $this);
                                break;    
								}
            		      
            		});
				});
                              
               var pages = Math.ceil(data.totalCount / count);
               
               if (includeCount) {
            	               	  
            	   if($('#pagination').data("twbs-pagination")){
                       $('#pagination').twbsPagination('destroy');
                  }
   
            	   $('#pagination').twbsPagination({
							totalPages : (pages <= 0) ? 1 : pages,
							visiblePages : 7,
							prev : '<span aria-hidden="true">&laquo;</span>',
							next : '<span aria-hidden="true">&raquo;</span>',
							first : 'الاول',
							last : 'الاخر'
						}).on('page', function (event, page) {
							   start = (page - 1) * count;
					         listContent(false);
					   });
					}
               
				} else {
					var template = $('#no-result-found').html();
					var resultTemplate = Mustache.to_html(template);
					$('#contentListId').html(resultTemplate);
					$('#pagination').twbsPagination('destroy');
				}
			}
		});
	}
	
})();
</script>

<script type="text/javascript">
function deleteContent(contentId) {
	swal({
        title: ' هل انت متأكد من حذف هذا المحتوى',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: 'نعم',
        cancelButtonText: 'إلغاء'
      }).then((result) => {
        if (result.value) {
           $.ajax({type : "POST", 
                url : "${pageContext.request.contextPath}/content/"+contentId+"/delete",
                success : function(data) {
                  if (data.status.code == 200) {
                    swal('تم !', 'تم حذف هذا المحتوى بنجاح','success');
                    $('#content_'+contentId).remove();
                  } else {
                      swal('فشل !', 'حدث خطأ اثناء حذف  المحتوى ','error');
                  }
                }
           });
        }
      });
}
function acceptContent(contentId, ele) {
	swal({
        title: 'موافق على هذا المحتوى',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: 'نعم',
        cancelButtonText: 'إلغاء'
      }).then((result) => {
        if (result.value) {
           $.ajax({type : "POST", 
                url : "${pageContext.request.contextPath}/content/"+contentId+"/accept",
                success : function(data) {
                  if (data.status.code == 200) {
                	  swal('تم !', 'تم الموافقة على المحتوى بنجاح','success');
                	  ele.siblings().andSelf().remove(".accept, .reject");
                  } else {
                      swal('فشل !', 'حدث خطأ اثناء الموافقة على المحتوى ','error');
                  }
                }
           });
        }
      });
}
function rejectContent(contentId, ele) {
	swal({
        title: 'رفض هذا المحتوى',
        type: 'warning',
        html: $('#reasonsTemplate').html(),
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'نعم',
        cancelButtonText: 'إلغاء',
        preConfirm: () => {
           $.ajax({type : "POST", 
                url : "${pageContext.request.contextPath}/content/"+contentId+"/reject?reasonId=" + $('#rejectReason').val(),
                success : function(data) {
                	 if (data.status.code == 200) {
                        swal('تم !', 'تم رفض المحتوى بنجاح','success');
                        ele.siblings().andSelf().remove(".accept, .reject");
                   } else {
                	      swal('فشل !', 'حدث خطأ اثناء رفض المحتوى ','error');
                   }
                }
           });
        }
   })
}
</script>

<script type="text/javascript">

function publishContent(contentId) {
	
	var published = $('#published'+contentId);
   var ispublished = ( published.attr('published') == 'true');
   	
	if(ispublished) {
		swal({
			  title: 'هل انت متأكد من إخفاء المحتوى',
			  type: 'warning',
			  showCancelButton: true,
			  confirmButtonText: 'نعم متأكد',
			  cancelButtonText: 'إلغاء'
			}).then((result) => {
			  if (result.value) {
				  $.ajax({type : "POST", 
                   url : "${pageContext.request.contextPath}/content/"+contentId+"/hide",
                   success : function(data) {
                	   var published = $('#published'+contentId);
            	      if (data.status.code == 200) {
                        published.toggleClass("fa-send fa-send-o");
                        published.attr('published', !ispublished);
                     }
                   }
              });
			  }
			})
	} else {
		swal({
	        title: 'تاريخ النشر',
	        html: '<input id="publishDate" type="date" class="form-control">',
	        focusConfirm: false,
	        preConfirm: () => {
	        	  $.ajax({type : "POST", 
	                url : "${pageContext.request.contextPath}/content/"+contentId+"/publish?publishDate=" + $('#publishDate').val(),
	                success : function(data) {
	                	 var published = $('#published'+contentId);
	                   if (data.status.code == 200) {
	                     published.toggleClass("fa-send fa-send-o");
	                     swal('تم النشر!', 'تم نشر المحتوى بنجاح','success');
	                     published.attr('published', !ispublished);
	                   }
	                }
	           });
	        }
	   })
	}
	
}

function setContentFeatured(contentId) {
    
	var featured = $('#featured'+contentId);
	var isFeatured = ( featured.attr('featured') == 'true');
   
	featured.toggleClass("fa-star-o fa-star");

   $.ajax({type : "GET", 
       url : "${pageContext.request.contextPath}/content/featured?contentId=" 
      	  + contentId +"&featured=" + !isFeatured,
       success : function(data) {
       	 if (data.status.code != 200) {
       		featured.toggleClass("fa-star-o fa-star");
       	 } else {
       		 $('#featured'+contentId).attr('featured', !featured);
       	 }
       }
   });
}

function viewContentDetails(event, index) {
	
	var content = $('#content'+index).data('content');
	
	$.get('${pageContext.request.contextPath}/resources/templates/content-details.jsp', function(template) {
	 	  var contentTemplate = Mustache.to_html(template, content);
	     $('#contentDetailsBody').html(contentTemplate);
	     formatDate(wiseLang);
	     $('#contentDetails').modal();
   });
	
}

</script>


<script id="contentListTemplate" type="text/template">
{{#contentList}}
<tr class="bubble" id="content_{{id}}">
    <td><a href="javascript:void(0)" onclick="viewContentDetails(event, {{index}})" id="content{{index}}" class="text-info" data-content="{{data}}">{{title}}</a></td>
    <td>{{category.name}}</td>
    <td>
      {{#rejected}}
         <span class="mytooltip tooltip-effect-1" style="z-index:100">
             <span class="tooltip-item2">{{status.status}}</span>
             <span class="tooltip-content4 clearfix">
                <span class="tooltip-text2">{{reason.reason}}</span> 
             </span>
         </span>
      {{/rejected}}
      {{^rejected}}
      {{status.status}} 
      {{/rejected}}
    </td>
    <td>{{contentType.name}}</td>

    <sec:authorize access="hasAuthority('CONTENT_FEATURED')">
      <td><a class="btn  btn-circle" href="javascript:void(0)" onclick="setContentFeatured({{id}})" class="text-info"><i featured="{{featured}}" id="featured{{id}}" class="fa {{featuredIcon}}" /></a></td>
	 </sec:authorize>
    <sec:authorize access="hasAuthority('CONTENT_PUBLISH_HIDE')">
      <td><a class="btn  btn-circle" href="javascript:void(0)" onclick="publishContent({{id}})" class="text-info"><i published="{{published}}" id="published{{id}}" class="fa {{publishedIcon}}" /></a></td>
    </sec:authorize>

    <td class="formatDate">{{insertDate}}</td>
    <td>
         <div class="actions btn-toolbar btn-toolbar-dark p-1" status="{{status.code}}" contentId="{{id}}"><i class="fa fa-ellipsis-h"></i></div>
    </td>
</tr>
{{/contentList}}
{{^contentList}}
   no data found
{{/contentList}}
</script>

<script id="reasonsTemplate" type="text/template">
<select class="form-control custom-select" name="statusId" id="rejectReason">
<option value="">سبب الرفض</option>
<c:forEach var="reason" items="${reasons}">
   <option value="${reason.id}" label="${reason.reason}" />
</c:forEach>
</select>
</script>

