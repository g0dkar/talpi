(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjetosListController', ProjetosListController);
	
	ProjetosListController.$inject = ["$rootScope", "$scope", "$http", "$state", "$window"];
	function ProjetosListController($rootScope, $scope, $http, $state, $window) {
		$scope.atualizar = function () {
			$scope.loading = true;
			$http.get($rootScope.endpoint + "/api/projeto/lista").then(function (response) {
				$scope.loading = false;
				$scope.projetos = response.data;
			}, function () {
				$scope.loading = false;
				$window.alert("Erro ao pegar lista de projetos");
			});
		};
		
		$scope.atualizar();
	};
	
})(angular);