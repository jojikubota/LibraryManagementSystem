app.controller('loginController',["$scope", "$http","$window","$state","$stateParams", "$filter", function($scope,$http,$window,$state,$stateParams,$filter) {

	$scope.timeValue = {
	        value: new Date(2016, 12, 20, 14, 57)
	 };
	
	 $http({
	        url: 'getapptime',
	        method: "GET",
	        headers: {
	            'Content-Type': 'application/json'
	        }
	    })
	    .then(function(response) {

	    	  if(response.data == undefined || response.data =='')
	    		  {
	    		  //$scope.timeValue.value =  $filter('date')(new Date(), "yyyy-MM-dd hh:mm:ss");
	    		  $scope.timeValue.value = new Date();
	    		  }
	    	  else
	    		  {
	    		 // $scope.timeValue.value = $filter('date')(new Date(response.data.date), "yyyy-MM-dd hh:mm:ss");
	    	  $scope.timeValue.value = new Date(response.data.date);
	    		  }
	    }, 
	    function(response) { // optional
	    	$scope.timeValue.value = new Date();
	    });
	
	
	
	$scope.setAppTime = function()
	{
		
		$scope.timeValue.value = $filter('date')( $scope.timeValue.value, 'yyyy-MM-dd HH:mm:ss Z')
		 $http({
	 	        url: 'apptime',
	 	        method: "POST",
	 	        headers: {
	 	            'Content-Type': 'application/json'
	 	        },
	 	        data: { 'time' : $scope.timeValue.value}
	 	        
	 	    })
	 	    .then(function(response) {
	 	    	 $scope.timeValue.value = new Date(response.data.date);
	 	    	 
	 	    	 $http({
	 		        url: 'setEmailTag',
	 		        method: "GET",
	 		        headers: {
	 		            'Content-Type': 'application/json'
	 		        }
	 		    })
	 		    .then(function(response) {}, 
	 		    function(response) {});
	 	    	 
	 	    	 
	 	    }, 
	 	    function(response) { // optional
	 	    	 $scope.timeValue.value = new Date();
	 	    });
		
		
	}
	$scope.hideErrorDiv = function() {
		document.getElementById('errordiv').style.display = 'none';
	}

	$scope.createErrorDiv = function(message) {errordiv}
	

	
	$scope.validate = function(user) {

		var universityId = user.universityId;
		if(universityId.length < 6 || universityId.length > 6)
			{
			$scope.createErrorDiv("Please provide university id of 6 digits");
			}
		else
			{
			  $http({
	    	        url: 'register',
	    	        method: "POST",
	    	        headers: {
	    	            'Content-Type': 'application/json'
	    	        },
	    	        data: { 'user' : user }
	    	        
	    	    })
	    	    .then(function(response) {
	    	    	$scope.hideErrorDiv();
	    	            $state.go("verification", { username: user.username });
	    	    }, 
	    	    function(response) { // optional
	    	    	alert("no"+response);
	    	    });
	  
			}
		
		
	}
	
    $scope.register = function(user)
    {
    	$scope.validate(user);

    }
    
    
    $scope.confirm = function(token)
    {
    	 $scope.username = $stateParams.username;
    	 
    	 $http({
 	        url: 'confirm',
 	        method: "POST",
 	        headers: {
 	            'Content-Type': 'application/json'
 	        },
 	        data: { 'token' : token, 'username': $scope.username }
 	        
 	    })
 	    .then(function(response) {
 	    	   $state.go("login");
 	    }, 
 	    function(response) { // optional
 	    	alert("no"+response);
 	    });

    	
    }
    
    $scope.login = function(user)
    {
    	 $http({
  	        url: 'login',
  	        method: "POST",
  	        headers: {
  	            'Content-Type': 'application/json'
  	        },
  	        data: { 'email':user.email ,'password' : user.password }
  	        
  	    })
  	    .then(function(response) {
  	    	
  	    	var s = user.email
  	    	var d = s.indexOf("@sjsu.edu")
  	    	if(d == -1)
  	    		{
  	    		$state.go("searchpatron", { username: user.email });
  	    		}
  	    	else
  	    		{
  	    		$state.go("search", { username: user.email });
  	    		}
  	    }, 
  	    function(response) { // optional
  	    	alert("no"+response);
  	    });
    }
    
    

}]);
