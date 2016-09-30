(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RequirementCtrl', RequirementCtrl);

	RequirementCtrl.$inject = ['requirementService', 'taskService', '$mdDialog', '$rootScope'];
	function RequirementCtrl(requirementService, taskService, $mdDialog, $rootScope) {

		var vm = this;

		vm.loading = false;
		vm.requirement = {};
		vm.requirements = [];

		vm.add = add;
		vm.detail = detail;
		vm.save = save;

		activate();

		/////////////////////

		function activate() {
			vm.loading = true;
			requirementService.getAll().then(function(response) {
				console.log(response.data);
				vm.requirements = response.data;
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				console.error(err);
				//TODO treat request error
				vm.loading = false;	
			});
		};

		function detail(req, ev) {
			$mdDialog.show({
				templateUrl: 'app/requirements-detail/requirements-detail.html',
				controller: 'RequirementDetailCtrl',
				controllerAs: 'vm',
				locals: {
					requirement: req
				},
				targetEvent: ev,
				clickOutsideToClose: true
			}).then(function(requirement) {
				vm.requirement = requirement;
				if(vm.requirement.indiceRisco === undefined) { vm.save(); }
				else { vm.changeState(); }
			});
		};

		function add(ev) {
			$mdDialog.show({
				templateUrl: 'app/requirements-add/requirements-add.html',
				controller: 'RequirementAddCtrl',
				controllerAs: 'vm',
				targetEvent: ev,
				clickOutsideToClose: true
			}).then(function(requirement) {
				vm.requirement = requirement;
				vm.save();
			});
		};

		function save() {
			vm.loading = true;
			if(vm.requirement.id === undefined) {
				vm.requirement.estado = {};
				vm.requirement.ultimaAlteracao = {};
				vm.requirement.estado.estado = 'CRIADO';
				vm.requirement.ultimaAlteracao.titulo = "Xpto";
				vm.requirement.ultimaAlteracao.descricao = "Xpto";
				vm.requirement.ultimaAlteracao.justificativa = "Xpto";
				vm.requirement.ultimaAlteracao.comprovacao = "http://localhost"
			}
			requirementService.post(vm.requirement).then(function(response) {
				if(angular.isArray(response.data)) {
					console.error(response);
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