(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('projectService', projectService);

	projectService.$inject = ['API_URL', '$http'];
	function projectService(API_URL, $http) {

		var service = {};

		service.getAll = getAll;
		service.get = get;
		service.post = post;
		service.put = put;
		service.remove = remove;

		return service;

		function getAll() {
			return $http.get(API_URL + '/api/projeto/lista');
		};

		function get(id) {
			return $http.get(API_URL + '/api/projeto/' + id);
		};

		function post(entity) {
			return $http.post(API_URL + '/api/projeto/editar', entity);
		};

		function put(id, entity) {

		};

		function remove(id) {

		};
	};

})(angular);