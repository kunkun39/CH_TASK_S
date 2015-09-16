$(function() {
	// init operate button role
	initRole();
});

function initRole() {
	_remoteCall("role/hasBugAllOpAuth.do", {bugId: $("#bugId").val()}, function(data) {
		data == "Y" ? $("div.operate-div").show() : $("div.operate-div").remove(); 
	});
}