(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('taskService', taskService);

	taskService.$inject = ['API_URL', '$http', '$rootScope'];
	function taskService(API_URL, $http, $rootScope) {

		var service = {};

		service.getAll = getAll;
		service.get = get;
		service.post = post;
		service.put = put;
		service.remove = remove;

		return service;

		function getAll(reqId) {
			return $http.get(API_URL + '/tarefa/' + $rootScope.project.id + '/' + $rootScope.requirement.id + '/lista');
		};

		function get(reqId, id) {
			return $http.get(API_URL + '/tarefa/' + $rootScope.project.id + '/' + $rootScope.requirement.id + '/' + id);
		};

		function post(entity) {
			return $http.post(API_URL + '/tarefa/' + $rootScope.project.id + '/' + $rootScope.requirement.id, { tarefa: entity });
		};

		function put(id, entity) {

		};

		function remove(id) {

		};
	};

})(angular);