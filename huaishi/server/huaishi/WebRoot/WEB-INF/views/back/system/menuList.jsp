<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/back/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="div_panel" class="easyui-panel" title="用户列表"
		style="padding: 10px; background: #fafafa;"
		data-options="iconCls:'icon-menu',closable:false,fit:true,   
                collapsible:false,minimizable:false,maximizable:true,tools:[
				{
					iconCls:'icon-add',
					handler:function (){
						//弹出添加的对话框
						showAddDialog();
					}
				}]
		">

		<!-- 使用easyui的表格控件 -->
		<table id="t_easyui-datagrid" class="easyui-datagrid"
			data-options="fitColumns:true,singleSelect:true">
			<thead>
				<tr>
					<th data-options="field:'selectAll',width:20,align:'center'"><input
						type="checkbox" checked="checked" /></th>
					<th data-options="field:'id',width:20,align:'center'">编号</th>
					<th data-options="field:'name',width:40,align:'center'">用户名</th>
					<th data-options="field:'password',width:40,align:'center'">密码</th>
					<th data-options="field:'phoneNumber',width:40,align:'center'">电话号码</th>
					<th data-options="field:'avatarAddress',width:40,align:'center'">头像地址</th>
					<th data-options="field:'option',width:40,align:'center'">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${p.rows}" var="item">
					<tr>
						<td><input type="checkbox" /></td>
						<td>${item.id}</td>
						<td>${item.name}</td>
						<td><a
							onclick="showUpdateDialog('${item.id}','${item.name}')"
							href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-pencil'">edit</a> <a
							onclick="deleteEntity(${item.id})" id="btn"
							href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">remove</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<!-- 分页对象 -->
		<p:paging url="${basePath}/${uri}/list" pageSize="${p.pageSize}"
			total="${p.total}" nowPage="${p.nowPage}" mode="0"
			firstIcon="${basePath}/images/back/paging/paging_first.png"
			preIcon="${basePath}/images/back/paging/paging_pre.png"
			nextIcon="${basePath}/images/back/paging/paging_next.png"
			lastIcon="${basePath}/images/back/paging/paging_last.png"
			refreshIcon="${basePath}/images/back/paging/paging_refresh.png" />

	</div>

	<!-- 隐藏的部分,这部分是添加的时候弹出来的对话框 -->
	<div id="div_entityForm" title="添加">
		<form id="entityForm">
			<table>
				<tr>
					<td>菜单名:</td>
					<td><input id="entityId" type="hidden" name="id" /><input
						id="entityName" type="text" name="name" /></td>
				</tr>
			</table>
		</form>
	</div>

</body>

<!-- 以下是脚本的代码 -->
<script type="text/javascript">

	//承载form表单的div的id
	var divFormId = "div_entityForm";
	//实体表单的id
	var formId = "entityForm";
	//返回的信息其中一种,错误信息
	var errorMsg = "error";
	//显示数据的地址
	var listUrl = "${basePath}/${uri}/list";
	//增加实体对象的地址
	var insertUrl = "${basePath}/${uri}/insert";
	//删除实体的地址
	var deleteUrl = "${basePath}/${uri}/deleteById";
	//更新实体的地址
	var updateUrl = "${basePath}/${uri}/updateById";
	//分页对象的每页显示的条数
	var pageSize = "${p.pageSize}";
	//分页对象的当前页
	var nowPage = "${p.nowPage}";

	/**
	 * 删除一个实体
	 */
	function deleteEntity(id) {
		$("#entityId").attr("value", id);
		$("#entityName").attr("value", "");
		updateEntity(deleteUrl, formId, function(msg) { // 成功回调的函数
			// 字符串转化为json数据
			// msg = eval('(' + msg + ')');
			// alert(msg + ":" + msg.msg + ":" + errorMsg);
			if (msg.msg == errorMsg) {
				$.messager.alert('提示', "操作失败,错误信息为:" + msg.msgText);
			} else {
				//重新加载数据
				document.location.href = listUrl + "?pageSize=" + pageSize
						+ "&nowPage=" + nowPage;
			}
		});
	}

	/**
	 * 弹出更新的对话框
	 */
	function showUpdateDialog(id, menuName) {
		$("#entityId").attr("value", id);
		$("#entityName").attr("value", menuName);
		showDialog("更新菜单", "更新", function() {
			updateEntity(updateUrl, formId, function(msg) { // 成功回调的函数
				// 字符串转化为json数据
// 				msg = eval('(' + msg + ')');
				// alert(msg + ":" + msg.msg + ":" + errorMsg);
				if (msg.msg == errorMsg) {
					$.messager.alert('提示', "操作失败,错误信息为:" + msg.msgText);
				} else {
					//重新加载数据
					document.location.href = listUrl + "?pageSize=" + pageSize
							+ "&nowPage=" + nowPage;
				}
			});
		});
	}

	/**
	 * 弹出添加菜单的对话框
	 */
	function showAddDialog() {
		showDialog("添加菜单", "保存", function() {
			updateEntity(insertUrl, formId, function(msg) { // 成功回调的函数
				// 字符串转化为json数据
				// msg = eval('(' + msg + ')');
				// alert(msg + ":" + msg.msg + ":" + errorMsg);
				if (msg.msg == errorMsg) {
					$.messager.alert('提示', "操作失败,错误信息为:" + msg.msgText);
				} else {
					//重新加载数据
					document.location.href = listUrl + "?pageSize=" + pageSize
							+ "&nowPage=" + nowPage;
				}
			});
		});
		//清空文本框中的数据
		$("#entityId").attr("value", "");
		$("#entityName").attr("value", "");
	}

	//弹出对话框,title对话框的标题    confimBtText确认按钮的文本   confimCallBackF确认按钮点击后回调的函数
	function showDialog(title, confimBtText, confimCallBackF) {
		$("#" + divFormId).dialog({
			title : title,
			closed : false,
			cache : false,
			modal : true,
			buttons : [ {
				text : '取消',
				handler : function() {
					// 做一些取消该做的事情
				}
			}, {
				text : confimBtText,
				handler : confimCallBackF
			} ]
		});
	}
</script>

</html>

