(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('authService', authService);

	authService.$inject = ['API_URL', '$http', '$cookieStore', '$rootScope'];
	function authService(API_URL, $http, $cookieStore, $rootScope) {

		var service = {};
		
		service.login = login;
		service.logout = logout;

		return service;

		function login(user) {
			return $http.post(API_URL + '/api/usuario/login', user);
		};

		function logout() {
			$rootScope.globals = null;
			$cookieStore.remove('globals');
		};
	};

})(angular);