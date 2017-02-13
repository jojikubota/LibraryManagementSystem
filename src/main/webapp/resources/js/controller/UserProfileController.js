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
		    	
		    	
	    	   // 	$scope.searchResults =  JSON.stringify(response.data);
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
		   	 
		   	 
	    	 //   	$scope.searchResults =  JSON.stringify(response.data);
	    	  //  	$scope.data = response.data
	    	    	$scope.message = "";
	    	    	
	    	    	if($scope.data[0].renewableBooks.length > 0)
	    	    		{
	    	    		$scope.message += $scope.data[0].messageString;
	    	    		}
	    	    	if($scope.data[1].renewableBooks.length > 0)
	    	    		{
	    	    		$scope.message += $scope.data[0].messageString;
	    	    		}
	    	    	if($scope.data[2].renewableBooks.length > 0)
	    	    		{
	    	    		$scope.message += $scope.data[0].messageString;
	    	    		}
		    }, 
		    function(response) { // optional
		    //	$scope.reset();
		    });
		 
		 
	}
	
	
}]);
