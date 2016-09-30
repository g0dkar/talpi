(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RequirementsDetailCtrl', RequirementsDetailCtrl);

	RequirementsDetailCtrl.$inject = ['$mdDialog', 'requirementService', 'taskService', '$rootScope', '$stateParams', '$state', '$cookieStore'];
	function RequirementsDetailCtrl($mdDialog, requirementService, taskService, $rootScope, $stateParams, $state, $cookieStore) {

		var vm = this;

		vm.loading = false;
		vm.requirement = null;
		vm.tasks = [];
		vm.task = {};

		vm.addTask = addTask;
		vm.detailTask = detailTask;
		vm.changeState = changeState;
		vm.save = save;

		activate();

		/////////////////////

		function activate() {
			requirementService.get($stateParams.req).then(function(response) {
				$cookieStore.put('requirement', response.data);
				$rootScope.requirement = response.data;
				vm.requirement = response.data;
				taskService.getAll().then(function(response) {
					vm.tasks = response.data;
					_stylizer(vm.tasks);				
				}, function(err) {
					console.error(err);
					//TODO treat request error
					console.error('Erro na requisição');
				})
			}, function(err) {
				console.error(err);
				//TODO treat request error
				console.error('Erro na requisição');
			});
		}

		function addTask(ev) {
			$mdDialog.show({
				templateUrl: 'app/task-add/task-add.html',
				controller: 'TaskAddCtrl',
				controllerAs: 'vm',
				targetEvent: ev,
				clickOutsideToClose: true
			}).then(function(task) {
				vm.task = task;
				vm.save();
			});
		};

		function detailTask() {

		};


		function save(index) {
			vm.loading = true;
			if(vm.task.estado === undefined) {
				vm.task.estado = {};
				vm.task.estado.estado = 'CRIADO';
			}
			taskService.post(vm.task).then(function(response) {
				console.log(response.data)
				if(angular.isArray(response.data)) {
					console.error(response);
					console.error(response.data[0].message);
					//TODO treat invalid data
					vm.loading = false;
				} else {
					if(index !== undefined) {
						vm.tasks[index] = response.data;
					} else {
						vm.tasks.push(response.data);
					}
					_stylizer(vm.tasks);
					vm.loading = false;
				}

			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
				vm.loading = false;	
			});
		};

		function changeState(task) {
			vm.task = task;
			vm.task.estado.estado = _verify(vm.task);
			vm.save(vm.tasks.indexOf(task));
		};

		function _stylizer(tasks) {
			tasks.forEach(function(task) {
				switch(input) {
					case 'CRIADO':
						task.criado = true;
					case 'INICIADO':
						task.iniciado = true
					case 'PRONTO_HOMOLOGACAO':
						task.pronto = true;
					case 'CONCLUIDO':
						task.concluido = true
					case 'NAO_HOMOLOGADO':
						task.nao = true;
					case 'PAUSADO':
						task.psd = true;
				}
			})
		}

		function _verify(task) {
			switch(task.estado.estado) {
				case 'CRIADO':
					return 'INICIADO';
				case 'INICIADO':
					return 'PRONTO_HOMOLOGACAO';
				case 'PRONTO_HOMOLOGACAO':
					return 'CONCLUIDO';
			}
		};
	}
})(angular);