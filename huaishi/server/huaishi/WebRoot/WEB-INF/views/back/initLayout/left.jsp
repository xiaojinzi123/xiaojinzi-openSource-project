<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/back/base.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<!-- 手风琴的效果 -->
	<div id="div_easyui-accordion" class="easyui-accordion"
		style="width: 200px; height: 400px;" data-options="fit:true">
		<c:forEach items="${menus}" var="menu">
			<div id="menu1" title="${menu.name}"
				data-options="iconCls: 'icon-ok'"
				style="overflow: auto; padding: 10px;">
				<c:forEach items="${menu.menuItems}" var="menuItem">
					<a href="${basePath}${menuItem.url}" target="center"
						style="width: 180px; margin-top: 6px;" class="easyui-linkbutton"
						data-options="iconCls:'icon-menu'">${menuItem.name}</a>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
</body>
</html>