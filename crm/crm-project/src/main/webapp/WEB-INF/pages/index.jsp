<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String url=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";

//System.out.println(url);
%>
<base href="<%=url%>">
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<script type="text/javascript">
		document.location.href = "settings/qx/user/loginPage.do";
	</script>
</body>
</html>
