(function(angular) {

	'use strict';

	angular
		.module('talpi')
		.filter('icon', icon);

	icon.$inject = [];
	function icon() {
		return function(input) {
			console.log(input)
			switch(input) {
				case 'CRIADO':
					return 'play_arrow';
				case 'INICIADO':
					return 'done';
				case 'PRONTO_HOMOLOGACAO':
					return 'done_all';
				case 'CONCLUIDO':
					return 'verified_user';
				case 'NAO_HOMOLOGADO':
					return 'thumb_down';
				case 'PAUSADO':
					return 'replay';
			}
		}
	};

})(angular);