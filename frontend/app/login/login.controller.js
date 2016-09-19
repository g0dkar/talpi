(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller("LoginController", LoginController)
		.controller("LogoutController", LogoutController);

	LoginController.$inject = ["$rootScope","$scope", "$http", "$window", "$state"];
	function LoginController($rootScope, $scope, $http, $window, $state) {

		var vm = this;
		$rootScope.setLogin(true);

		/**	Attributes*/
		vm.loginData = {};
		vm.submitting = false;

		/** Functions*/
		vm.login = login;

		function login (form, evt) {
			evt.preventDefault();
			
			if (form.$valid) {				
				vm.submitting = true;
				$http.post($rootScope.endpoint + "/api/usuario/login", vm.loginData).then(function (response) {
					vm.submitting = false;
					
					if (angular.isArray(response.data)) {
						$window.alert("Usuário ou senha inválidos.");
					}
					else {
						$rootScope.checkLogin = true;
						$rootScope.lastCheck = Date.now();
						$rootScope.loggedUser = response.data;
						$rootScope.setLogin(false);
						
						if ($scope._toState && $scope._toState != "login") {
							$state.go($scope._toState.name, $scope._toStateParams);
						}
						else {
							$state.go("main");
						}
					}
				}, function () {
					vm.submitting = false;
					$window.alert("Não foi possível fazer login. Tente novamente mais tarde.");
				});
			}
			else {
				$window.alert("Preencha os campos antes de continuar");
			}
		};
	};

	LogoutController.$inject = ["$rootScope", "$scope", "$state"];
	function LogoutController($rootScope, $scope, $state) {
		$rootScope.loggedUser = null;
		$rootScope.checkLogin = false;
		$state.go("login");
	};

})(angular);