var app = angular.module('app', ['ui.router']);

app.config(function($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/');
    
    $stateProvider

    .state('register', {
        url: '/register',
        templateUrl: 'resources/views/register.html',
        controller: 'loginController'
    })
    .state('verification', {
        url: '/verification/:username',
        templateUrl: 'resources/views/verification.html',
        controller: 'loginController'
    })
      .state('login', {
        url: '/login',
        templateUrl: 'resources/views/login.html',
        controller: 'loginController'
    })
     .state('search', {
        url: '/search/:username',
        templateUrl: 'resources/views/search.html',
        controller: 'searchController',
        
    })
      .state('searchpatron', {
        url: '/searchpatron/:username',
        templateUrl: 'resources/views/searchpatron.html',
        controller: 'searchPatronController',
        
    })
    .state('cart', {
        url: '/cart:res',
        templateUrl: 'resources/views/cart.html',
        controller: 'cartController',
        
    })
    .state('update', {
        url: '/update/:books',
        templateUrl: 'resources/views/update.html',
        controller: 'searchController',
        
    })
    .state('addbook', {
    	parent: 'search',
    	url: '/search/addbook',
        templateUrl: 'resources/views/addbook.html',
        controller: 'searchController',
    })
     .state('userprofile', {
    	parent: 'searchpatron',
    	url: '/searchpatron/userprofile',
        templateUrl: 'resources/views/userprofile.html',
        controller: 'userProfileController',
        params : { books: null}
    })
    
   
    
});