(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectCtrl', ProjectCtrl);

	ProjectCtrl.$inject = ['$rootScope', '$cookieStore', '$stateParams', '$state', 'projectService'];
	function ProjectCtrl($rootScope, $cookieStore, $stateParams, $state, projectService) {

		var vm = this;

		vm.init = init;
		vm.loading = false;
		vm.project = null;

		function init() {
			vm.loading = true;
			if(!$stateParams.project) {
				projectService.get($stateParams.id).then(function(response) {
					$cookieStore.put('project', response.data);
					$rootScope.project = response.data;
					vm.project = response.data;
					vm.loading = false;
				}, function(err) {
					console.error('Erro na requisição');
					//TODO treat request error
					vm.loading = false;
					$state.go('project_list');
					return;
				});
			} else {
				$cookieStore.put('project', $stateParams.project);
				$rootScope.project = $stateParams.project;
				vm.project = $stateParams.project;
				vm.loading = false;
			}
			$state.go('project.frame');
		};

	};

})(angular);