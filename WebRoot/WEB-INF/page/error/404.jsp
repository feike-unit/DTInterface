<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>出错了!</title>
    
	<style>
		BODY { PADDING-RIGHT: 32px; PADDING-LEFT: 32px; FONT-SIZE: 13px; BACKGROUND: #eee; PADDING-BOTTOM: 32px; MARGIN-LEFT: auto; WIDTH: 80%; COLOR: #000; MARGIN-RIGHT: auto; PADDING-TOP: 32px; FONT-FAMILY: verdana, arial, sans-serif }
		DIV { BORDER-RIGHT: #bbb 1px solid; PADDING-RIGHT: 32px; BORDER-TOP: #bbb 1px solid; PADDING-LEFT: 32px; BACKGROUND: #fff; PADDING-BOTTOM: 32px; BORDER-LEFT: #bbb 1px solid; PADDING-TOP: 32px; BORDER-BOTTOM: #bbb 1px solid }
		H1 { PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: bold; FONT-SIZE: 120%; PADDING-BOTTOM: 20px; MARGIN: 0px; COLOR: #904; PADDING-TOP: 0px; FONT-FAMILY: "trebuchet ms", ""lucida grande"", verdana, arial, sans-serif }
		H2 { PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: bold; FONT-SIZE: 105%; PADDING-BOTTOM: 0px; MARGIN: 0px 0px 8px; TEXT-TRANSFORM: uppercase; COLOR: #999; PADDING-TOP: 0px; BORDER-BOTTOM: #ddd 1px solid; FONT-FAMILY: "trebuchet ms", ""lucida grande"", verdana, arial, sans-serif }
		P { PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 6px; MARGIN: 0px; PADDING-TOP: 6px }
		A:link { COLOR: #002c99; TEXT-DECORATION: none }
		A:visited { COLOR: #002c99; TEXT-DECORATION: none }
		A:hover { COLOR: #cc0066; BACKGROUND-COLOR: #f5f5f5; TEXT-DECORATION: underline }
	</style>
	<body>
		<form id="frmMain" method="post" runat="server">
			<div>
				<h1>抱歉!发生了错误</h1>

				<h2>Details</h2>
				<p>所访问的地址不存在, 请确认输入的地址是否正确, 如果仍然存在问题, 请与管理员联系。</p>
				<p style="MARGIN-TOP: 24px"><a href="/DTInterface">返回</a></p>
			</div>
		</form>
	</body>
</html>
