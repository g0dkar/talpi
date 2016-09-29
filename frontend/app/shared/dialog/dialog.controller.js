(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RequirementDetailCtrl', RequirementDetailCtrl);

	RequirementDetailCtrl.$inject = ['$mdDialog', 'obj'];
	function RequirementDetailCtrl($mdDialog, obj) {

		var vm = this;

		vm.save = save;
		vm.cancel = cancel;
		vm.obj = obj || {};

		function finish() {
			$mdDialog.hide(vm.obj);
		};

		function cancel() {
			$mdDialog.cancel();
		};
	}
})(angular);