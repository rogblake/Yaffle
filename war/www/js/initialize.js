// JavaScript Document
var yaffleController = angular.module('oatesonline.org.yaffle.v2' , []);


yaffleController.controller("initializeController", function ($scope){
	var jsonType="application/json";
	var  leagueUrls=["/league/e1","/league/e2","/league/e3","/league/e4","/league/s1","/league/s2","/league/s3","/league/s1"];

	var leagueResponse = function (data){
		alert(data.toString());
		var  leagues={}
		leagues[data.leagueCode] = data;
	};
	for (var i=0; i<leagueUrls.length; i++){
		$.ajax(leagueUrls[i],jsonType,leagueResponse);
	}
});