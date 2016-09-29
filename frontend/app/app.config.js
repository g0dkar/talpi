(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.config(config);

	config.$inject = ['$httpProvider', '$stateProvider', '$urlRouterProvider'];
	function config($httpProvider, $stateProvider, $urlRouterProvider) {

		$httpProvider.defaults.withCredentials = true;

		$urlRouterProvider.otherwise('/projects');

		$stateProvider
			.state('login', {
				url: '/login',
				views: {
					'': {
						templateUrl: 'app/login/login.html'
					},
					'register@login': {
						templateUrl: 'app/register/register.html',
						controller: 'RegisterCtrl',
						controllerAs: 'vm'
					},
					'sign-in@login': {
						templateUrl: 'app/signin/signin.html',
						controller: 'SignInCtrl',
						controllerAs: 'vm'
					}
				}
			})
			.state('projects', {
				url: '/projects',
				templateUrl: 'app/projects/projects.html',
				controller: 'ProjectsCtrl',
				controllerAs: 'vm'
			})
			.state('project', {
				url: '/project/:id',
				params: { project: null },
				templateUrl: 'app/project/project.html',
				controller: 'ProjectCtrl',
				controllerAs: 'vm'
			})
			.state('project.requirement', {
				url: '/requirements',
				templateUrl: 'app/requirements/requirements.html',
				controller: 'RequirementCtrl',
				controllerAs: 'vm'
			})
			.state('project.config', {
				url: '/config',
				templateUrl: 'app/config/config.html',
				controller: 'ConfigCtrl',
				controllerAs: 'vm'
			})
			.state('project.members', {
				url: '/members',
				templateUrl: 'app/members/members.html',
				controller: 'MembersCtrl',
				controllerAs: 'vm'
			});

	};
		
})(angular);