(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ConfigCtrl', ConfigCtrl);

	ConfigCtrl.$inject = ['projectService', '$rootScope', '$cookieStore', '$state'];
	function ConfigCtrl(projectService, $rootScope, $cookieStore, $state) {

		var vm = this;

		vm.loading = false;
		vm.project = {};

		vm.cancel = cancel; 
		vm.save = save;

		activate();

		/////////////////////

		function activate() {
			projectService.get($rootScope.project.id).then(function(response) {
				console.log(response.data);
				vm.project = response.data;
			}, function(err) {
				console.error(err);
				//TODO treat request error;
			})
		}

		function save() {
			vm.loading = true;
			projectService.post(vm.project).then(function(response) {
				projectService.get($rootScope.project.id).then(function(response) {
					$cookieStore.put('project', response.data);
					$rootScope.project = response.data;
					vm.project = response.data;
					vm.loading = false;
					$state.go('project.requirements');
				}, function(err) {
					console.error(err);
					//TODO treat request error
				});
			}, function(err) {
				console.log('Erro no requisição');
				//TODO treat request error
				vm.loading = false;
			});
		};

		function cancel() {
			$state.go('project.requirement');
		}
	};

})(angular);