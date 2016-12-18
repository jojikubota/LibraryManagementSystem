app.controller('userProfileController',["$scope", "$http","$window","$state","$stateParams","$rootScope", function($scope, $http,$window,$state,$stateParams,$rootScope) {
	  
	$scope.books = JSON.parse($stateParams.books);
	
	
	$scope.returnSelectedBooks = function(book)
	{
		$scope.dataList = [];
		 angular.forEach(book,function(data){
             if(data.selection !=undefined ){
            	 if(data.selection == true)
            		 {
            		 $scope.dataList.push(data);
            		 }
             }
         });  
		 
		 
		 $http({
		        url: 'return',
		        method: "DELETE",
		        headers: {
		            'Content-Type': 'application/json'
		        },
		        data : {
		        	'books' : $scope.dataList
		        }
		        
		    })
		    .then(function(response) {
	    	    	$scope.searchResults =  JSON.stringify(response.data);
	    	    	//$state.go("userprofile", { books: $scope.searchResults});
		    }, 
		    function(response) { // optional
		    //	$scope.reset();
		    });
	}
	
	$scope.renewSelectedBooks = function(book)
	{
		$scope.dataList = [];
		 angular.forEach(book,function(data){
            if(data.selection !=undefined ){
           	 if(data.selection == true)
           		 {
           		 $scope.dataList.push(data);
           		 }
            }
        });  
		 
		 $http({
		        url: 'renew',
		        method: "POST",
		        headers: {
		            'Content-Type': 'application/json'
		        },
		        data : {
		        	'books' : $scope.dataList
		        }
		        
		    })
		    .then(function(response) {
	    	    	$scope.searchResults =  JSON.stringify(response.data);
	    	    //	$state.go("userprofile", { books: $scope.searchResults});
		    }, 
		    function(response) { // optional
		    //	$scope.reset();
		    });
		 
		 
	}
	
	
}]);
