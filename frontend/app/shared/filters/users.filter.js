(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.filter('members', role);

	role.$inject = [];
	function role() {
		return function(input, searched) {
			var items = [];
			input.forEach(function(item) {
				
			})
		}
	};

})(angular);