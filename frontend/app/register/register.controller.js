(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('RegisterCtrl', RegisterCtrl);

	RegisterCtrl.$inject = ['$rootScope', '$state', '$cookieStore', 'authService', 'userService'];
	function RegisterCtrl($rootScope, $state, $cookieStore, authService, userService) {

		var vm = this;

		vm.loading = false;
		vm.user = {};
		vm.confirmPass = null;
		
		vm.register = register;
		vm.auth = auth;

		function register() {
			vm.loading = true;
			userService.post(vm.user).then(function(response) {
				if(angular.isArray(response.data)) {
					console.error(response.data[0].message);
				}
				vm.auth();
				vm.loading = false;
			}, function(err) {
				console.error('Erro na requisição');
				console.error(err)
				//TODO treat request error
				vm.loading = false;
			});
		}

		function auth() {
			authService.login(vm.user).then(function(response) {
				if(angular.isArray(response.data)) {
					console.error('Usuário e/ou senha inválidos');
					//TODO treat invalid response
				} else {
					$cookieStore.put('globals', response.data);
					$rootScope.globals = response.data;
					$state.go('projects');
				}
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
			});
		};
	}

})(angular);