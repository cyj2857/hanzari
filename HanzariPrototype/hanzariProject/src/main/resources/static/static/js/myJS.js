new Vue({
	el : "#app",
	data : {
		message : "Hello Vue.js!",
		radius : 50
	},
	methods : {
		say : function(message) {
			alert(message)
		}
	}
});