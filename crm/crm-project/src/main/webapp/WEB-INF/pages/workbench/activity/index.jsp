<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String url=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

//System.out.println(url);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="<%=url%>">
<html>
<head>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		//点击创建按钮弹出模态窗口
		$("#createBtn").click(function (){
			$("#createActivityModal").modal("show")
		})
		//创建成功初始化
		$("#createActivityFrom").get(0).reset()
		//点击保存提交数据
		$("#saveId").click(function (){
			//收集参数
			var owner=$("#create-marketActivityOwner").val()
			var name=$("#create-marketActivityName").val()
			var startDate=$("#create-startTime").val()
			var endDate=$("#create-endTime").val()
			var cost=$("#create-cost").val()
			var description=$("#create-describe").val()
			//所有者和名称不能为空
			if(owner==""||name==""){
				alert("所有者和名称不能为空")
				return;
			}
			//开始时间或结束时间只要有一个不为就就进行判断开始时间要小于结束时间
			if(startDate!=""&&endDate!=""){
				if(endDate<startDate){
					alert("开始时间要小于结束时间")
					return;
				}
			}
			//成本只能为非负整数
			var regEx=/^(([1-9]\d*)|0)$/;
			if(!regEx.test(cost)){
				alert("成本只能为非负整数")
				return;
			}
			//发送请求
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.stateCode=="1"){
						// alert("yes")
						//关闭模态窗口
						$("#createActivityModal").modal("hide")
						//创建成功初始化
						// $("#createActivityFrom").get(0).reset()
						//成功刷新市场活动页面
						selectCondition(1,$("#pageId").bs_pagination("getOption","rowsPerPage"))
					}else {
						//提示错我信息
						alert(data.message)
						$("#createActivityModal").modal("show")
					}
				}
			})
		})
		//市场活动页面加载完成时，查询所有数据的第一页以及总条数
		selectCondition(1,10);
		//点击查询按钮
		$("#queryBtn").click(function (){
			selectCondition(1,$("#pageId").bs_pagination("getOption","rowsPerPage"));
		})
		//选中全选按钮把所有字按钮都选上
		$("#selAll").click(function (){
			$("#tbId input[type='checkbox']").prop("checked",this.checked)
		})
		//如果子按钮都选上的话选上serAll
		$("#tbId").on("click","input[type='checkbox']",function (){

			if($("#tbId input[type='checkbox']").size()==$("#tbId input[type='checkbox']:checked").size()){
				$("#selAll").prop("checked",true)
			}else {
				$("#selAll").prop("checked",false)
			}
		})
		//给删除按钮添加单击事件，单击后删除选中数据
		$("#deleteActivityBtn").click(function (){
			//拿到所有选中的数据
			var checkedS=$("#tbId input[type='checkbox']:checked")
			if(checkedS.size()==0){
				alert("请选择要删除的数据")
				return
			}
			//遍历checkedS拿到ids 发送请求
			if(window.confirm("确定要删除吗")){
				var ids=""
				$.each(checkedS,function (index,obj){
					ids+="id="+obj.value+"&"
				})
				ids=ids.substring(0,ids.length-1)
				$.ajax({
					url:"workbench/activity/deleteActivityById.do",
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data){
						if(data.stateCode==1){
							selectCondition(1,$("#pageId").bs_pagination("getOption","rowsPerPage"))
						}else {
							alert(data.message)
						}
					}
				})
			}
		})
        //给修改按钮添加单击事件
        $("#editIdBtn").click(function (){
            var checkedS=$("#tbId input[type='checkbox']:checked")//所有选中的数据
            //判断是否选中以及是否多选
            if(checkedS.size()==0){
                alert("请选择你要修改的数据")
                return
            }
            if(checkedS.size()>1){
                alert("一次只能修改一个数据哦")
                return;
            }
            var id=checkedS[0].value
            //发送ajax请求
            $.ajax({
                url:"workbench/activity/selectById.do",
                data:{
                    id:id
                },
                type:'post',
                dataType:'json',
                success:function (data){
					$("#edit-id").val(data.id)
					$("#edit-marketActivityOwner").val(data.owner)
					$("#edit-marketActivityName").val(data.name)
					$("#edit-startTime").val(data.startDate)
					$("#edit-endTime").val(data.endDate)
					$("#edit-cost").val(data.cost)
					$("#edit-describe").val(data.description)
					//打开修改模态窗口
					$("#editActivityModal").modal("show")
                }
            })
        })
		//给更新按钮添加点击事件
		$("#updateId").click(function (){
			// alert("yes")
			//收集数据
			var id =$("#edit-id").val()
			var owner=$("#edit-marketActivityOwner").val()
			var name=$("#edit-marketActivityName").val()
			var startDate=$("#edit-startTime").val()
			var endDate=$("#edit-endTime").val()
			var cost=$("#edit-cost").val()
			var description=$("#edit-describe").val()
			//判断是否合法
			//所有者和名称不能为空
			if(owner==""||name==""){
				alert("所有者和名称不能为空")
				return;
			}
			//开始时间或结束时间只要有一个不为就就进行判断开始时间要小于结束时间
			if(startDate!=""&&endDate!=""){
				if(endDate<startDate){
					alert("开始时间要小于结束时间")
					return;
				}
			}
			//成本只能为非负整数
			var regEx=/^(([1-9]\d*)|0)$/;
			if(!regEx.test(cost)){
				alert("成本只能为非负整数")
				return;
			}
			//发ajax
			$.ajax({
				url:"workbench/activity/updateActivityById.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.stateCode=="1"){
						alert("yes")
						//关闭模态窗口
						$("#editActivityModal").modal("hide")
						//创建成功初始化
						// $("#createActivityFrom").get(0).reset()
						//成功刷新市场活动页面
						selectCondition($("#pageId").bs_pagination("getOption","currentPage"),$("#pageId").bs_pagination("getOption","rowsPerPage"))
					}else {
						//提示错我信息
						alert(data.message)
						$("#editActivityModal").modal("show")
					}
				}
			})
		})
		//给批量导出加单击事件
		$("#exportActivityAllBtn").click(function (){
			window.location.href="workbench/activity/selectAllActivity.do";
		})
		$("#exportActivityXzBtn").click(function (){
			//所有选中的数据
			var checkedS=$("#tbId input[type='checkbox']:checked")
			//判断是否选中
			if(checkedS.size()==0){
				alert("请选择你要导出的数据")
				return
			}
			var ids=""
			$.each(checkedS,function (index,obj){
				ids+="id="+obj.value+"&"
			})
			ids=ids.substring(0,ids.length-1)
			alert(ids)
			//发送请求
			window.location.href="workbench/activity/selectByIds.do?"+ids;
			// $.ajax({
			// 	url: "workbench/activity/selectByIds.do",
			// 	data:ids,
			// 	type:'post',
			// 	dataType:'json',
			// 	success:function (){
			//
			// 	}
			// })
			// $.ajax({
			// 	url:"workbench/activity/deleteActivityById.do",
			// 	data:ids,
			// 	type:'post',
			//
			// 	success:function (data){
			// 		if(data.stateCode==1){
			// 			selectCondition(1,$("#pageId").bs_pagination("getOption","rowsPerPage"))
			// 		}else {
			// 			alert(data.message)
			// 		}
			// 	}
			// })
		})
		//给市场导入添加单击事件
		$("#inputActivityBtn").click(function (){
			//打开模态窗口
			$("#importActivityModal").modal("show")
		})
		//点击导出
		$("#imActivityBtn").click(function (){
			//收集参数
			//选择导入文件，
			var activity=$("#activityFile").val()//文件名
			//判断是不是excel文件
			var suffix=activity.substring(activity.lastIndexOf(".")+1)//后缀
			if(suffix!="xls"){
				alert("文件只能是excel")
				return
			}
			//获取文件// 判断文件是否小于5M
			var activityFile=$("#activityFile")[0].files[0]
			if(activityFile.size>5*1024*1024){

				alert("文件不能大于5M")
				return;
			}
			//用来传提交数据的可以提交二进制数据
			var formData=new FormData();
			formData.append("activityFile",activityFile)
			//发请求
			$.ajax({
				url:"workbench/activity/inputActivityBatch.do",
				data:formData,
				processData:false,//设置ajax向后台提交数据前是否转换为字符串
				contentType:false,//设置ajax向后台提交数据前是否把所有参数统一按urlencode编码
				type:'post',
				dataType:'json',
				success:function (data){
					if(data.stateCode==1){
						alert("成功导入"+data.otherMsg+"条数据")
						$("#importActivityModal").modal("hide")
						//成功刷新市场活动页面
						selectCondition($("#pageId").bs_pagination("getOption","currentPage"),$("#pageId").bs_pagination("getOption","rowsPerPage"))
					}else {
						alert(data.message)
					}
				}
			})
		})
	});

	//条件查询函数
	function selectCondition(pageNo,pageSize){
		//收集传入条件参数
		var name =$("#query-name").val();
		var owner=$("#query-owner").val();
		var startDate=$("#query-startDate").val();
		var endDate=$("#query-endTime").val();
		$.ajax({
			url: "workbench/activity/selectActivityByConditionForPage.do",
			data: {
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
            dataType:'json',
			success:function (date){
				// alert(date.account);
				// //获取总行数
				// $("#bid").text(date.account);
				//遍历activityList
				var htmlStr=""
				$.each(date.activityList,function (index,obj){
					htmlStr+="<tr class=\"active\">";
					htmlStr+="	<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
					htmlStr+="	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/selectDetailData.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
					htmlStr+="    <td>"+obj.owner+"</td>";
					htmlStr+="	<td>"+obj.startDate+"</td>";
					htmlStr+="	<td>"+obj.endDate+"</td>";
					htmlStr+="</tr>";
				})
				$("#tbId").html(htmlStr);
				$("#selAll").prop("checked",false)
					var totalPages=1
					if(date.account%pageSize==0){
						totalPages=date.account/pageSize
					}else {
						totalPages=parseInt(date.account/pageSize)+1;
					}
					//分页操作调用bs_pagination插件
					$("#pageId").bs_pagination({
						currentPage:pageNo,//当前页码
						rowsPerPage:pageSize,//每页显示条数
						totalRows: date.account,//总条数
						totalPages:totalPages,
						visiblePageLinks: 5,
						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,
						onChangePage:function (event,pageObj){
							selectCondition(pageObj.currentPage,pageObj.rowsPerPage)
						}
					});
			}
		})
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal"id="createActivityFrom" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="date" class="form-control" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="date" class="form-control" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveId" >保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateId">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="imActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endTime">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editIdBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button id="inputActivityBtn" type="button" class="btn btn-default" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selAll" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tbId">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
				<div id="pageId"></div>
			</div>

<%--			<div style="height: 50px; position: relative;top: 30px;">--%>
<%--				<div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="bid"></b>条记录</button>--%>
<%--				</div>--%>
<%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--					<div class="btn-group">--%>
<%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--							10--%>
<%--							<span class="caret"></span>--%>
<%--						</button>--%>
<%--						<ul class="dropdown-menu" role="menu">--%>
<%--							<li><a href="#">20</a></li>--%>
<%--							<li><a href="#">30</a></li>--%>
<%--						</ul>--%>
<%--					</div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--				</div>--%>
<%--				<div style="position: relative;top: -88px; left: 285px;">--%>
<%--					<nav>--%>
<%--						<ul class="pagination">--%>
<%--							<li class="disabled"><a href="#">首页</a></li>--%>
<%--							<li class="disabled"><a href="#">上一页</a></li>--%>
<%--							<li class="active"><a href="#">1</a></li>--%>
<%--							<li><a href="#">2</a></li>--%>
<%--							<li><a href="#">3</a></li>--%>
<%--							<li><a href="#">4</a></li>--%>
<%--							<li><a href="#">5</a></li>--%>
<%--							<li><a href="#">下一页</a></li>--%>
<%--							<li class="disabled"><a href="#">末页</a></li>--%>
<%--						</ul>--%>
<%--					</nav>--%>
<%--				</div>--%>
<%--			</div>--%>

		</div>

	</div>
</body>
</html>
