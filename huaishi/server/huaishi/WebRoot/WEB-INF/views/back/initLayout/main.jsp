<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${basePath}/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${basePath}/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/easyui/demo.css">

<script type="text/javascript" src="${basePath}/js/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${basePath}/js/easyui/jquery.easyui.min.js"></script>
</head>
<frameset rows="15%,*" border="2px">
	<frame src="${basePath}/back/main/initTop" scrolling="no" name="top"
		noresize="noresize" />
	<frameset cols="240px,*">
		<frame src="${basePath}/back/main/initLeft" name="left" noresize="noresize" />
		<frame src="${basePath}/back/main/initCenter" name="center" />
	</frameset>
</frameset>

</html>