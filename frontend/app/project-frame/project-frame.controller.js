(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectFrameCtrl', ProjectFrameCtrl);

	ProjectFrameCtrl.$inject = ['requirementService', 'taskService'];
	function ProjectFrameCtrl(requirementService, taskService) {

		var vm = this;

		vm.init = init;
		vm.loading = false;
		vm.requirement = {};
		vm.requirements = [];

		vm.save = save;

		function init() {
			vm.loading = true;
			requirementService.getAll().then(function(response) {
				vm.requirements = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;	
			});
		};

		function save() {
			vm.loading = true;
			requirementService.post(vm.project).then(function(response) {
				if(angular.isArray(response.data)) {
					console.error(response.data[0].message);
					//TODO treat invalid data
					vm.loading = false;
				} else {
					vm.requirements.push(response.data);
					vm.loading = false;
				}

			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;	
			});
		}
	};

})(angular);