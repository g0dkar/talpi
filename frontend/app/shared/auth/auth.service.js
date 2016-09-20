(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('authService', authService);

	authService.$inject = ['API_URL', '$http', '$cookieStore', '$rootScope', '$q'];
	function authService(API_URL, $http, $cookieStore, $rootScope, $q) {

		var service = {};
		
		service.login = login;
		service.logout = logout;
		service.check = check;

		return service;

		function login(user) {
			return $http.post(API_URL + '/usuario/login', user);
		};

		function logout() {
			$rootScope.globals = null;
			$cookieStore.remove('globals');
		};

		function check() {
			return $http.get(API_URL + '/usuario/check');
		};
	};

})(angular);