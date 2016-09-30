(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('TaskAddCtrl', TaskAddCtrl);

	TaskAddCtrl.$inject = ['$mdDialog'];
	function TaskAddCtrl($mdDialog) {

		var vm = this;

		vm.save = save;
		vm.cancel = cancel;
		vm.task = {};

		function save() {
			$mdDialog.hide(vm.task);
		};

		function cancel() {
			$mdDialog.cancel();
		};
	}
})(angular);