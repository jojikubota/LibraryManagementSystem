app.controller('searchPatronController',["$scope", "$http","$window","$state","$stateParams","$rootScope", function($scope, $http,$window,$state,$stateParams,$rootScope) {
	  
	$scope.username = $stateParams.username;
	
	
	
	$scope.search = function(keyword)
	    {
		var data = {username : $scope.username, keyword :data}
	    	$scope.keyword = keyword;
	    	    $http({
	    	        url: 'searchPatron',
	    	        method: "POST",
	    	        headers: {
	    	            'Content-Type': 'application/json'
	    	        },
	    	        data: { 'keyword' : keyword}
	    	        
	    	    })
	    	    .then(function(response) {
	    	    	var aa = JSON.stringify(response.data);
	    	    	$state.go("cart", { res: aa});
	    	    	 
	    	    }, 
	    	    function(response) { // optional
	    	    	alert("no"+response);
	    	    });
	  
	         
	    }
	
	$scope.searchText = "";
	$scope.searchResults = [];
	$scope.cartItems = [];
	$scope.confirmedItems = [];
	
	
	 $scope.navigate = function(){
		 
		 $http({
		        url: 'getBooksForUser',
		        method: "GET",
		        headers: {
		            'Content-Type': 'application/json'
		        }
		        
		    })
		    .then(function(response) {
	    	    	$scope.searchResults =  JSON.stringify(response.data);
	    	    	$state.go("userprofile", { books: $scope.searchResults});
		    }, 
		    function(response) { // optional
		    //	$scope.reset();
		    });
	    }
	    
	 $scope.navigate();
}]);