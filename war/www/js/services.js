angular.module('org.oatesonline.yaffle.services', ['ngResource'])

/**
 * A simple example service that returns some data.
 */
.factory('Leagues', function($log, $resource, $q) {

//  var slug_url = 'http://localhost:8888/league'; // This doesn't work in the browser. Cross-siteconstraint?
 var slug_url = '/league'; // This doesn't work in the browser. Cross-siteconstraint?
  // var url = 'http://localhost:8000/js/data/mock/s1.json'; // This doesn't work either - not sure why
  var league_url = 'js/data/mock/s1.json'; // mock data just now. Need to build the URL once CORS has beem worked out.
  var all_url = 'js/data/mock/leagues.json'; //this works

  var leagueData = $resource(league_url);
  var allLeagueData = $resource(all_url);
  

 //var yaffle_server_url="http://localhost:8888";
 var yaffle_server_url="/";
 
  return {
    all: function() {

       var leagues = allLeagueData.get();
      return leagues;

    },
    promotionTeams: function(){
       var allLeagues = allLeagueData.get();
       var pLeagues = []
       var leagueCodes = ["e1","e2","e3","e4"];
        for (var i = 0; i< leagueCodes.length; i++){
          $log.log("retrieving league data for  : " + leagueCodes[i]);
           pLeagues[i] = get(leagueCode[i]);
        }
      return pLeagues;
    },    
    relegationTeams: function(){
       var allLeagues = allLeagueData.get();
       var rLeagues = []
       var leagueCodes = ["s1","s2","s3","s4"];
        for (var i = 0; i< leagueCodes.length; i++){
          $log.log("retrieving league data for  : " + leagueCodes[i]);
           pLeagues[i] = get(leagueCode[i]);
        }
      return rLeagues;
    },
    get: function(leagueId) {
      leagueData = $resource(yaffle_server_url + "/league/" + leagueId);
      $log.log("League Code :" + leagueId + " has been selected.");
      //var league = leagueData.get(leagueId);
      var league = leagueData.get();
      return league;  
    },
    findById: function(leagueId) {
        var remoteURL = slug_url + "/" + leagueId;
        $log.log("pre call remote Service : " + remoteURL)
        var remoteLeagueData = $resource(remoteURL);
        var league = remoteLeagueData.get();
        var deferred = $q.defer();
        deferred.resolve(league);
 //       return deferred.promise;
        return league;
    }
  }
  
})

// .factory('Teams', function ($log, $resource, $q, $cookieStore){
//   var yaffle_server_url="/";
//   var user_id= $cookieStore("org.oatesonline.yaffle.yuid");
//   var team_url = yaffle_server_url + "teams/" + user_id; 
//   var teamResource = $resource(team_url, null, {method:"POST"});
//   var addTeams = function(user){
//     var deferred = $q.defer();
//   }
//   return {
//     add: function(user){
//       return addTeams(user);
//     }
//   }
// })

.factory('Leaderboard', function ($log, $resource, $q){
    var yaffle_server_url="/";
    var leaderboard_path = "leaderboard/";
    var  leaderboard_url= yaffle_server_url + leaderboard_path;
    var leaderboard_resource = $resource(leaderboard_url, null,{method: 'get', isArray:false});
     var getLeaderboard = function(){ 
  //    leaderboard_resource.get();
      var deferred = $q.defer();
      leaderboard_resource.get(null, function(successResult){ //success  
        $log.log("Leaderboard Successful: ");
        deferred.resolve(successResult);
      }, function (failureResult){
        $log.log("Leaderboard Failed to  load" );
        deferred.reject(failureResult);
      });
        return deferred.promise;  
      } 
      return {
       getLeaderboard : function (){
        return getLeaderboard();
       }
     }
})

.factory('Player', function ($q, $resource, $cookie){
    var yaffle_server_url="/";
    var player_path="player";
    var player_url = yaffle_server_url + player_path;
    var player_resource = (player_url);
})
.factory('Login', function ($log, $resource, $q){
  var yaffle_server_url="/";
  var login_path = "login";
  var login_url = yaffle_server_url + login_path;

  var loginResource = $resource(login_url, null, {method:"POST"});
  var getPlayer = function(user){
      var deferred = $q.defer();
      loginResource.save(user,
          function(successResult){ //success
          //  $rootScope.player = successResult;        
            $log.log("Login Successful: " + successResult.name);
            deferred.resolve(successResult);
          }, function (failureResult){
            $log.log("Login Failed: Response:" + failureResult.toString());
            deferred.reject(failureResult);
          });
      return deferred.promise;
    }
    return {
      login: function(user){
        return getPlayer(user);
    }
  } 
});