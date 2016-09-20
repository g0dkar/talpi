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
				vm.loading = false;
				vm.projects = response.data;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
			});
		}

		function save() {
			projectService.post(vm.project).then(function(response) {
				if(angular.isArray(response.data)) {
					//TODO treat invalid data
					console.error(response.data[0].message);
				} else {
					$cookieStore.put('project', response.data);
					$rootScope.project = response.data;
					$state.go("project", { id: response.data.id });
				}

			}, function(err) {
				//TODO treat request error
				console.error('Erro na requisição');
			});
		}
	};

})(angular);