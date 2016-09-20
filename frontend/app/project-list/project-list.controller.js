(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectListCtrl', ProjectListCtrl);

	ProjectListCtrl.$inject = ['projectService', '$state', '$cookieStore', '$rootScope'];
	function ProjectListCtrl(projectService, $state, $cookieStore, $rootScope) {

		var vm = this;

		vm.init = init;
		vm.loading = false;
		vm.project = {};
		vm.projects = [];
		vm.save = save;

		function init() {
			vm.loading = true;
			projectService.getAll().then(function(response) {
				vm.projects = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;
			});
		}

		function save() {
			vm.loading = true;
			projectService.post(vm.project).then(function(response) {
				if(angular.isArray(response.data)) {
					console.error(response.data[0].message);
					//TODO treat invalid data
					vm.loading = false;
				} else {
					$cookieStore.put('project', response.data);
					$rootScope.project = response.data;
					vm.loading = false;
					$state.go("project", { id: response.data.id });
				}

			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;
			});
		}
	};

})(angular);