(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('voteService', voteService);

	voteService.$inject = ['API_URL', '$http', '$rootScope', '$cookieStore', '$q'];
	function voteService(API_URL, $http, $rootScope, $cookieStore, $q) {

		var service = {};

		service.getAll = getAll;
		service.get = get;
		service.post = post;
		service.put = put;
		service.remove = remove;

		return service;

		function getAll(type, id) {
			return $q(function(resolve, reject) {
				switch(type) {
					case 'task':
						resolve({data: $rootScope.taskVotes[id]});
					case 'requirement':
						resolve({data: $rootScope.requirementVotes[id]});
				}
				reject('Informe o tipo correto');
			});
		};

		function get(type, id, index) {
			return $q(function(resolve, reject) {
				switch(type) {
					case 'task':
						resolve({data: $rootScope.taskVotes[id][index]});
					case 'requirement':
						resolve({data: $rootScope.requirementVotes[id][index]});
				}
				reject('Informe o tipo correto');
			});
		};

		function post(type, entity) {
			return $q(function(resolve, reject) {
				switch(type) {
					case 'task':
						$rootScope.taskVotes.push(entity);
						$cookieStore.put('taskVotes', $rootScope.taskVotes);
						resolve({data: entity});
					case 'requirement':
						$rootScope.requirementVotes.push(entity);
						$cookieStore.put('requirementVotes', $rootScope.requirementVotes);
						resolve({data: entity});
				}
				reject('Informe o tipo correto');
			});
		};

		function put(id, entity) {

		};

		function remove(type, id, index) {
			return $q(function(resolve, reject) {
				switch(type) {
					case 'task':
						$rootScope.taskVotes.splice(index, 1);
						$cookieStore.put('taskVotes', $rootScope.taskVotes);
						resolve({data: $rootScope.taskVotes});
					case 'requirement':
						$rootScope.requirementVotes.splice(index, 1);
						$cookieStore.put('requirementVotes', $rootScope.requirementVotes);
						resolve({data: $rootScope.requirementVotes});
				}
				reject('Informe o tipo correto');
			});
		};
	};

})(angular);