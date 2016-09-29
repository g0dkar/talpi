(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('MembersAddCtrl', MembersAddCtrl);

	MembersAddCtrl.$inject = ['userService', '$mdDialog'];
	function MembersAddCtrl(userService, $mdDialog) {

		var vm = this;

		vm.loading = false;
		vm.members = [];
		vm.users = [];

		vm.check = check;
		vm.save = save;
		vm.cancel = cancel;

		activate();

		/////////////////////

		function activate() {
			vm.loading = true;
			userService.getAll().then(function(response) {
				vm.users = response.data;
				vm.loading = false;
			}, function(err) {
				console.error(err);
				//TODO treat request error
				vm.loading = false;
			});
		}

		function check(user) {
			var index = vm.members.indexOf(user);
			if(index === -1) {
				vm.members.push(user);
			} else {
				vm.members.splice(index, 1);
			}
		}

		function save() {
			$mdDialog.hide(wrapper(vm.members));
		};

		function cancel() {
			$mdDialog.cancel();
		};

		function wrapper(members) {
			var projectMembers = [];
			members.forEach(function(member) {
				projectMembers.push({ usuario: member, papel: 'MEMBRO' })
			});
			return projectMembers;
		}
	}
})(angular);