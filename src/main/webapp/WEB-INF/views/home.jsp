<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<script src="https://code.angularjs.org/1.4.2/angular.js"></script>
<script src="https://code.angularjs.org/1.4.2/angular-route.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular-resource/1.5.8/angular-resource.min.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.13/angular-ui-router.min.js"></script>
	<script src="<c:url value="/resources/js/app.js" />"></script>
	<script src="<c:url value="/resources/js/controller/LoginController.js" />"></script>
	<script src="<c:url value="/resources/js/controller/SearchController.js" />"></script>
	<script src="<c:url value="/resources/js/controller/BookController.js" />"></script>
	<script src="<c:url value="/resources/js/controller/SearchPatronController.js" />"></script>
	<script src="<c:url value="/resources/js/controller/CartController.js" />"></script>
	<script src="<c:url value="/resources/js/controller/UserProfileController.js" />"></script>
	
	<!-- <script src="../resources/js/app.js"></script> -->
	<!-- <script src="../resources/js/controller/LoginController.js"></script> -->
	<script src="<c:url value="/resources/libraries/jquery.js" />"></script>
	<script src="<c:url value="/resources/libraries/bootstrap.js" />"></script>
	<link href="<c:url value="/resources/css/kinglibrary.css" />" rel="stylesheet"> 
	<link href="<c:url value="/resources/css/bootstrap.css" />" rel="stylesheet">
	
	<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

	<title>Home</title>
</head>
<body ng-app="app" >
<div ng-controller="loginController">
  <div class="content">
       
  </div>
    
    
<!--  <nav class="navbar">
  		<div class="container-fluid">
  			<div class="navbar-header">
      			<a class="navbar-brand" href="#">
      				<h2 class="brand"><b>SJSU KING LIBRARY</b></h2>
      			</a>
    		</div>
  		  	<ul class="nav navbar-nav">
				<li class="navbar-text">
					<p class="navbar-text">
						<h2 class="brand"><a href="#" data-toggle="modal" data-target="#myModal">Sign Up</a></h2>
					</p>
				</li>
			</ul>
  		</div>
	</nav> -->
	
	
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle</span>
        
      </button>
      <a class="navbar-brand" href="#">
    <!--   <img src="../images/logo.png" style="width:30px; height:30px; display:inline"/> -->
       <img class="logo" style="width:70px; height:40px; display:inline"/>
      <span>SJSU Library</span>
      </a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <!-- <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li> -->
      </ul>
      
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#/register">Sign up</a></li>
        <li><a href="#/login">Login</a></li>
       <li> 
       <input type="datetime-local"  ng-model="timeValue.value">
       <button ng-click="setAppTime()">Set Date</button>
       </li>
        
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
	
	
	
<div ui-view>

</div>

</div>
</body>
</html>
