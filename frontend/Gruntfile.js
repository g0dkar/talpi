module.exports = function(grunt) {
	// 1. Time how long tasks take
	require("time-grunt")(grunt);

	// 2. load all grunt tasks matching the `grunt-*` pattern
	require("load-grunt-tasks")(grunt);

	// 3. All configuration goes here
	grunt.initConfig({
		path: {
			src: "app",			// App files
			dest: "dist",		// Dist files
		},
		
		concat: {
			options: {
				sourceMap: true
			},
			vendor_js: {
				src: [
					"bower_components/angular/angular.js",
					"bower_components/angular-animate/angular-animate.js",
					"bower_components/angular-sanitize/angular-sanitize.js",
					"bower_components/angular-ui-router/release/angular-ui-router.js",
					"bower_components/angular-loading-bar/build/loading-bar.js",
					"bower_components/moment/moment.js",
					"bower_components/moment/locale/br.js",
				],
				dest: "<%= path.dest %>/js/vendor.js",
			},
			vendor_css: {
				src: [
					"bower_components/**/*.css"
				],
				dest: "<%= path.dest %>/css/vendor.css"
			},
			js: {
				src: [
					"<%= path.src %>/app.js",
					"<%= path.src %>/app.config.js",
					"<%= path.src %>/app.run.js",
					"<%= path.src %>/**/*.js",
					"<%= path.src %>/login/*.js",
					"<%= path.src %>/project-list/*.js",
					"<%= path.src %>/project-detail/*.js",
					"<%= path.src %>/project-new/*.js",
					"<%= path.src %>/requirement-list/*.js",
					"<%= path.src %>/requirement-detail/*.js",
					"<%= path.src %>/requirement-new/*.js",
					"<%= path.src %>/task-list/*.js",
					"<%= path.src %>/task-detail/*.js",
					"<%= path.src %>/task-new/*.js",
				],
				dest: "<%= path.dest %>/js/talpi.js"
			},
			css: {
				src: [
					"<%= path.src %>/**/*.css"
				],
				dest: "<%= path.dest %>/css/talpi.css",
			}
		},
		
		uglify: {
			options: {
				sourceMap: true,
				sourceMapIncludeSources: true,
				sourceMapIn: "<%= path.dest %>/js/talpi.js.map",
				compress: { drop_console: false }
			},
			dev: {
				files: {
					"<%= path.dest %>/js/talpi.js": ["<%= path.dest %>/js/talpi.js"]
				}
			}
		},
		
		cssnano: {
			dist: {
				files: {
					"<%= path.dest %>/css/talpi.css": "<%= path.dest %>/css/talpi.css"
				}
			}
		},
		
		imagemin: {
			dynamic: {										// Another target
				files: [{
					expand: true,							// Enable dynamic expansion
					cwd: "<%= path.src %>/share/img/",		// src matches are relative to this path
					src: ["**/*.{png,jpg,gif}"],			// Actual patterns to match
					dest: "<%= path.dest %>/img/"			// Destination path prefix
				}]
			}
		},
		
		watch: {
			js: {
				files: "<%= path.src %>/**/*.js",
				tasks: ["concat:js", "uglify"]
			},
			css: {
				files: "<%= path.src %>/**/*.css",
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
					// keepalive: true,
					base: "./"
				}
			}
		}
	});

	// 4. Where we tell Grunt what to do when we type "grunt" into the terminal.
	grunt.registerTask("default", ["concat:vendor_js", "concat:vendor_css", "concat:js", "uglify", "concat:css", "cssnano", "imagemin", "connect", "watch"]);
};