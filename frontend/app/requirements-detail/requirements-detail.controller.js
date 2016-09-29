(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RequirementDetailCtrl', RequirementDetailCtrl);

	RequirementDetailCtrl.$inject = ['$mdDialog', 'requirement'];
	function RequirementDetailCtrl($mdDialog, requirement) {

		var vm = this;

		vm.save = save;
		vm.cancel = cancel;
		vm.requirement = requirement || {};

		function save() {
			$mdDialog.hide(vm.requirement);
		};

		function cancel() {
			$mdDialog.cancel();
		};
	}
})(angular);