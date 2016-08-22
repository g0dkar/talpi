(function (angular) {
	var app = angular.module("talpi", ["ngAnimate", "ngSanitize", "ui.router"]);
	var endpoint = "http://localhost";
	var loggedUser = null;
	var checkLogin = false;
	var lastCheck = 0;
	
//	app.controller("MainController", ["$scope", "$state", function ($scope, $state) {
//		if (!loggedUser) {
//			$state.go("login");
//		}
//		else {
//			$scope.setLogin(false);
//		}
//	}]);
	
	app.controller("LogoutController", ["$state", function ($state) {
		loggedUser = null;
		checkLogin = false;
		$state.go("login");
	}]);
	
	app.controller("LoginController", ["$scope", "$http", "$window", "$state", function ($scope, $http, $window, $state) {
		loggedUser = null;
		checkLogin = false;
		$scope.setLogin(true);
		$scope.loginData = {};
		$scope.submitting = false;
		$scope.login = function (form, evt) {
			evt.preventDefault();
			
			if (form.$valid) {
//				loggedUser = angular.copy($scope.loginData);
//				loggedUser.id = -1;
//				$state.go("main");
//				return;
				
				$scope.submitting = true;
				$http.post(endpoint + "/api/usuario/login", $scope.loginData).then(function (response) {
					$scope.submitting = false;
					
					if (angular.isArray(response.data)) {
						$window.alert("Usuário ou senha inválidos.");
					}
					else {
						checkLogin = true;
						lastCheck = Date.now();
						loggedUser = response.data;
						$scope.setLogin(false);
						
						if ($scope._toState && $scope._toState != "login") {
							$state.go($scope._toState.name, $scope._toStateParams);
						}
						else {
							$state.go("main");
						}
					}
				}, function () {
					$scope.submitting = false;
					$window.alert("Não foi possível fazer login. Tente novamente mais tarde.");
				});
			}
			else {
				$window.alert("Preencha os campos antes de continuar");
			}
		};
	}]);
	
	app.controller("ProjetosListController", ["$scope", "$http", "$state", "$window", function ($scope, $http, $state, $window) {
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
						$state.go("projeto_detalhe", { id: response.data.id });
					}
				});
			}
			else {
				$window.alert("Por favor, preencha todos os campos obrigatórios!");
			}
		}
	}]);
	
	app.run(["$rootScope", "$state", function ($rootScope, $state) {
		$rootScope.doLogin = false;
		$rootScope.setLogin = function (val) { $rootScope.doLogin = val; };
		$rootScope.$on("$stateChangeStart", function (event, toState, toStateParams) {
			console.log("$stateChangeStart ->", arguments);
			if (toState.name != "login" && !loggedUser) {
				console.log(event);
				event.preventDefault();
				$rootScope._toState = toState;
				$rootScope._toStateParams = toStateParams;
				$state.go("login");
			}
		});
	}]);
	
	app.factory("loginInterceptor", ["$q", function ($q) {
		return {
			request: function (config) {
				return $q(function (resolve, reject) {
					// Checa a cada 5min
					if (checkLogin && (!loggedUser || (loggedUser.id > 0 && Date.now() - lastCheck >= 300000))) {
						$http.get(endpoint + "/api/usuario/check").then(function (response) {
							if (response.data === loggedUser.id) {
								lastCheck = Date.now();
								resolve(config);
							}
							else {
								reject(config);
							}
						}, function () {
							reject(config);
						});
					}
					else {
						resolve(config);
					}
				});
			}
		};
	}]);
	
	app.config(["$httpProvider", "$locationProvider", "$compileProvider", "$urlRouterProvider", "$urlMatcherFactoryProvider", "$stateProvider", function ($httpProvider, $locationProvider, $compileProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $stateProvider) {
		// Configuring the $httpProvider to use our paramsSerializer, send forms like it should and adding an error interceptor
		$httpProvider.interceptors.push("loginInterceptor");
		$httpProvider.defaults.withCredentials = true;
		
		// Setting HTML 5 mode (bretty urls)
//		$locationProvider.html5Mode(true);
		
		// Show debug stuff?
		$compileProvider.debugInfoEnabled(false);
		
		// A "404" will go to /quiz
		$urlRouterProvider.otherwise("/logout");
		
		// Trailling "/" are optional 
		$urlMatcherFactoryProvider.strictMode(false);
		
		// And now... routes.
		$stateProvider.state("main", {
			url: "",
			templateUrl: "pages/main.html",
//			controller: "MainController"
		})
		.state("login", {
			url: "/login",
			templateUrl: "pages/login.html",
			controller: "LoginController"
		})
		.state("logout", {
			url: "/logout",
			template: "",
			controller: "LogoutController"
		})
		.state("projetos", {
			url: "/projetos",
			templateUrl: "pages/projetos.html",
			controller: "ProjetosListController"
		})
		.state("projetos_novo", {
			url: "/projetos/novo",
			templateUrl: "pages/projetos_novo.html",
			controller: "ProjetosNovoController"
		}) 
//		.state("admin.quiz", {
//			url: "/quiz",
//			"abstract": true,
//			template: "<ui-view/>"
//		})
//		.state("admin.quiz.dashboard", {
//			url: "",
//			templateUrl: context + "/api/ng/quiz-dashboard",
//			controller: "QuizDashboardController",
//			resolve: {
//				quizzes: ["$q", "$http", "toastr", function ($q, $http, toastr) {
//					return requireHttp("qdql", $q, $http, toastr, context + "/api/quiz/my-quizzes");
//				}]
//			}
//		})
	}]);
})(angular);