(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RequirementAddCtrl', RequirementAddCtrl);

	RequirementAddCtrl.$inject = ['$mdDialog'];
	function RequirementAddCtrl($mdDialog) {

		var vm = this;

		vm.save = save;
		vm.cancel = cancel;
		vm.requirement = {};

		function save() {
			$mdDialog.hide(vm.requirement);
		};

		function cancel() {
			$mdDialog.cancel();
		};
	}
})(angular);