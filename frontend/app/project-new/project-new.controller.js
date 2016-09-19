(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('ProjetosNovoController', ProjetosNovoController);
	
	ProjetosNovoController.$inject = ["$rootScope", "$scope", "$http", "$state", "$window"];
	function ProjetosNovoController($rootScope, $scope, $http, $state, $window) {
		$scope.projeto = { congelado: false };
		$scope.novoProjeto = function (form, event) {
			event.preventDefault();
			
			if (form.$valid) {
				$http.post($rootScope.endpoint + "/api/projeto/editar", $scope.projeto).then(function (response) {
					if (angular.isArray(response.data)) {
						$window.alert("Por favor, verifique os campos e tente novamente.");
					}
					else {
						$state.go("projeto_dashboard", { id: response.data.id });
					}
				});
			}
			else {
				$window.alert("Por favor, preencha todos os campos obrigatórios!");
			}
		}
	};

})(angular);