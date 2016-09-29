(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('requirementService', requirementService);

	requirementService.$inject = ['API_URL', '$http', '$rootScope'];
	function requirementService(API_URL, $http, $rootScope) {

		var service = {};

		service.getAll = getAll;
		service.get = get;
		service.post = post;
		service.put = put;
		service.remove = remove;

		return service;

		function getAll() {
			return $http.get(API_URL + '/requisito/' + $rootScope.project.id + '/lista');
		};

		function get(id) {
			return $http.get(API_URL + '/requisito/' + $rootScope.project.id + '/' + id);
		};

		function post(entity) {
			return $http.post(API_URL + '/requisito/' + $rootScope.project.id + '/editar', { requisito: entity, comprovacao: "http://localhost", justificativa: "Xpto" });
		};

		function put(id, entity) {

		};

		function remove(id) {

		};
	};

})(angular);