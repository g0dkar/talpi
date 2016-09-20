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
			console.log('Changing...', toState.name);
			console.log('Globals...', $rootScope.globals);
			if (toState.name != "login" && !$rootScope.globals) {
				event.preventDefault();
				$state.go("login");
			}
		};

		function logout() {
			authService.logout();
			$state.go('login');
		};

	};

})(angular);