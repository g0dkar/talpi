(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('userService', userService);

	userService.$inject = [];
	function userService() {

		var service = {};

		service.getAll = getAll;
		service.get = get;
		service.post = post;
		service.put = put;
		service.remove = remove;

		return service;

		function getAll() {

		};

		function get(id) {

		};

		function post(entity) {

		};

		function put(id, entity) {

		};

		function remove(id) {

		};
	};

})(angular);