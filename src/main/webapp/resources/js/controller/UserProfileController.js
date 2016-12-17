app.controller('userProfileController',["$scope", "$http","$window","$state","$stateParams","$rootScope", function($scope, $http,$window,$state,$stateParams,$rootScope) {
	  
	$scope.books = JSON.parse($stateParams.books);
	$scope.books;
	
	
}]);
