<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Welcome, 한자리 !</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/resources/css/main.css">
</head>

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

		var myCanvas = document.getElementById("myCanvas");
		var myContext = myCanvas.getContext("2d");

		myContext.fillStyle = "red";
		myContext.fillRect(40, 20, 60, 60);
		myContext.fillStyle = "green";
		myContext.fillRect(120, 20, 60, 60);
		myContext.fillStyle = "blue";
		myContext.fillRect(40, 100, 60, 60);
		myContext.fillStyle = "yellow";
		myContext.fillRect(120, 100, 60, 60);

	}
</script>

</head>

<body onload="init()">
	<div class="d1" id="d1">
		<h3>
			<label>Choose Floor<input list="floors" name="myFloors"
				style="width: 230px;" /></label>
		</h3>
		<datalist id="floors">
			<option value="1층">
			<option value="2층">
			<option value="3층">
			<option value="4층">
			<option value="5층">
			<option value="6층">
			<option value="7층">
			<option value="8층">
			<option value="9층">
			<option value="10층">
			<option value="11층">
		</datalist>

		<canvas id="myCanvas" style="background-color: aliceblue" width="230"
			height="300">
		</canvas>

		<h3>
			<label>Search Employee</label>
		</h3>
		<div class="search" id="search">
			<input type="text" placeholder="검색어를 입력해주세요.">
		</div>
		<div class="button">
			<button>검색</button>
		</div>

	</div>
	<div class="d3" id="hr"></div>
	<div class="d2" id="d2">
		<div class="button">
			<button>팝업창 보기</button>
		</div>
	</div>
</body>

</html>
