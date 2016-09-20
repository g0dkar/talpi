(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectConfigCtrl', ProjectConfigCtrl);

	ProjectConfigCtrl.$inject = ['projectService', '$rootScope'];
	function ProjectConfigCtrl(projectService, $rootScope) {


		var vm = this;

		vm.loading = false;
		vm.project = $rootScope.project;
		vm.save = save;

		function save() {
			vm.loading = true;
			projectService.post(vm.project).then(function(response) {
				$cookieStore.put('project', response.data);
				$rootScope.project = response.data;
				vm.project = response.data;
				vm.loading = false;
			}, function(err) {
				console.log('Erro no requisição');
				//TODO treat request error
				vm.loading = false;
			});
		};
	};

})(angular);