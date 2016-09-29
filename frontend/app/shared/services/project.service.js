(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('projectService', projectService);

	projectService.$inject = ['API_URL', '$http', '$rootScope'];
	function projectService(API_URL, $http, $rootScope) {

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

		function addMembros(entity) {
			return $http.post(API_URL + '/projeto/' + $rootScope.project.id + '/membros', { membros: entity });
		}
	};

})(angular);