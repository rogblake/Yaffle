// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('org.oatesonline.yaffle', ['ionic', 'org.oatesonline.yaffle.controllers', 'org.oatesonline.yaffle.services'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.StatusBar) {
      // org.apache.cordova.statusbar required
  //    StatusBar.styleDefault();
      StatusBar.hide();
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider

    // setup an abstract state for the tabs directive
    .state('tab', {
      url: "/tab",
      abstract: true,
      templateUrl: "templates/tabs.html"
    })

    // Each tab has its own nav history stack:

    .state('tab.dash', {
      url: '/dash',
      views: {
        'tab-dash': {
          templateUrl: 'templates/tab-dash.html',
          controller: 'DashCtrl'
        }
      }
    })

    .state('tab.leagues', {
      url: '/leagues',
      views: {
        'tab-leagues': {
          templateUrl: 'templates/tab-leagues.html',
          controller: 'LeaguesCtrl'
        }
      }
    })
    .state('league', { //not a child of tab.leagues
      url: '/league/:leagueId', // NOTE: No '#' here, but it is present in the href in the repeater on the view
      templateUrl: 'templates/tab-leagues-league.html',
      controller: 'LeagueCtrl'
    })

    .state('tab.leaderboard', {
      url: '/leaderboard',
      views: {
        'tab-leaderboard': {
          templateUrl: 'templates/tab-leaderboard.html',
          controller: 'LeaderboardCtrl'
        }
      }
    })  
    .state('signin', {
      url: "/login",
      templateUrl: "templates/sign-in.html",
      controller: 'SignInCtrl'
    })

    .state('addPlayer', {
      url: "/addplayer",
      templateUrl: "templates/add-player.html",
      controller: 'AddPlayerCtrl'
    })

    .state('forgotpassword', {
      url: "/forgot-password",
      templateUrl: "templates/forgot-password.html"
    })

    .state('tab.more', {
      url: '/more',
      templateUrl: 'templates/tab-more.html',
      controller: 'MoreCtrl'
    })
  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/tab/dash');

});

