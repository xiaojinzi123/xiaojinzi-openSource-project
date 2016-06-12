<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/pager" prefix="p"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- ===============以下是依赖的样式文件和脚本文件========================== -->
<link rel="stylesheet" type="text/css"
	href="${basePath}/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${basePath}/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/easyui/demo.css">

<script type="text/javascript" src="${basePath}/js/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${basePath}/js/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="${basePath}/js/common.js"></script>

<!-- ================上述是依赖的样式文件和脚本文件========================== -->
</head>
</html>