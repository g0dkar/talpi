(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectCtrl', ProjectCtrl);

	ProjectCtrl.$inject = ['$rootScope', '$cookieStore', '$stateParams', 'projectService'];
	function ProjectCtrl($rootScope, $cookieStore, $stateParams, projectService) {

		var vm = this;

		vm.init = init;
		vm.project = null;

		function init() {
			if(!$stateParams.project) {
				projectService.get($stateParams.id).then(function(response) {
					$cookieStore.put('project', response.data);
					$rootScope.project = response.data;
					vm.project = response.data;
				}, function(err) {
					//TODO treat request error
					console.error('Erro na requisição');
				});
			} else {
				$cookieStore.put('project', $stateParams.project);
				$rootScope.project = $stateParams.project;
				vm.project = $stateParams.project;
			}
		};

	};

})(angular);