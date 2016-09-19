(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.run(run);

	run.$inject = ["$rootScope", "$state"];
	function run($rootScope, $state) {
		
		$rootScope.endpoint = "http://localhost:8080";
		$rootScope.loggedUser = null;
		$rootScope.checkLogin = false;
		$rootScope.lastCheck = 0;
		$rootScope.doLogin = false;
		$rootScope.setLogin = setLogin;
		$rootScope.$on("$stateChangeStart", changeState);

		function setLogin(val) { $rootScope.doLogin = val; };
		function changeState(event, toState, toStateParams) {
			if (toState.name != "login" && !$rootScope.loggedUser) {
				console.log(event);
				event.preventDefault();
				$rootScope._toState = toState;
				$rootScope._toStateParams = toStateParams;
				$state.go("login");
			}
		};
	};

})(angular);