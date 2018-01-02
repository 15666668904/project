<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	function dataFlag() {
		if(!document.getElementById("projectFile").value){
			alert("请选择project文件!");
			return false;
		}
		return confirm("将清空现有数据，确认？");
	}
</script>
<title>请选择project文件</title>
</head>
<body>
	<form style="margin-top: 10%; margin-left: 30%;" action="fileupload"
		onsubmit="return dataFlag();" method="post"
		enctype="multipart/form-data">
		<div style="display: inline;">
			请选择project文件：<input style="height: 25px;" name="projectFile" id="projectFile"
				type="file" accept=".mpp"></input> &nbsp;&nbsp;&nbsp;
			<button style="height: 25px; width: 70px;" type="submit">确认</button>
			<!-- 参数begin -->
			<input type="hidden" name="plan_version_sid"
				value="${plan_version_sid }" /> <input type="hidden"
				name="xpmobs_sid" value="${xpmobs_sid }" /> <input type="hidden"
				name="calendar_flag" value="${ calendar_flag}" /> <input
				type="hidden" name="dept_sid" value="${dept_sid }" /> <input
				type="hidden" name="Range" value="${Range }" /> <input
				name="project_date_flag" type="hidden" value="${project_date_flag }" id="project_date_flag" />
			<!-- 参数end -->
		</div>
	</form>
</body>
</html>