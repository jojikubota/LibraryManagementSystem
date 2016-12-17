app.controller('cartController',["$scope", "$http","$window","$state","$stateParams","$rootScope", function($scope, $http,$window,$state,$stateParams,$rootScope) {
	
	$scope.cartItems = [];
	$scope.searchResults = JSON.parse($stateParams.res);
	//$scope.searchResults = ["item1", "item2", "item3","items4","item5","item6"];
	  $scope.searchSubmit = function() {
	    	alert($scope.searchText);
	    	//$http.post('/search', $scope.searchText).then(function(results){
	        	//$scope.searchResults = results;
	    	//});
	    //	$scope.searchResults = ["item1", "item2", "item3","items4","item5","item6"];
	    //	$state.go("cart",{res: $scope.searchResults });
	    };
	
    $scope.addToCart = function(item){
    	
    
    	if($scope.cartItems.indexOf(item) > -1)
    		{
    		alert("Book Already Added!!!");
    		}
    	else
    		$scope.cartItems.push(item);
    	
    	var data = {data :$scope.cartItems}
//		 $http({
//	  	        url: 'checkout',
//	  	        method: "POST",
//	  	        headers: {
//	  	            'Content-Type': 'application/json'
//	  	        },
//	  	        data: { 'checkout':data}
//	  	        
//	  	    })
//	  	    .then(function(response) {
//	  	    	   alert("added");
//	  	    }, 
//	  	    function(response) { // optional
//	  	    	alert("no"+response);
//	  	    });
    	
    	
    };
    
    $scope.removeFromCart = function(item){
    	var index = $scope.cartItems.indexOf(item);
    	$scope.cartItems.splice(index,1);
    	
    };
    
    $scope.confirmOrder = function(){
	   	//$http.post('/confirmOrder', $scope.cartItems).then(function(results){
	    	/* if(results.statusCode == 200) {
	    		$scope.confirmedItems = $scope.cartItems;
	    	}	 */
		//});
    //	$scope.confirmedItems = $scope.cartItems;
    	var data = {data :$scope.cartItems}
    	 $http({
	  	        url: 'checkout',
	  	        method: "POST",
	  	        headers: {
	  	            'Content-Type': 'application/json'
	  	        },
	  	        data: { 'checkout':data}
	  	        
	  	    })
	  	    .then(function(response) {
	  	    	 var aa = JSON.stringify(response.data);
	  	    	//$scope.confirmedItems
	  	    	   alert("added");
	  	    }, 
	  	    function(response) { // optional
	  	    	alert("no"+response);
	  	    });
    };

	
}]);