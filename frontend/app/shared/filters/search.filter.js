(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.filter('search', search);

	search.$inject = [];
	function search() {
		return function(input, filter) {
			var items = [];
			if(!filter) return input;
			for(var index in input) {
				if(input[index].nome.toLowerCase().indexOf(filter.toLowerCase()) != -1 ||
					input[index].email.toLowerCase().indexOf(filter.toLowerCase()) != -1) {
					items.push(input[index]);
				}
			}
			return items;
		}
	};

})(angular);