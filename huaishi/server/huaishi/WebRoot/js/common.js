/**
 * 这个js脚本里面存放着一些公共的js方法,抽取出来的
 */

/**
 * 更新一个实体对象,出错的话,弹出错误的消息<br>
 * ,没出错的话,回调成功的函数successFunCall<br>
 * 包含插入,更新,删除的操作
 */
function updateEntity(url, formId, successFunCall) {
	// alert("url=" + url + ",content=" + $("#" + formId).serialize());
	$.ajax({
		url : url,
		type : "POST",
		data : $("#" + formId).serialize() + "&isReturnJson=true",
		success : successFunCall,
		error : function() {
			alert("对不起,更新操作失败");
		}
	});
}

/**
 * ajax的封装
 * 
 * @param url
 *            地址,默认使用GET方式请求
 * @param successFunCall
 *            成功获取之后回调的函数
 */
function getJson(url, successFunCall) {
	$.ajax({
		url : url,
		type : "GET",
		success : function(data) {
			successFunCall(data);
		},
		error : function() {
			alert("对不起,操作失败");
		}
	});
}
