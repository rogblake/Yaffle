angular.module('org.oatesonline.yaffle', ['ionic', 'org.oatesonline.yaffle.controllers', 'org.oatesonline.yaffle.services' ,'ngCookies'])

.run(function($ionicPlatform, $log) {

//http://stackoverflow.com/questions/15358029/why-am-i-unable-to-inject-angular-cookies
// $cookies does not get loaded  on startup as only providers and constants can be injected into 
// config blocks.  $cookies is a service. Therefore we inject it manually here.
  // var $cookies;
  //   angular.injector(['ngCookies']).invoke(function(_$cookies_) {
  //     $cookies = _$cookies_;
  //   });


    if(window.StatusBar) {
      StatusBar.hide();
    }
  // });
})


.config(function($stateProvider, $urlRouterProvider) {



  // Ionic uses AngularUI Router which uses the concept of states
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

