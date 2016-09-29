(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.filter('state', state);

	state.$inject = [];
	function state() {
		return function(input, state) {
			var items = [];
			input.forEach(function(requirement) {
				if(requirement.estado.estado === state)
					items.push(requirement);
			});
			return items;
		}
	};

})(angular);