(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.config(config);

	config.$inject = ["$httpProvider", "$locationProvider", "$compileProvider", "$urlRouterProvider", "$urlMatcherFactoryProvider", "$stateProvider"];
	function config($httpProvider, $locationProvider, $compileProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $stateProvider) {

		$httpProvider.defaults.withCredentials = true;
		// Setting HTML 5 mode (bretty urls)
		//$locationProvider.html5Mode(true);
		// Show debug stuff?
		$compileProvider.debugInfoEnabled(false);
		// A "404" will go to /quiz
		$urlRouterProvider.otherwise("/logout");
		// Trailling "/" are optional 
		$urlMatcherFactoryProvider.strictMode(false);
		
		// And now... routes.
		$stateProvider.state("main", {
			url: "",
			templateUrl: "app/talpi.html",
		})
		.state("login", {
			url: "/login",
			templateUrl: "app/login/login.html",
			controller: "LoginController",
			controllerAs: "vm"
		})
		.state("logout", {
			url: "/logout",
			template: "",
			controller: "LogoutController"
		})
		.state("projetos", {
			url: "/projetos",
			templateUrl: "app/project-list/project-list.html",
			controller: "ProjetosListController"
		})
		.state("projetos_novo", {
			url: "/projetos/novo",
			templateUrl: "app/project-new/project-new.html",
			controller: "ProjetosNovoController"
		})
		.state("projetos_dashboard", {
			url: "/projetos/{id:\\d+}",
			templateUrl: "pages/projetos_dashboard.html",
			controller: "ProjetosDashboardController",
			resolve: {
				projeto: ["$q", "$http", "$state", "$stateParams", function ($q, $http, $state, $stateParams) {
					return $q(function (resolve, reject) {
						$http.get(endpoint + "/api/projeto/" + $stateParams.id).then(function (response) {
							resolve(response.data);
						},
						function (data) {
							$state.go("projetos");
							reject(response.data);
						});
					});
				}]
			}
		})
	};	
		
})(angular);