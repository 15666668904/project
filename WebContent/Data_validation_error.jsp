<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据校验失败</title>
</head>
<body>
	<center>
		<h1>
			<%=request.getAttribute("Data_validation_results")%>
		</h1>
	</center>
</body>
</html>