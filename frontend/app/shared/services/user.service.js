(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('userService', userService);

	userService.$inject = ['API_URL', '$http', '$rootScope'];
	function userService(API_URL, $http, $rootScope) {

		var service = {};

		service.getAll = getAll;
		service.get = get;
		service.post = post;
		service.put = put;
		service.remove = remove;

		return service;

		function getAll() {
			return $http.get(API_URL + '/usuario/perfil/');
		};

		function get(id) {
			return $http.get(API_URL + '/usuario/perfil/' + id);
		};

		function post(entity) {
			return $http.post(API_URL + '/usuario/perfil/', entity);
		};

		function put(id, entity) {

		};

		function remove(id) {

		};
	};

})(angular);