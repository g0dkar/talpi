(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.filter('role', role);

	role.$inject = [];
	function role() {
		return function(input) {
			switch(input) {
				case 'PM':
					return 'Gerente de projeto';
				case 'MEMBRO':
					return 'Membro';
				case 'CLIENTE':
					return 'Cliente';
			}
		}
	};

})(angular);