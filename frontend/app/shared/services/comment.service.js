(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.factory('commentService', commentService);

	commentService.$inject = ['API_URL', '$http', '$rootScope', '$cookieStore', '$q'];
	function commentService(API_URL, $http, $rootScope, $cookieStore, $q) {

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
						if($rootScope.taskComments[id] === undefined) $rootScope.taskComments[id] = [];
						resolve({data: $rootScope.taskComments[id]});
					case 'requirement':
						if($rootScope.requirementComments[id] === undefined) $rootScope.requirementComments[id] = [];
						console.log('Id...', id);
						console.log('Comments...', $rootScope.requirementComments[id])
						resolve({data: $rootScope.requirementComments[id]});
				}
				reject('Informe o tipo correto');
			});
		};

		function get(type, id, index) {
			return $q(function(resolve, reject) {
				switch(type) {
					case 'task':
						resolve({data: $rootScope.taskComments[id] === undefined ? {} : $rootScope.taskComments[id][index]});
					case 'requirement':
						resolve({data: $rootScope.requirementComments[id] === undefined ? {} : $rootScope.requirementComments[id][index]});
				}
				reject('Informe o tipo correto');
			});
		};

		function post(type, id, entity) {
			return $q(function(resolve, reject) {
				switch(type) {
					case 'task':
						if($rootScope.taskComments[id] === undefined) $rootScope.taskComments[id] = [];
						$rootScope.taskComments[id].push(entity);
						$cookieStore.put('taskComments', $rootScope.taskComments);
						resolve({data: $rootScope.taskComments[id] === undefined ? [] : $rootScope.taskComments[id]});
					case 'requirement':
						if($rootScope.requirementComments[id] === undefined) $rootScope.requirementComments[id] = [];
						$rootScope.requirementComments[id].push(entity);
						$cookieStore.put('requirementComments', $rootScope.requirementComments);
						resolve({data: $rootScope.requirementComments[id] === undefined ? [] : $rootScope.requirementComments[id]});
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
						$rootScope.taskComments[id].splice(index, 1);
						$cookieStore.put('taskComments', $rootScope.taskComments);
						resolve({data: $rootScope.taskComments[id] === undefined ? [] : $rootScope.taskComments[id]});
					case 'requirement':
						$rootScope.requirementComments[id].splice(index, 1);
						$cookieStore.put('requirementComments', $rootScope.requirementComments);
						resolve({data: $rootScope.requirementComments[id] === undefined ? [] : $rootScope.requirementComments[id]});
				}
				reject('Informe o tipo correto');
			});
		};
	};

})(angular);