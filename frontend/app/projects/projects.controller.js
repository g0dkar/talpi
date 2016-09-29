(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectsCtrl', ProjectsCtrl);

	ProjectsCtrl.$inject = ['projectService', '$state', '$cookieStore', '$rootScope', '$mdDialog'];
	function ProjectsCtrl(projectService, $state, $cookieStore, $rootScope, $mdDialog) {

		var vm = this;

		vm.loading = false;
		vm.project = {};
		vm.projects = [];
		vm.add = add;
		vm.hide = hide;
		vm.save = save;

		activate();

		/////////////////////

		function activate() {
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

		function add(ev) {
			$mdDialog.show({
				templateUrl: 'app/projects-add/projects-add.html',
				controller: 'ProjectsAddCtrl',
				controllerAs: 'vm',
				targetEvent: ev,
				clickOutsideToClose: true
			}).then(function(project) {
				vm.project = project;
				vm.save();
			});
		}

		function hide() {
			$mdDialog.hide();
		}

		function save() {
			vm.hide();
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