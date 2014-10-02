angular.module('org.oatesonline.yaffle.controllers', [])

.controller('DashCtrl', function($scope, $log, $rootScope) {
$log.log("Dashboard controller Fired");
    $scope.myself = $rootScope.loggedInPlayer;
    $scope.myName = $rootScope.playerName;
    $scope.myTeams =$rootScope.teams;

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

.controller('SignInCtrl', function($scope, Login, $log, $state, $rootScope) {
  $scope.signIn = function(user) {

    var loggedInPlayer = Login.login(user).then(function(myPlayer){
      $log.log("Player found: " + myPlayer.name);
      user.name=myPlayer.name;
      $rootScope.loggedInPlayer = myPlayer;
      $rootScope.teams =    myPlayer.teams; 
      $rootScope.id=myPlayer.id;
      $rootScope.playerName = myPlayer.name;
      document.location.href = '/webclient/www/index.html#/tab/dash';
    }, function(myPlayer){
      $log.log("Login Failure: " + myPlayer);
    });

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
    };
  };
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
