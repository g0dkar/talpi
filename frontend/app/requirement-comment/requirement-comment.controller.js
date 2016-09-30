(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RequirementCommentsCtrl', RequirementCommentsCtrl);

	RequirementCommentsCtrl.$inject = ['$mdDialog', 'commentService', '$rootScope', '$state', '$cookieStore'];
	function RequirementCommentsCtrl($mdDialog, commentService, $rootScope, $state, $cookieStore) {

		var vm = this;

		vm.loading = false;
		vm.comment = {};
		vm.comments = [];

		vm.save = save;
		vm.update = update;
		vm.remove = remove;

		activate();

		/////////////////////

		function activate() {
			commentService.get('requirement', $rootScope.requirement.id).then(function(response) {
				console.log('Response...', response);
				if(response.data !== undefined) {
					vm.comments = response.data;
				}
			}, function(err) {
				console.error(err);
				//TODO treat request error
				console.error('Erro na requisição');
			});
		}


		function save() {
			vm.loading = true;
			vm.comment.usuario = $rootScope.globals.nome;
			commentService.post('requirement', $rootScope.requirement.id, angular.copy(vm.comment)).then(function(response) {
				vm.comments = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;	
			});
		};

		function update(comment) {
			var index = $rootScope.requirementComments[$rootScope.requirement.id].indexOf(comment);
			commentService.put('requirement', comment, index).then(function(response) {
				vm.comments = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;	
			});
		};

		function remove(comment) {
			var index = $rootScope.requirementComments[$rootScope.requirement.id].indexOf(comment);
			commentService.remove('requirement', $rootScope.requirement.id, index).then(function(response) {
				vm.comments = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;	
			});
		};
	}
})(angular);