(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectMembersCtrl', ProjectMembersCtrl);

	ProjectMembersCtrl.$inject = ['userService', 'projectService'];
	function ProjectMembersCtrl(userService, projectService) {

		var vm = this;

		vm.init = init;
		vm.loading = false;
		vm.users = [];
		vm.projectUsers = [];
		
		vm.save = save;

		function init() {
			vm.loading = true;
			userService.getAll().then(function(response) {
				vm.users = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;
			});
		};

		function save() {
			projectService.addMembros(vm.projectService).then(function(response) {

			}, function(err) {

			});
		};

	};

})(angular);