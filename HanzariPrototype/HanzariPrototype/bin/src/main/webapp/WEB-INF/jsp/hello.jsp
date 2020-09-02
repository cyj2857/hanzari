<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Welcome, 한자리 !</title>

<style type="text/css">
.d1 {
	float: left;
	width: 20%;
	height: 100%;
	border-right: 1px solid #b8b8b8;
	margin-right: -1px;
	overflow-y: scroll;
}

.d2 {
	float: left;
	width: 79%;
	height: 100%;
	overflow-y: scroll;
}

.d3 {
	float: left;
	width: 0.3%;
	height: 100%;
	background-color: #888888;
}

#hr {
	cursor: pointer;
}
</style>

<script type="text/javascript">
	var startpos, diffpos = 0, range = 50;
	var isEnable = false;

	function on_mouse_down(e) {
		startpos = event.clientX + diffpos;
		isEnable = true;
		return false;
	}

	function on_mouse_up(e) {
		isEnable = false;
		return false;
	}

	function on_mouse_move(e) {
		if (isEnable) {
			pos = event.clientX;

			diffpos = startpos - pos;

			var width = window.innerWidth / 2;
			if (diffpos > -(width - range) && diffpos < (width - range)) {
				document.getElementById("d1").style.width = width - diffpos
						+ "px";
				document.getElementById("d2").style.width = width - 20
						+ diffpos + "px";
			}
		}
	}

	function init() {
		document.getElementById("hr").onmousedown = on_mouse_down;
		document.onmouseup = on_mouse_up;
		document.onmousemove = on_mouse_move;
	}
</script>

</head>

<body onload="init()">
	<div class="d1" id="d1">
		<canvas id="myCanvas" style="background-color: aliceblue" width="230"
			height=300">
		</canvas>
		<script>
			var canvas = document.getElementById("myCanvas");
			var context = canvas.getContext("2d");

			context.fillStyle = "red";
			context.fillRect(40, 20, 60, 60);
			context.fillStyle = "green";
			context.fillRect(120, 20, 60, 60);
			context.fillStyle = "blue";
			context.fillRect(40, 100, 60, 60);
			context.fillStyle = "yellow";
			context.fillRect(120, 100, 60, 60);
			
		</script>
	</div>
	<div class="d3" id="hr"></div>
	<div class="d2" id="d2">RIGHT</div>
</body>

</html>