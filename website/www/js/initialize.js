// JavaScript Document
var jsonType="application/json";
var  leagueUrls=["/league/e1","/league/e2","/league/e3","/league/e4","/league/s1","/league/s2","/league/s3","/league/s1"]
function onSiteLoad(){
	for (var i=0; i<leagueUrls.length; i++){
		$.ajax(leagueUrl,jsonType,leagueResponse);
	}
}

var leagueResponse = function (data){
	alert(data.toString());
};