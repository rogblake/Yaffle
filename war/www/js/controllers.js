angular.module('org.oatesonline.yaffle.controllers', [])

.controller('DashCtrl', function($scope, $log, Player) {
  $log.log("Dashboard controller Fired");
  $scope.onload = function(){
    {
      $log.log("Loading Player via Cookie")
      $scope.loggedInPlayer = Player.loadPlayer().then(function(myPlayer){
        $log.log("Player found from Cookie: " + myPlayer.name);    
        $scope.loggedInPlayer = myPlayer;
        $scope.initializePlayer(myPlayer);
      });
    }  
  },

  $scope.initializePlayer = function(myPlayer){
    $scope.loggedInPlayer = myPlayer;
    $scope.teams =    myPlayer.teams;
    $scope.myNickname = myPlayer.nickname;
    $scope.rTeams = []
    $scope.pTeams = []
    for (var i=0; i<myPlayer.teams.length; i++){
      $log.log("In Loop with " + myPlayer.teams[i].leagueCode.charAt(0).toLowerCase());
      if (myPlayer.teams[i].leagueCode.charAt(0).toLowerCase() == 's'){
        $scope.pTeams.push(myPlayer.teams[i]);
      } else {
        $scope.rTeams.push(myPlayer.teams[i]);
      }
    }
    $log.log("Length of rTeams: " +  $scope.pTeams.length)
    $log.log("Length of pTeams: " +  $scope.pTeams.length)

    $scope.id=myPlayer.id;
    $scope.playerName = myPlayer.name;
    document.location.href = '/www/index.html#/tab/dash';
  }
})

.controller('LeaguesCtrl', function($scope, Leagues, $log) {
 $log.log("Leagues controller Fired");

  var lData =  Leagues.all();
  $scope.leagues = lData;
})

.controller('LeagueCtrl', function($scope, Leagues, $log, $stateParams) {
  $log.log("League Detail controller fired");
  $scope.leagueCode = $stateParams.leagueId;
//  $scope.league = Leagues.get($scope.leagueCode);
 $scope.league = Leagues.findById($scope.leagueCode);
  if ($scope.league !=null){
  	$scope.teams= $scope.league.teams;
  	$log.log("Selected League = " + $scope.league.name);
  } else {
    $log.log("Selected League Not Found.");
  }
})

.controller('SignInCtrl', function($scope, Login, $log, $state, Player) {

  //TODO Refactor out to a service. This is a duplicate function of one found in DashCntrl
  $scope.initializePlayer = function(myPlayer){
    $scope.loggedInPlayer = myPlayer;
    $scope.teams =    myPlayer.teams;
    $scope.myNickname = myPlayer.nickname;
    $scope.rTeams = []
    $scope.pTeams = []
    for (var i=0; i<myPlayer.teams.length; i++){
      $log.log("In Loop with " + myPlayer.teams[i].leagueCode.charAt(0).toLowerCase());
      if (myPlayer.teams[i].leagueCode.charAt(0).toLowerCase() == 's'){
        $scope.pTeams.push(myPlayer.teams[i]);
      } else {
        $scope.rTeams.push(myPlayer.teams[i]);
      }
    }
    $log.log("Length of rTeams: " +  $scope.pTeams.length)
    $log.log("Length of pTeams: " +  $scope.pTeams.length)

    $scope.id=myPlayer.id;
    $scope.playerName = myPlayer.name;
    document.location.href = '/www/index.html#/tab/dash';
  }, 

  $scope.onload = function(){
    {
      $log.log("Loading Player via Cookie")
      $scope.loggedInPlayer = Player.loadPlayer().then(function(myPlayer){
        $log.log("Player found from Cookie: " + myPlayer.name);    
        $scope.loggedInPlayer = myPlayer;
        $scope.initializePlayer(myPlayer);
      });
    }  
  },

  $scope.signIn = function(user) {
    if ($rootScope.loggedInPlayer == null){
      var loggedInPlayer = Login.login(user).then(function(myPlayer){
        $log.log("Player found from Login: " + myPlayer.name);    
        $rootScope.loggedInPlayer = myPlayer;
        $scope.initializePlayer(myPlayer);
      });
    }
  },


  function(myPlayer){
    $log.log("Login Failure: " + myPlayer);
      alert("Login Failed.  Please ensure you have the correct email address and Pin. Contact the administrator to get your correct details.");
      document.location.href = '/www/index.html#/login';
  },

  $scope.openHelp = function () {
    $modal.open({
        templateUrl: '/templates/help.html',
        windowClass: 'modal',
        controller: function ($scope, $modalInstance, $log) {
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
          },
      });
    }
  })
// .controller('TeamCtrl', function($scope, $rootScope, Leagues, $log,) {{
//     if ($rootScope.leagues == null){
//       $rootScope.leagues = Leagues.all();
//     } 
//       $scope.getLeague = function(){
//   };

// })
// .controller('AddPlayerCtrl', function($scope, $rootScope, $log) {
//   $log.log("Add Player controller Fired");

// })

.controller('HelpCtrl', function($scope, $modal, $log) {
  $log.log("Help Detail controller Fired");
   
})

.controller('PlayerCtrl', function($scope, $log) {
  $log.log("Player Detail controller Fired");
   
}).controller('MoreCtrl', function($scope, $log) {
  $log.log("More button controller Fired");
   
})

.controller('LeaderboardCtrl', function( $scope, Leaderboard, $log) {
  if ($scope.leaderboard == null){
      var lbData =  Leaderboard.getLeaderboard();
      lbData.then(function(lb){
           $scope.leaderboard  = lb;
           $scope.players = lb.players;
      }, function (errorData){
          alert("Error");
      })
  }
});
