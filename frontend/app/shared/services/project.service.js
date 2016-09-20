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

		service.addMembros = addMembros;

		return service;

		function getAll() {
			return $http.get(API_URL + '/projeto/lista');
		};

		function get(id) {
			return $http.get(API_URL + '/projeto/' + id);
		};

		function post(entity) {
			return $http.post(API_URL + '/projeto/editar', entity);
		};

		function put(id, entity) {

		};

		function remove(id) {

		};

		function addMembros(id, entity) {
			return $http.post(API_URL + '/projeto/' + id + '/membros', entity);
		}
	};

})(angular);