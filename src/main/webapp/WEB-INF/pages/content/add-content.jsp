
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tiles/layout/includes.jsp"%>

<div class="page-wrapper">

	<!-- Bread crumb and right sidebar toggle -->
	<div class="row page-titles">
	    <div class="col-md-5 align-self-center">
	        <h3 class="text-themecolor"> إضافة محتوى </h3>
	    </div>
	</div>
	
	<!-- Container fluid  -->
	<div class="container-fluid">
	    
	    <!-- Start Page Content -->
	     <div class="row">
	         <div class="col-12">
	             <div class="card">
	                 <div class="card-body">
	                     
	                     <div id="resultMessage" ></div>
	                     
	                     <!-- 
	                       <iframe width="560" height="315" src="https://www.youtube.com/embed/7KRQio_xlNY" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe> 
	                     -->
	                     	                     
	                     <form:form data-provide="validation" data-disable="false" id="contentForm">
				               <div class="card-body">
				
				                  <div class="row">
				                  
				                     <div class="col-md-6">
				                        
				                        <div class="form-group">
				                           <select class="form-control col-md-12 custom-select" name="categoryId" id="categoryId">
				                              <option value="">القسم</option>
				                              <c:forEach var="category" items="${categories}">
				                                 <option value="${category.value.id}"
				                                    label="${category.value.name}" />
				                              </c:forEach>
				                           </select>
				                        </div>
				                        
				                        <div class="form-group">
				                           <select class="form-control col-md-12 custom-select" name="contentTypeId" id="contentTypeId">
				                              <option value="">النوع</option>
				                              <c:forEach var="type" items="${contentTypes}">
				                                 <option value="${type.value.id}" data="${type.value.code}" label="${type.value.name}" />
				                              </c:forEach>
				                           </select>
				                        </div>
				                        				                        
				                        <div class="form-group">
				                     		<textarea class="form-control col-md-12" id="textarea" placeholder="العنوان" rows="2" name="title"></textarea>
				                        </div>
				                        
				                     </div>
				                     
				                     <div class="col-md-6">
				                     	<div class="form-group">
				                     		<input type="text" class="form-control col-md-12" name="videoUrl" placeholder="حدد نوع المحتوى اولا" style="display:none">
				                        </div>
				                        
				                        <div id="contentData">
					                       
				                        </div>
				                     </div>
				                   
				                  </div>
				
				                  <div class="row">
				                     <div class="col-md-12 text-left">
				                        <button class="btn btn-danger" type="submit" >إضـــــــافة</button>
				                     </div>
				                  </div>
				
				               </div>
				            </form:form>
	                     
	                 </div>
	             </div>
	         </div>
	     </div>
	     
	</div>

<!-- End Of page -->
</div>
<script type="text/javascript">
(function() {
	"use strict";
	
	$('#contentForm').formValidation({
        framework : 'bootstrap',
        fields : {
        	categoryId : {
              validators : {
                 notEmpty : {
                    message : 'القسم مطلوب'
                 }
              }
           },
           contentTypeId: {
               validators : {
                   notEmpty : {
                      message : 'نوع المحتوى مطلوب'
                   }
                }
           },
           title : {
              validators : {
                 notEmpty : {
                    message : 'عنوان المحتوى مطاوب'
                 }
              }
           }
        }
     }).on('success.form.fv', function(e) {
    	   e.preventDefault();
    	   
    	   var response = {message: ''};
    	 
         $.ajax({
            type : "POST", 
            url : "${pageContext.request.contextPath}/content/add",
            data : $("#contentForm").serialize(),
            success : function(data) {
               
	        if (data.status.code == 200) {
						var template = $('#resultSuccess').html();
						response.message = 'تم حفظ المحتوى بنجاح';
						var resultTemplate = Mustache.to_html(template, response);
						$('#resultMessage').html(resultTemplate);
						
						$('#contentData').html('')
						$('#contentForm [name="videoUrl"]').hide();
                  $('#contentForm').trigger("reset").data('formValidation').resetForm();
                  $('#contentForm .old-content').remove();
						
					} else {
						var template = $('#resultFailure').html();
						response.message = 'لم يتم حفظ المحتوى';
						var resultTemplate = Mustache.to_html(template, response);
						$('#resultMessage').html(resultTemplate);
					}
				},
				error : function(e) {
					var template = $('#resultFailure').html();
					var resultTemplate = Mustache.to_html(template);
					$('#resultMessage').html(resultTemplate);
				}
			});
		});

	})();
	
	$('#contentTypeId').change(function() {
		
		$('#contentData').html('');
		$('#contentForm [name="title"]').val('');
		$('#contentForm .old-content').remove();
		
		if($('#contentTypeId :selected').attr('data') == 'VIDEO') {
			handleViewVideo();
		} else {
			handleViewImage();
		}
		
	});
	
	function handleViewImage() {
		
		$('#contentForm [name="videoUrl"]').hide();
		
		var template = $('#imageView').html();
		var resultTemplate = Mustache.to_html(template);
		$('#contentData').html(resultTemplate);
		
		registerDropzoneImage();
	}
	
	function handleViewVideo() {
		
		
		swal({
	           title: 'أدخل رابط الفيديو',
	           html: '<input id="videoContentId" type="text" class="form-control">',
	           focusConfirm: false,
	           showCancelButton: true,
	           confirmButtonText: 'نعم',
	           cancelButtonText: 'إلغاء',
	           preConfirm: () => {
	        	      
	        	      var videoId = validateYouTubeUrl($('#videoContentId').val());
	               var url = 'https://www.googleapis.com/youtube/v3/videos?id='+videoId+'&key=AIzaSyDJ9ky9DrRxhUBO0WF3cZMnPFCu7KfgRD8&fields=items(id,snippet(thumbnails,title,description,publishedAt),statistics)&part=snippet,statistics';
	               
	               $.ajax({
	                      type : "GET", 
	                      url : url,
	                      contentType : "application/json; charset=utf-8",
	                      success : function(response) {
	                         console.log(response)
	                         if(response.items.length > 0) {
	                        	 
	                         
	                         var snippet = response.items[0].snippet;
	                         var statistics = response.items[0].statistics;
	                         
	                         if(snippet && statistics) {
	                              console.log('snippet && statistics')
	                           $('#contentForm').append('<input class="old-content" type="hidden" value="'+snippet.title+'" name="video.title" />')
	                              .append('<input class="old-content" type="hidden" value="'+response.items[0].id+'" name="video.videoId" />')
	                              .append('<input class="old-content" type="hidden" value="'+snippet.description+'" name="video.description" />')
	                              .append('<input class="old-content" type="hidden" value="'+snippet.thumbnails.default.url+'" name="video.thumbnail" />')
	                              .append('<input class="old-content" type="hidden" value="'+snippet.publishedAt+'" name="video.publishDate" />')
	                              .append('<input class="old-content" type="hidden" value="'+statistics.viewCount+'" name="video.noOfViews" />');
	                           
	                           var template = $('#videoView').html();
	                           var resultTemplate = Mustache.to_html(template, {"snippet": snippet, "statistics": statistics});
	                           
	                           $('#contentData').html(resultTemplate);
	                           $('#contentForm [name="title"]').val(snippet.title);
	                           $('#contentForm').formValidation('revalidateField', 'title');
	                           $('#contentData').html(resultTemplate);
	                        
	                         } 
	                         } else {
	                        	 swal('خطأ!', 'رابط غير صالح','error');
	                         }
	                         
	                      },
	                      error : function(e) {
	                          $('#contentForm [name="title"]').val('');
	                          swal('خطأ!', 'رابط غير صالح','error');
	                      }
                  });
	           }
	      })
		/*
		$('#contentForm [name="videoUrl"]')
		.show()
		.prop( "placeholder", 'ألصق رابط الفيديو هنا...' )
		.prop( "disabled", false )
		.bind('input propertychange', function() {
			
		      if(this.value.length){
		    	  
		    	   inputChangeCount = 1;
		        
			    	var videoId = validateYouTubeUrl(this.value);
			    	var url = 'https://www.googleapis.com/youtube/v3/videos?id='+videoId+'&key=AIzaSyDJ9ky9DrRxhUBO0WF3cZMnPFCu7KfgRD8&fields=items(id,snippet(thumbnails,title,description,publishedAt),statistics)&part=snippet,statistics';

		        if(videoId) {
		        	$.ajax({
		                type : "GET", 
		                url : url,
		                contentType : "application/json; charset=utf-8",
		                success : function(response) {
		                	console.log(response)
		                   var snippet = response.items[0].snippet;
		                	 var statistics = response.items[0].statistics;

		                   if(snippet && statistics) {
		                	   	console.log('snippet && statistics')
		                	   $('#contentForm').append('<input class="old-content" type="hidden" value="'+snippet.title+'" name="video.title" />')
			                	   .append('<input class="old-content" type="hidden" value="'+response.items[0].id+'" name="video.videoId" />')
			                	   .append('<input class="old-content" type="hidden" value="'+snippet.description+'" name="video.description" />')
								   	.append('<input class="old-content" type="hidden" value="'+snippet.thumbnails.default.url+'" name="video.thumbnail" />')
								   	.append('<input class="old-content" type="hidden" value="'+snippet.publishedAt+'" name="video.publishDate" />')
								   	.append('<input class="old-content" type="hidden" value="'+statistics.viewCount+'" name="video.noOfViews" />');
							   	
							   	var template = $('#videoView').html();
									var resultTemplate = Mustache.to_html(template, {"snippet": snippet, "statistics": statistics});
									
									$('#contentData').html(resultTemplate);
									$('#contentForm [name="title"]').val(snippet.title);
									$('#contentForm').formValidation('revalidateField', 'title');
									$('#contentData').html(resultTemplate);
								
		                   } 
		                   
		                },
		                error : function(e) {
		                	$('#contentForm [name="title"]').val('');
		                }
		             });
		        }
		      }
		});
		*/
		
	}
	
	function validateYouTubeUrl(url) {

        if (url != undefined || url != '') {
            var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=|\?v=)([^#\&\?]*).*/;
            var match = String(url).match(regExp);
            if (match && match[2].length == 11) {
                return match[2];
            }
            else {
                return undefined;
            }
        }
        
	}
	
	function registerDropzoneImage() {
		
		var appDropzone = new Dropzone("div#appDropzone", { 
				url : "${pageContext.request.contextPath}/file/?module=CONTENT",
				paramName : 'file',
				acceptedFiles : 'image/*',
				maxFiles : 1,
				init : function() {
					var myDropzone = this;

					myDropzone.on("addedfile", function() {
						if (this.files[1] != null) {
							this.removeFile(this.files[0]);
						}
					});

					myDropzone.on("success", function(file, response) {
						var old = $('#contentForm .old-content').remove();
						$('#contentForm [name="title"]').val(response.info.name);
						$('#contentForm').formValidation('revalidateField', 'title');
						$('#contentForm').append('<input class="old-content" type="hidden" value="'+response.info.id+'" name="images[0].id" />');
						$('#contentForm').append('<input class="old-content" type="hidden" value="'+response.info.fileModuleId+'" name="images[0].fileModuleId" />');
					});

					this.on("removedfile", function(file) {
						$('#userForm .image').val("")
					});

				}
		});
	}
	
	
</script>

<script id="imageView" type="text/template">
<div id="appDropzone" class="dropzone">
	<div class="dz-default dz-message">
		<span> أضف صورة هنا </span>
	</div>
</div>
</script>

<script id="videoView" type="text/template">
<div class="card">
    <img class="card-img-top img-fluid" src="{{snippet.thumbnails.default.url}}" alt="Card image cap">
</div>
</script>