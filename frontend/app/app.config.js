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
				templateUrl: 'app/login/login.html',
				controller: 'LoginCtrl',
				controllerAs: 'vm'
			})
			.state('project_list', {
				url: '/projects',
				templateUrl: 'app/project-list/project-list.html',
				controller: 'ProjectListCtrl',
				controllerAs: 'vm'
			})
			.state('project', {
				url: '/project/:id',
				params: { project: null },
				templateUrl: 'app/project/project.html',
				controller: 'ProjectCtrl',
				controllerAs: 'vm'
			})
			.state('project.frame', {
				url: '/frame',
				templateUrl: 'app/project-frame/project-frame.html',
				controller: 'ProjectFrameCtrl',
				controllerAs: 'vm'
			})
			.state('project.config', {
				url: '/config',
				templateUrl: 'app/project-config/project-config.html',
				controller: 'ProjectConfigCtrl',
				controllerAs: 'vm'
			})
			.state('project.members', {
				url: '/members',
				templateUrl: 'app/project-members/project-members.html',
				controller: 'ProjectMembersCtrl',
				controllerAs: 'vm'
			});

	};
		
})(angular);