(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectCtrl', ProjectCtrl);

	ProjectCtrl.$inject = ['$rootScope', '$cookieStore', '$stateParams', '$state', 'projectService'];
	function ProjectCtrl($rootScope, $cookieStore, $stateParams, $state, projectService) {

		var vm = this;

		vm.loading = false;
		vm.project = null;

		activate();

		/////////////////////

		function activate() {
			vm.loading = true;
			projectService.get($stateParams.id).then(function(response) {
				$cookieStore.put('project', response.data);
				$rootScope.project = response.data;
				vm.project = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;
				$state.go('projects');
				return;
			});
			$state.go('project.requirement');
		};

	};

})(angular);