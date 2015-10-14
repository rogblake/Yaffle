angular.module('org.oatesonline.yaffle.services', ['ngResource', 'ngCookies'])

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
      return allLeagueData.get();
      var deferred = $q.defer();
      var league = allLeagueData.get(null, function(successResult){
          $log.log("Leagues Retrieval Success");
          deferred.resolve(successResult);
          return successResult;
        }, function(failureResult){
          $log.log("Leagues Retrieval Failure");
          deferred.reject(failureResult);
          return failureResult;
        });
    },
    promotionTeams: function(){
       var allLeagues = this.all();
       var pLeagues = [];
       var pCodes = ["E1","E2","E3","E4"];
       for (var i = 0; i < pCodes.length; i++){
          $log.log("retrieving league data for  : " + allLeagues[pCodes[i]]);
          pLeagues.push(allLeagues[pCodes[i]]);
       }
      return pLeagues;
    },    
    relegationTeams: function(){
       var allLeagues = this.all();
       var rLeagues = [];
       var rCodes = ["S1","S2","S3","S4"];
       for (var i = 0; i < rCodes.length; i++){
          $log.log("retrieving league data for  : " + allLeagues[rCodes[i]]);
          rLeagues.push(allLeagues[rCodes[i]]);
        }
       return rLeagues;
    },
    get: function(leagueId) {
      leagueData = $resource(yaffle_server_url + "/league/" + leagueId);
      $log.log("League Code :" + leagueId + " has been selected.");
      //var league = leagueData.get(leagueId);
      var league = leagueData.get();
      var deferred = $q.defer();
      deferred.resolve(league);
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

.factory('Player', function ($q, $resource, $cookies, $log, $rootScope){
    var yaffle_server_url="/";
    var player_path="player";
    var player_id =  $cookies["org.oatesonline.yaffle.yuid"];
    var player = null;

    $log.log("player ID : " +  player_id);
//    if ((player_id != "") && (player_id != undefined)){
      var player_url = yaffle_server_url + player_path + "/" + player_id;
      var player_resource = $resource(player_url, null, {method:"GET"});

  var getPlayer = function(){
      var deferred = $q.defer();
      player_resource.get(
          function(successResult){ //success
            $rootScope.player = successResult;        
            $log.log("Player Successful: " + successResult.name);
            deferred.resolve(successResult);
            return successResult;
          }, function (failureResult){
            $log.log("Player Failed: Response:" + failureResult.toString());
            deferred.reject(failureResult);
          });
      return deferred.promise;
    }

    return {
      loadPlayer: function(){
        var p = getPlayer();
        player = p;
        return p;
      },
      get: function(){
        if (player != null){
          return player;
        }
        return getPlayer();
      },
      getPlayerIdFromCookie: function(){
        if ((player_id != null) && (String(player_id).length > 1 )){
          $log.log("Cookie Found  for player: " + player_id);
          return player_id;
        } else return null;
      }
    }
  })

.factory('Login', function ($log, $resource, $q, $rootScope){
  var yaffle_server_url="/";
  var login_path = "login";
  var login_url = yaffle_server_url + login_path;

  var login_resource = $resource(login_url, null, {method:"POST"});
  var getPlayer = function(user){
      var deferred = $q.defer();
      login_resource.save(user,
          function(successResult){ //success
            $rootScope.player = successResult;        
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