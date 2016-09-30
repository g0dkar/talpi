(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.run(run);

	run.$inject = ['$rootScope', "$cookieStore", "$state", 'authService'];
	function run($rootScope, $cookieStore, $state, authService) {
		
		$rootScope.logout = logout;
		$rootScope.checking = false;

		$rootScope.taskVotes = $cookieStore.get('taskVotes') || {};
		$rootScope.taskComments = $cookieStore.get('taskComments') || {};
		$rootScope.requirementVotes = $cookieStore.get('requirementVotes') || {};
		$rootScope.requirementComments = $cookieStore.get('requirementComments') || {};

		$rootScope.globals = $cookieStore.get('globals') || null;
		$rootScope.project = $cookieStore.get('project') || null;
		$rootScope.requirement = $cookieStore.get('requirement') || null;
		$rootScope.$on("$stateChangeStart", changeState);

		function changeState(event, toState, toStateParams) {
			authService.check().then(function() {
				if ((toState.name != "login" && !$rootScope.globals)) {
					event.preventDefault();
					$state.go("login");
				} else if((!/project.requirement[^s]/g.test(toState.name) && $rootScope.globals))  {
					resetRequirement();
				}
			}, function() {
				logout();
			})
		};

		function logout() {
			authService.logout();
			$state.go('login');
		};

		function resetRequirement() {
			$rootScope.requirement = null;
			$cookieStore.remove('requirement');
		};

	};

})(angular);