(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.controller('LoginCtrl', LoginCtrl);

	LoginCtrl.$inject = ['$rootScope', '$state', '$cookieStore', 'authService'];
	function LoginCtrl($rootScope, $state, $cookieStore, authService) {

		var vm = this;

		vm.loading = false;
		vm.user = {};
		vm.auth = auth;

		function auth() {
			vm.loading = true;
			authService.login(vm.user).then(function(response) {
				if(angular.isArray(response.data)) {
					console.error('Usuário e/ou senha inválidos');
					//TODO treat invalid response
					vm.loading = false;
				} else {
					$cookieStore.put('globals', response.data);
					$rootScope.globals = response.data;
					vm.loading = false;
					$state.go('project_list');
				}
			}, function(err) {
				console.error('Erro na requisição');
				//TODO treat request error
			});
		}
	}

})(angular);