(function (angular) {
	
	angular.module("talpi", ["ngAnimate", 
			"ngSanitize", 
			"ui.router"
		]
	);
	
//	app.controller("MainController", ["$scope", "$state", function ($scope, $state) {
//		if (!loggedUser) {
//			$state.go("login");
//		}
//		else {
//			$scope.setLogin(false);
//		}
//	}]);
	
	/*app.controller("LogoutController", ["$state", function ($state) {
		loggedUser = null;
		checkLogin = false;
		$state.go("login");
	}]);*/
		
	/*app.controller("ProjetosListController", ["$scope", "$http", "$state", "$window", function ($scope, $http, $state, $window) {
		$scope.atualizar = function () {
			$scope.loading = true;
			$http.get(endpoint + "/api/projeto/lista").then(function (response) {
				$scope.loading = false;
				$scope.projetos = response.data;
			}, function () {
				$scope.loading = false;
				$window.alert("Erro ao pegar lista de projetos");
			});
		};
		
		$scope.atualizar();
	}]);
	
	app.controller("ProjetosNovoController", ["$scope", "$http", "$state", "$window", function ($scope, $http, $state, $window) {
		$scope.projeto = { congelado: false };
		$scope.novoProjeto = function (form, event) {
			event.preventDefault();
			
			if (form.$valid) {
				$http.post(endpoint + "/api/projeto/editar", $scope.projeto).then(function (response) {
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
	}]);
	
	app.controller("ProjetosNovoController", ["$scope", "$http", "$state", "$window", function ($scope, $http, $state, $window) {
		$scope.projeto = { congelado: false };
		$scope.novoProjeto = function (form, event) {
			event.preventDefault();
			
			if (form.$valid) {
				$http.post(endpoint + "/api/projeto/editar", $scope.projeto).then(function (response) {
					if (angular.isArray(response.data)) {
						$window.alert("Por favor, verifique os campos e tente novamente.");
					}
					else {
						$state.go("projetos_dashboard", { id: response.data.id });
					}
				});
			}
			else {
				$window.alert("Por favor, preencha todos os campos obrigatórios!");
			}
		}
	}]);
	
	app.controller("ProjetosDashboardController", ["$scope", "$http", "$state", "$window", "projeto", function ($scope, $http, $state, $window, projeto) {
		$scope.projeto = projeto;
	}]);*/
})(angular);