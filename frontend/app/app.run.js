(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.run(run);

	run.$inject = ['$rootScope', "$cookieStore", "$state", 'authService'];
	function run($rootScope, $cookieStore, $state, authService) {
		
		$rootScope.logout = logout;
		$rootScope.checking = false;
		$rootScope.project = $cookieStore.get('project') || null;
		$rootScope.globals = $cookieStore.get('globals') || null;
		$rootScope.$on("$stateChangeStart", changeState);

		function changeState(event, toState, toStateParams) {
			authService.check().then(function() {
				if ((toState.name != "login" && !$rootScope.globals) || !authService.check) {
					event.preventDefault();
					$state.go("login");
				}
			}, function() {
				logout();
			})
		};

		function logout() {
			authService.logout();
			$state.go('login');
		};

	};

})(angular);