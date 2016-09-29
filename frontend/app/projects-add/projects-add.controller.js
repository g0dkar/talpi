(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjectsAddCtrl', ProjectsAddCtrl);

	ProjectsAddCtrl.$inject = ['$mdDialog'];
	function ProjectsAddCtrl($mdDialog) {

		var vm = this;

		vm.save = save;
		vm.cancel = cancel;
		vm.project = {};

		function save() {
			$mdDialog.hide(vm.project);
		};

		function cancel() {
			$mdDialog.cancel();
		};
	}
})(angular);