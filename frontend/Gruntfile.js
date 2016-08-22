module.exports = function(grunt) {
	// 1. Time how long tasks take
	require("time-grunt")(grunt);

	// 2. load all grunt tasks matching the `grunt-*` pattern
	require("load-grunt-tasks")(grunt);

	// 3. All configuration goes here
	grunt.initConfig({
		path: {
			src: "dev",			// Dev files
			dest: "res",		// Dist files
		},
		concat: {
			options: {
				sourceMap: true
			},
			js: {
				src: [
					"<%= path.src %>/bower_components/angular/angular.js",
					"<%= path.src %>/bower_components/angular/angular-animate.js",
					"<%= path.src %>/bower_components/angular/angular-sanitize.js",
					"<%= path.src %>/bower_components/angular/angular-ui-router.js",
					"<%= path.src %>/bower_components/angular/loading-bar.js",
					"<%= path.src %>/bower_components/angular/moment-with-locales.js",
					"<%= path.src %>/talpi.js"
				],
				dest: "<%= path.src %>/js/talpi-all.js",
			},
			css: {
				src: [
					"<%= path.src %>/css/deps/bootstrap.css",
					"<%= path.src %>/css/talpi.css"
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
					"<%= path.dest %>/js/talpi.js": ["<%= path.src %>/js/talpi-all.js"]
				}
			}
		},
		
		cssnano: {
			dist: {
				files: {
					"<%= path.dest %>/css/talpi-all.css": "<%= path.src %>/css/talpi.css"
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
				files: "<%= path.src %>/js/**/*.js",
				tasks: ["concat:js", "uglify"]
			},
			css: {
				files: "<%= path.src %>/css/**/*.css",
				tasks: ["concat:css", "cssnano"],
			},
			img: {
				files: "<%= path.src %>/img/**/*.{png,jpg,gif}",
				tasks: ["imagemin"],
			}
		}
	});

	// 4. Where we tell Grunt what to do when we type "grunt" into the terminal.
	grunt.registerTask("default", ["concat:js", "uglify", "concat:css", "cssnano", "imagemin", "watch"]);
};