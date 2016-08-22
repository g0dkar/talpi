module.exports = function(grunt) {
	// 1. Time how long tasks take
	require("time-grunt")(grunt);

	// 2. load all grunt tasks matching the `grunt-*` pattern
	require("load-grunt-tasks")(grunt);

	// 3. All configuration goes here
	grunt.initConfig({
		path: {
			src: "dev",			// Dev files
			dest: "dist",		// Dist files
		},
		
		concat: {
			options: {
				sourceMap: true
			},
			js: {
				src: [
					"<%= path.src %>/bower_components/angular/angular.js",
					"<%= path.src %>/bower_components/angular-animate/angular-animate.js",
					"<%= path.src %>/bower_components/angular-sanitize/angular-sanitize.js",
					"<%= path.src %>/bower_components/angular-ui-router/release/angular-ui-router.js",
					"<%= path.src %>/bower_components/angular-loading-bar/build/loading-bar.js",
					"<%= path.src %>/bower_components/moment/moment.js",
					"<%= path.src %>/bower_components/moment/locale/br.js",
					"<%= path.src %>/talpi.js"
				],
				dest: "<%= path.src %>/js/talpi-all.js",
			},
			css: {
				src: [
					"<%= path.src %>/bower_components/bootstrap/dist/css/bootstrap.css",
					"<%= path.src %>/talpi.css"
				],
				dest: "<%= path.src %>/css/talpi-all.css",
			}
		},
		
		uglify: {
			options: {
				sourceMap: true,
				sourceMapIncludeSources: true,
				sourceMapIn: "<%= path.src %>/js/talpi-all.js.map",
				compress: { drop_console: false }
			},
			dev: {
				files: {
					"<%= path.dest %>/talpi.js": ["<%= path.src %>/js/talpi-all.js"]
				}
			}
		},
		
		cssnano: {
			dist: {
				files: {
					"<%= path.dest %>/talpi.css": "<%= path.src %>/css/talpi-all.css"
				}
			}
		},
		
		imagemin: {
			dynamic: {										// Another target
				files: [{
					expand: true,							// Enable dynamic expansion
					cwd: "<%= path.src %>/img/",			// src matches are relative to this path
					src: ["**/*.{png,jpg,gif}"],			// Actual patterns to match
					dest: "<%= path.dest %>/img/"			// Destination path prefix
				}]
			}
		},
		
		watch: {
			js: {
				files: "<%= path.src %>/talpi.js",
				tasks: ["concat:js", "uglify"]
			},
			css: {
				files: "<%= path.src %>/talpi.css",
				tasks: ["concat:css", "cssnano"]
			},
			img: {
				files: "<%= path.src %>/img/**/*.{png,jpg,gif}",
				tasks: ["imagemin"]
			}
		},
		
		connect: {
			server: {
				options: {
					port: 8000,
//					keepalive: true,
					base: "./"
				}
			}
		}
	});

	// 4. Where we tell Grunt what to do when we type "grunt" into the terminal.
	grunt.registerTask("default", ["concat:js", "uglify", "concat:css", "cssnano", "imagemin", "connect", "watch"]);
};