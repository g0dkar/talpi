(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('MembersCtrl', MembersCtrl);

	MembersCtrl.$inject = ['userService', 'projectService', '$rootScope', '$cookieStore', '$mdDialog'];
	function MembersCtrl(userService, projectService, $rootScope, $cookieStore, $mdDialog) {

		var vm = this;

		vm.loading = false;
		vm.editMode = false;
		vm.members = [];
		vm.newMembers = [];
		vm.roles = ['PM', 'MEMBRO', 'CLIENTE'];
		
		vm.reset = reset;
		vm.remove = remove;
		vm.edit = edit;
		vm.add = add;
		vm.save = save;

		activate();

		/////////////////////

		function activate() {
			console.log($rootScope.project);
			vm.members = $rootScope.project.usuarios;
		};

		function remove(member) {
			var index = vm.members.indexOf(member);
			member.deleted = member.deleted ? false : true;
			vm.members[index] = member;
		}

		function reset() {
			vm.editMode = false;
			vm.newMembers = [];
			for(var index in vm.members) {
				vm.members[index].deleted = false;
			}
		}

		function edit() {
			vm.editMode = vm.editMode ? false : true;
		}

		function add(ev) {
			$mdDialog.show({
				templateUrl: 'app/members-add/members-add.html',
				controller: 'MembersAddCtrl',
				controllerAs: 'vm',
				targetEvent: ev,
				clickOutsideToClose: true
			}).then(function(users) {
				vm.newMembers = vm.members.concat(users);
				vm.save();
			});
		}

		function save() {
			var projectMembers = vm.newMembers.length > 0 ? angular.copy(vm.newMembers) : angular.copy(vm.members);
			_delete(projectMembers);
			if(_validate(projectMembers)) {
				projectService.addMembros(projectMembers).then(function(response) {
					vm.members = projectMembers;
					$rootScope.project.usuarios = vm.members;
					$cookieStore.put('project', $rootScope.project);
					vm.editMode = false;
				}, function(err) {
					console.error(err);
					//TODO treat request error
				});
			} else {
				//TODO treat missing Project Manager
				console.error('O projeto deve conter pelo menos um gerente de projetos');
			}
			vm.reset();
		};

		function _delete(projectMembers) {
			for(var index in projectMembers) {
				if(projectMembers[index].deleted === true) {
					projectMembers.splice(index, 1);
				}
			}
		}

		function _validate(projectMembers) {
			for(var index in projectMembers) {
				if(projectMembers[index].deleted === true) {
					projectMembers.splice(index, 1);
				}
				if(projectMembers[index].papel === 'PM') return true;
			};
			return false;
		}

	};

})(angular);