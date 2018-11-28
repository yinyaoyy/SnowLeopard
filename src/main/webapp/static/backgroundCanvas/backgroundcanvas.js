/*$(document).ready(function() {
	var path = window.location.pathname;
	var pathName = path.split('/')[path.split('/').length - 1].toLowerCase()
	if(pathName === 'login.html' || pathName === 'lockscreen.html') {
		var cookie = getCookie('backgroundType');
		if(cookie != null && cookie != undefined && cookie != '') {
			var cookieList = cookie.split("|");
			console.warn(typeof(decodeURI(cookieList[3])))
			$.loadBackground({
				iscavans: cookieList[0],
				israndom: cookieList[1],
				styleOrPath: cookieList[2],
				canvasText: decodeURI(cookieList[3])
			})
		} else {
			$.loadBackground({
				iscavans: 'canvas',
				israndom: true,
				styleOrPath: 'canvasStar'
			})
		}
	} else {
		console.warn('not login or lockscreen')
	}
});*/

$.extend({
	loadBackground: function(ownOpt) {
		var defaultOpt = {
			iscavans: 'canvas',
			israndom: true,
			styleOrPath: 'canvasStar',
			canvasText: 'KEYTEC'
		}
		var Opts = $.extend(defaultOpt, ownOpt);
		top.$.setBgCookie('backgroundType', Opts.iscavans, Opts.israndom, Opts.styleOrPath)
		var cookieArr = top.$.getCookie('backgroundType').split('|');
		if(cookieArr[0] == 'pic') {
			$('#backgroundcanvas').hide();
			$('.page-body').css({
				'background': 'url(' + Opts.styleOrPath + ') no-repeat',
				'backgroundSize': '100% 100%'
			});
		} else if(cookieArr[0] == 'canvas') {
			var canvas = $('canvas')[0];
			var ctx = canvas.getContext('2d');
			ctx.clearRect(0, 0, window.innerWidth, window.innerHeight);
			if(cookieArr[1] == 'true') {
				randomCanvas(""+Opts.canvasText)
			} else {
				eval(Opts.styleOrPath + '("'+Opts.canvasText+'")')
			}
		}
	}
})

function randomCanvas(canvasText) {
	//判断是否为锁屏界面
	var path = window.location.pathname;
	var urlAdr = path.split('/')[path.split('/').length - 1]
	var styleType;
	if(urlAdr == 'lockscreen.html') {
		styleType = ['canvasStar', 'canvasDotted', 'canvasRedLine', 'canvasSquare', 'canvasHexagon']
	} else {
		styleType = ['canvasStar', 'canvasDotted', 'canvasRedLine', 'canvasSquare', 'canvasHexagon', 'canvasFont']
	}
	//判断Cookie
	var cookieArr = getCookie('backgroundType').split('|');
	var type = cookieArr[2]
	if(type != '' && type != 'undefined' && type != null) {
		for(var i = 0; i < styleType.length; i++) {
			if(styleType[i] == type) {
				var newType = styleType[Math.floor(styleType.length * Math.random())]
				setBgCookie('backgroundType', 'canvas', true, newType)
				if(newType == 'canvasFont') {
					eval(newType + '("' + canvasText + '")')
				} else {
					eval(newType + '()')
				}

			}
		}
	}
}

function setBgCookie(bgType, iscavans, israndom, style, fonttext) {
	var d = new Date();
	document.cookie = bgType + "=" + iscavans + "|" + israndom + "|" + style + "|" + fonttext+";path=/;";
}

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i = 0; i < ca.length; i++) {
		var c = ca[i].trim();
		if(c.indexOf(name) == 0) return c.substring(name.length, c.length);
	}
	return "";
}

function canvasStar() {
	var WIDTH;
	var HEIGHT;
	var canvas;
	var con;
	var g;
	var pxs = new Array;
	var rint = 60;
	WIDTH = "100%";
	HEIGHT = "100%";
	$("#container").width(WIDTH).height(HEIGHT);
	WIDTH = window.innerWidth;
	HEIGHT = window.innerHeight;
	canvas = document.getElementById("backgroundcanvas");
	canvas.style.background = "#000000";
	$(canvas).attr("width", WIDTH).attr("height", HEIGHT);
	con = canvas.getContext("2d");
	for(var e = 0; e < 600; e++) {
		pxs[e] = new Circle;
		pxs[e].reset()
	}
	setInterval(draw, rint)
	$(".services .header2 .service-header").hover(function() {
		var e = $(this);
		e.find("h3").hide();
		$(this).parent().find(".header-bg").stop(true, true).animate({
			width: "100%"
		}, "fast", function() {
			e.find("h3").addClass("active").fadeIn("fast")
		})
	}, function() {
		var e = $(this);
		e.find("h3").hide();
		e.parent().find(".header-bg").stop(true, true).animate({
			width: 70
		}, "fast", function() {
			e.find("h3").removeClass("active").fadeIn("fast")
		})
	})

	function draw() {
		con.clearRect(0, 0, WIDTH, HEIGHT);
		for(var e = 0; e < pxs.length; e++) {
			pxs[e].fade();
			pxs[e].move();
			pxs[e].draw()
		}
	}

	function Circle() {
		WIDTH = window.innerWidth;
		HEIGHT = window.innerHeight;
		this.s = {
			ttl: 8e3,
			xmax: 5,
			ymax: 2,
			rmax: 10,
			rt: 1,
			xdef: 960,
			ydef: 540,
			xdrift: 4,
			ydrift: 4,
			random: true,
			blink: true
		};
		this.reset = function() {
			this.x = this.s.random ? WIDTH * Math.random() : this.s.xdef;
			this.y = this.s.random ? HEIGHT * Math.random() : this.s.ydef;
			this.r = (this.s.rmax - 1) * Math.random() + 1;
			this.dx = Math.random() * this.s.xmax * (Math.random() < .5 ? -1 : 1);
			this.dy = Math.random() * this.s.ymax * (Math.random() < .5 ? -1 : 1);
			this.hl = this.s.ttl / rint * (this.r / this.s.rmax);
			this.rt = Math.random() * this.hl;
			this.s.rt = Math.random() + 1;
			this.stop = Math.random() * .2 + .4;
			this.s.xdrift *= Math.random() * (Math.random() < .5 ? -1 : 1);
			this.s.ydrift *= Math.random() * (Math.random() < .5 ? -1 : 1)
		};
		this.fade = function() {
			this.rt += this.s.rt
		};
		this.draw = function() {
			if(this.s.blink && (this.rt <= 0 || this.rt >= this.hl)) this.s.rt = this.s.rt * -1;
			else if(this.rt >= this.hl) this.reset();
			var e = 1 - this.rt / this.hl;
			con.beginPath();
			con.arc(this.x, this.y, this.r, 0, Math.PI * 2, true);
			con.closePath();
			var t = this.r * e;
			g = con.createRadialGradient(this.x, this.y, 0, this.x, this.y, t <= 0 ? 1 : t);
			g.addColorStop(0, "rgba(255,255,255," + e + ")");
			g.addColorStop(this.stop, "rgba(77,101,181," + e * .6 + ")");
			g.addColorStop(1, "rgba(77,101,181,0)");
			con.fillStyle = g;
			con.fill()
		};
		this.move = function() {
			WIDTH = window.innerWidth;
			HEIGHT = window.innerHeight;
			this.x += this.rt / this.hl * this.dx;
			this.y += this.rt / this.hl * this.dy;
			if(this.x > WIDTH || this.x < 0) this.dx *= -1;
			if(this.y > HEIGHT || this.y < 0) this.dy *= -1
		};
		this.getX = function() {
			return this.x
		};
		this.getY = function() {
			return this.y
		}
	}
}

function canvasDotted() {
	var canvas = document.querySelector('#backgroundcanvas'),
		ctx = canvas.getContext('2d')
	canvas.style.background = "#000000"
	canvas.width = window.innerWidth;
	canvas.height = window.innerHeight;
	ctx.lineWidth = .3;
	ctx.strokeStyle = (new Color(150)).style;

	var mousePosition = {
		x: 30 * canvas.width / 100,
		y: 30 * canvas.height / 100
	};

	var dots = {
		nb: 500,
		distance: 50,
		d_radius: 100,
		array: []
	};

	function colorValue(min) {
		return Math.floor(Math.random() * 255 + min);
	}

	function createColorStyle(r, g, b) {
		return 'rgba(' + r + ',' + g + ',' + b + ', 0.8)';
	}

	function mixComponents(comp1, weight1, comp2, weight2) {
		return(comp1 * weight1 + comp2 * weight2) / (weight1 + weight2);
	}

	function averageColorStyles(dot1, dot2) {
		var color1 = dot1.color,
			color2 = dot2.color;

		var r = mixComponents(color1.r, dot1.radius, color2.r, dot2.radius),
			g = mixComponents(color1.g, dot1.radius, color2.g, dot2.radius),
			b = mixComponents(color1.b, dot1.radius, color2.b, dot2.radius);
		return createColorStyle(Math.floor(r), Math.floor(g), Math.floor(b));
	}

	function Color(min) {
		min = min || 0;
		this.r = colorValue(min);
		this.g = colorValue(min);
		this.b = colorValue(min);
		this.style = createColorStyle(this.r, this.g, this.b);
	}

	function Dot() {
		this.x = Math.random() * canvas.width;
		this.y = Math.random() * canvas.height;

		this.vx = -.5 + Math.random();
		this.vy = -.5 + Math.random();

		this.radius = Math.random() * 2;

		this.color = new Color();
	}

	Dot.prototype = {
		draw: function() {
			ctx.beginPath();
			ctx.fillStyle = this.color.style;
			ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2, false);
			ctx.fill();
		}
	};

	function createDots() {
		for(i = 0; i < dots.nb; i++) {
			dots.array.push(new Dot());
		}
	}

	function moveDots() {
		for(i = 0; i < dots.nb; i++) {

			var dot = dots.array[i];

			if(dot.y < 0 || dot.y > canvas.height) {
				dot.vx = dot.vx;
				dot.vy = -dot.vy;
			} else if(dot.x < 0 || dot.x > canvas.width) {
				dot.vx = -dot.vx;
				dot.vy = dot.vy;
			}
			dot.x += dot.vx;
			dot.y += dot.vy;
		}
	}

	function connectDots() {
		for(i = 0; i < dots.nb; i++) {
			for(j = 0; j < dots.nb; j++) {
				i_dot = dots.array[i];
				j_dot = dots.array[j];

				if((i_dot.x - j_dot.x) < dots.distance && (i_dot.y - j_dot.y) < dots.distance && (i_dot.x - j_dot.x) > -dots.distance && (i_dot.y - j_dot.y) > -dots.distance) {
					if((i_dot.x - mousePosition.x) < dots.d_radius && (i_dot.y - mousePosition.y) < dots.d_radius && (i_dot.x - mousePosition.x) > -dots.d_radius && (i_dot.y - mousePosition.y) > -dots.d_radius) {
						ctx.beginPath();
						ctx.strokeStyle = averageColorStyles(i_dot, j_dot);
						ctx.moveTo(i_dot.x, i_dot.y);
						ctx.lineTo(j_dot.x, j_dot.y);
						ctx.stroke();
						ctx.closePath();
					}
				}
			}
		}
	}

	function drawDots() {
		for(i = 0; i < dots.nb; i++) {
			var dot = dots.array[i];
			dot.draw();
		}
	}

	function animateDots() {
		ctx.clearRect(0, 0, canvas.width, canvas.height);
		moveDots();
		connectDots();
		drawDots();

		requestAnimationFrame(animateDots);
	}

	$('canvas').on('mousemove', function(e) {
		mousePosition.x = e.pageX;
		mousePosition.y = e.pageY;
	});

	$('canvas').on('mouseleave', function(e) {
		mousePosition.x = canvas.width / 2;
		mousePosition.y = canvas.height / 2;
	});

	createDots();
	requestAnimationFrame(animateDots);
}

function canvasRedLine() {
	b = document.getElementById('backgroundcanvas'),
		b.style.background = "#000000"
	c = b.getContext('2d');
	b.width = window.innerWidth;
	b.height = window.innerHeight;

	var goal,
		followers = [],
		count = 100,
		md = 0,
		mx = b.width / 2,
		my = b.height / 2,
		tick = 0,
		hue = 0;

	function rand(min, max) {
		return Math.random() * (max - min) + min;
	}

	function Goal() {
		this.x = b.width / 2;
		this.y = b.height + 10;
		this.ax = 0;
		this.ay = 0;
		this.vx = 0;
		this.vy = 0;
		this.r = 1;
	}

	Goal.prototype.step = function() {
		this.ax += rand(-0.4, 0.4);
		this.ay += rand(-0.4, 0.4);
		this.vx += this.ax;
		this.vy += this.ay;
		this.ax *= Math.abs(this.ax) > 3 ? 0.75 : 1;
		this.ay *= Math.abs(this.ay) > 3 ? 0.75 : 1;
		this.vx *= Math.abs(this.vx) > 5 ? 0.75 : 1;
		this.vy *= Math.abs(this.vy) > 5 ? 0.75 : 1;
		this.x += this.vx;
		this.y += this.vy;
		if(this.x + this.r >= b.width || this.x <= this.r) {
			this.vx = 0;
			this.ax = 0;
		}
		if(this.y + this.r >= b.height || this.y <= this.r) {
			this.vy = 0;
			this.ay = 0;
		}
		if(this.x + this.r >= b.width) {
			this.x = b.width - this.r;
		}
		if(this.x <= this.r) {
			this.x = this.r;
		}
		if(this.y + this.r >= b.height) {
			this.y = b.height - this.r;
		}
		if(this.y <= this.r) {
			this.y = this.r;
		}

		if(md) {
			this.vx += (mx - this.x) * 0.02;
			this.vy += (my - this.y) * 0.02;
		}
	};

	function Follower(leader, r, d) {
		this.x = b.width / 2;
		this.y = b.height / 2;
		this.vx = 0;
		this.vy = 0;
		this.r = r;
		this.leader = leader;
		this.damp = d;
	}

	Follower.prototype.step = function() {
		this.vx = (this.leader.x - this.x) * this.damp;
		this.vy = (this.leader.y - this.y) * this.damp;
		this.x += this.vx;
		this.y += this.vy;
	};

	function loop() {
		requestAnimationFrame(loop);

		// step
		goal.step();
		goal.step();
		goal.step();

		c.globalCompositeOperation = 'lighter';
		c.beginPath();
		c.moveTo(followers[0].x, followers[0].y);
		followers.forEach(function(follower, i) {
			follower.step();
			if(i > 0) {
				c.lineTo(follower.x, follower.y);
			}

		});
		c.lineWidth = 2;
		c.strokeStyle = 'hsla(0, 0%, 0%, 0.05)';
		c.strokeStyle = 'hsla(' + (hue + rand(-10, 10)) + ', ' + rand(50, 100) + '%, ' + rand(30, 60) + '%, ' + rand(0.01, 0.1) + ')';
		c.stroke();
		tick++;
	}

	function reset() {
		c.clearRect(0, 0, b.width, b.height);
		goal.x = b.width / 2;
		goal.y = b.height + 10;
		followers.forEach(function(follower, i) {
			follower.x = b.width / 2
			follower.y = b.height + 10;
		});
	}

	goal = new Goal();

	for(var i = 0; i < count; i++) {
		var leader = (i == 0) ? goal : followers[i - 1],
			r = 1 + ((count - i) / count) * 10,
			d = (i == 0) ? 0.05 : 0.4;
		followers.push(new Follower(leader, r, d));
	}

	window.addEventListener('mousedown', function() {
		hue = rand(0, 360);
		//			reset();
		md = 1;
	});

	window.addEventListener('mouseup', function() {
		md = 0;
	});

	window.addEventListener('mousemove', function(e) {
		mx = e.clientX;
		my = e.clientY;
	});

	loop();
	setInterval(function() {
			hue = rand(0, 360)
			reset()
		}, 30000)
		//	reset();

}

function canvasSquare() {

	var canvas,
		ctx,
		width,
		height,
		size,
		lines,
		tick;

	function line() {
		this.path = [];
		this.speed = rand(10, 20);
		this.count = randInt(10, 30);
		this.x = width / 2, +1;
		this.y = height / 2 + 1;
		this.target = {
			x: width / 2,
			y: height / 2
		};
		this.dist = 0;
		this.angle = 0;
		this.hue = tick / 5;
		this.life = 1;
		this.updateAngle();
		this.updateDist();
	}

	line.prototype.step = function(i) {
		this.x += Math.cos(this.angle) * this.speed;
		this.y += Math.sin(this.angle) * this.speed;

		this.updateDist();

		if(this.dist < this.speed) {
			this.x = this.target.x;
			this.y = this.target.y;
			this.changeTarget();
		}

		this.path.push({
			x: this.x,
			y: this.y
		});
		if(this.path.length > this.count) {
			this.path.shift();
		}

		this.life -= 0.001;

		if(this.life <= 0) {
			this.path = null;
			lines.splice(i, 1);
		}
	};

	line.prototype.updateDist = function() {
		var dx = this.target.x - this.x,
			dy = this.target.y - this.y;
		this.dist = Math.sqrt(dx * dx + dy * dy);
	}

	line.prototype.updateAngle = function() {
		var dx = this.target.x - this.x,
			dy = this.target.y - this.y;
		this.angle = Math.atan2(dy, dx);
	}

	line.prototype.changeTarget = function() {
		var randStart = randInt(0, 3);
		switch(randStart) {
			case 0: // up
				this.target.y = this.y - size;
				break;
			case 1: // right
				this.target.x = this.x + size;
				break;
			case 2: // down
				this.target.y = this.y + size;
				break;
			case 3: // left
				this.target.x = this.x - size;
		}
		this.updateAngle();
	};

	line.prototype.draw = function(i) {
		ctx.beginPath();
		var rando = rand(0, 10);
		for(var j = 0, length = this.path.length; j < length; j++) {
			ctx[(j === 0) ? 'moveTo' : 'lineTo'](this.path[j].x + rand(-rando, rando), this.path[j].y + rand(-rando, rando));
		}
		ctx.strokeStyle = 'hsla(' + rand(this.hue, this.hue + 30) + ', 80%, 55%, ' + (this.life / 3) + ')';
		ctx.lineWidth = rand(0.1, 2);
		ctx.stroke();
	};

	function rand(min, max) {
		return Math.random() * (max - min) + min;
	}

	function randInt(min, max) {
		return Math.floor(min + Math.random() * (max - min + 1));
	};

	function init() {
		canvas = document.getElementById('backgroundcanvas');
		canvas.style.background = "#000000";
		ctx = canvas.getContext('2d');
		size = 30;
		lines = [];
		reset();
		loop();
	}

	function reset() {
		width = Math.ceil(window.innerWidth / 2) * 2;
		height = Math.ceil(window.innerHeight / 2) * 2;
		tick = 0;

		lines.length = 0;
		canvas.width = width;
		canvas.height = height;
	}

	function create() {
		if(tick % 10 === 0) {
			lines.push(new line());
		}
	}

	function step() {
		var i = lines.length;
		while(i--) {
			lines[i].step(i);
		}
	}

	function clear() {
		ctx.globalCompositeOperation = 'destination-out';
		ctx.fillStyle = 'hsla(0, 0%, 0%, 0.1';
		ctx.fillRect(0, 0, width, height);
		ctx.globalCompositeOperation = 'lighter';
	}

	function draw() {
		ctx.save();
		ctx.translate(width / 2, height / 2);
		ctx.rotate(tick * 0.001);
		var scale = 0.8 + Math.cos(tick * 0.02) * 0.2;
		ctx.scale(scale, scale);
		ctx.translate(-width / 2, -height / 2);
		var i = lines.length;
		while(i--) {
			lines[i].draw(i);
		}
		ctx.restore();
	}

	function loop() {
		requestAnimationFrame(loop);
		create();
		step();
		clear();
		draw();
		tick++;
	}

	function onresize() {
		reset();
	}

	window.addEventListener('resize', onresize);

	init();

}

function canvasHexagon() {
	var c = document.getElementById('backgroundcanvas')
	var w = c.width = window.innerWidth,
		h = c.height = window.innerHeight,
		ctx = c.getContext('2d'),

		opts = {

			len: 40,
			count: 100,
			baseTime: 10,
			addedTime: 10,
			dieChance: .05,
			spawnChance: 1,
			sparkChance: .1,
			sparkDist: 10,
			sparkSize: 2,

			color: 'hsl(hue,100%,light%)',
			baseLight: 50,
			addedLight: 10, // [50-10,50+10]
			shadowToTimePropMult: 6,
			baseLightInputMultiplier: .01,
			addedLightInputMultiplier: .02,

			cx: w / 2,
			cy: h / 2,
			repaintAlpha: .04,
			hueChange: .1
		},

		tick = 0,
		lines = [],
		dieX = w / 2 / opts.len,
		dieY = h / 2 / opts.len,

		baseRad = Math.PI * 2 / 6;

	ctx.fillStyle = 'black';
	ctx.fillRect(0, 0, w, h);

	function loop() {

		window.requestAnimationFrame(loop);

		++tick;

		ctx.globalCompositeOperation = 'source-over';
		ctx.shadowBlur = 0;
		ctx.fillStyle = 'rgba(0,0,0,alp)'.replace('alp', opts.repaintAlpha);
		ctx.fillRect(0, 0, w, h);
		ctx.globalCompositeOperation = 'lighter';

		if(lines.length < opts.count && Math.random() < opts.spawnChance)
			lines.push(new Line);

		lines.map(function(line) {
			line.step();
		});
	}

	function Line() {

		this.reset();
	}
	Line.prototype.reset = function() {

		this.x = 0;
		this.y = 0;
		this.addedX = 0;
		this.addedY = 0;

		this.rad = 0;

		this.lightInputMultiplier = opts.baseLightInputMultiplier + opts.addedLightInputMultiplier * Math.random();

		this.color = opts.color.replace('hue', tick * opts.hueChange);
		this.cumulativeTime = 0;

		this.beginPhase();
	}
	Line.prototype.beginPhase = function() {

		this.x += this.addedX;
		this.y += this.addedY;

		this.time = 0;
		this.targetTime = (opts.baseTime + opts.addedTime * Math.random()) | 0;

		this.rad += baseRad * (Math.random() < .5 ? 1 : -1);
		this.addedX = Math.cos(this.rad);
		this.addedY = Math.sin(this.rad);

		if(Math.random() < opts.dieChance || this.x > dieX || this.x < -dieX || this.y > dieY || this.y < -dieY)
			this.reset();
	}
	Line.prototype.step = function() {

		++this.time;
		++this.cumulativeTime;

		if(this.time >= this.targetTime)
			this.beginPhase();

		var prop = this.time / this.targetTime,
			wave = Math.sin(prop * Math.PI / 2),
			x = this.addedX * wave,
			y = this.addedY * wave;

		ctx.shadowBlur = prop * opts.shadowToTimePropMult;
		ctx.fillStyle = ctx.shadowColor = this.color.replace('light', opts.baseLight + opts.addedLight * Math.sin(this.cumulativeTime * this.lightInputMultiplier));
		ctx.fillRect(opts.cx + (this.x + x) * opts.len, opts.cy + (this.y + y) * opts.len, 2, 2);

		if(Math.random() < opts.sparkChance)
			ctx.fillRect(opts.cx + (this.x + x) * opts.len + Math.random() * opts.sparkDist * (Math.random() < .5 ? 1 : -1) - opts.sparkSize / 2, opts.cy + (this.y + y) * opts.len + Math.random() * opts.sparkDist * (Math.random() < .5 ? 1 : -1) - opts.sparkSize / 2, opts.sparkSize, opts.sparkSize)
	}
	loop();

	window.addEventListener('resize', function() {

		w = c.width = window.innerWidth;
		h = c.height = window.innerHeight;
		ctx.fillStyle = 'black';
		ctx.fillRect(0, 0, w, h);

		opts.cx = w / 2;
		opts.cy = h / 2;

		dieX = w / 2 / opts.len;
		dieY = h / 2 / opts.len;
	});

}

function canvasFont(canvasText) {
	(function() {
		function n(n) {
			function t(t, r, e, u, i, o) {
				for(; i >= 0 && o > i; i += n) {
					var a = u ? u[i] : i;
					e = r(e, t[a], a, t)
				}
				return e
			}
			return function(r, e, u, i) {
				e = b(e, i, 4);
				var o = !k(r) && m.keys(r),
					a = (o || r).length,
					c = n > 0 ? 0 : a - 1;
				return arguments.length < 3 && (u = r[o ? o[c] : c], c += n), t(r, e, u, o, c, a)
			}
		}

		function t(n) {
			return function(t, r, e) {
				r = x(r, e);
				for(var u = O(t), i = n > 0 ? 0 : u - 1; i >= 0 && u > i; i += n)
					if(r(t[i], i, t)) return i;
				return -1
			}
		}

		function r(n, t, r) {
			return function(e, u, i) {
				var o = 0,
					a = O(e);
				if("number" == typeof i) n > 0 ? o = i >= 0 ? i : Math.max(i + a, o) : a = i >= 0 ? Math.min(i + 1, a) : i + a + 1;
				else if(r && i && a) return i = r(e, u), e[i] === u ? i : -1;
				if(u !== u) return i = t(l.call(e, o, a), m.isNaN), i >= 0 ? i + o : -1;
				for(i = n > 0 ? o : a - 1; i >= 0 && a > i; i += n)
					if(e[i] === u) return i;
				return -1
			}
		}

		function e(n, t) {
			var r = I.length,
				e = n.constructor,
				u = m.isFunction(e) && e.prototype || a,
				i = "constructor";
			for(m.has(n, i) && !m.contains(t, i) && t.push(i); r--;) i = I[r], i in n && n[i] !== u[i] && !m.contains(t, i) && t.push(i)
		}
		var u = this,
			i = u._,
			o = Array.prototype,
			a = Object.prototype,
			c = Function.prototype,
			f = o.push,
			l = o.slice,
			s = a.toString,
			p = a.hasOwnProperty,
			h = Array.isArray,
			v = Object.keys,
			g = c.bind,
			y = Object.create,
			d = function() {},
			m = function(n) {
				return n instanceof m ? n : this instanceof m ? void(this._wrapped = n) : new m(n)
			};
		"undefined" != typeof exports ? ("undefined" != typeof module && module.exports && (exports = module.exports = m), exports._ = m) : u._ = m, m.VERSION = "1.8.3";
		var b = function(n, t, r) {
				if(t === void 0) return n;
				switch(null == r ? 3 : r) {
					case 1:
						return function(r) {
							return n.call(t, r)
						};
					case 2:
						return function(r, e) {
							return n.call(t, r, e)
						};
					case 3:
						return function(r, e, u) {
							return n.call(t, r, e, u)
						};
					case 4:
						return function(r, e, u, i) {
							return n.call(t, r, e, u, i)
						}
				}
				return function() {
					return n.apply(t, arguments)
				}
			},
			x = function(n, t, r) {
				return null == n ? m.identity : m.isFunction(n) ? b(n, t, r) : m.isObject(n) ? m.matcher(n) : m.property(n)
			};
		m.iteratee = function(n, t) {
			return x(n, t, 1 / 0)
		};
		var _ = function(n, t) {
				return function(r) {
					var e = arguments.length;
					if(2 > e || null == r) return r;
					for(var u = 1; e > u; u++)
						for(var i = arguments[u], o = n(i), a = o.length, c = 0; a > c; c++) {
							var f = o[c];
							t && r[f] !== void 0 || (r[f] = i[f])
						}
					return r
				}
			},
			j = function(n) {
				if(!m.isObject(n)) return {};
				if(y) return y(n);
				d.prototype = n;
				var t = new d;
				return d.prototype = null, t
			},
			w = function(n) {
				return function(t) {
					return null == t ? void 0 : t[n]
				}
			},
			A = Math.pow(2, 53) - 1,
			O = w("length"),
			k = function(n) {
				var t = O(n);
				return "number" == typeof t && t >= 0 && A >= t
			};
		m.each = m.forEach = function(n, t, r) {
			t = b(t, r);
			var e, u;
			if(k(n))
				for(e = 0, u = n.length; u > e; e++) t(n[e], e, n);
			else {
				var i = m.keys(n);
				for(e = 0, u = i.length; u > e; e++) t(n[i[e]], i[e], n)
			}
			return n
		}, m.map = m.collect = function(n, t, r) {
			t = x(t, r);
			for(var e = !k(n) && m.keys(n), u = (e || n).length, i = Array(u), o = 0; u > o; o++) {
				var a = e ? e[o] : o;
				i[o] = t(n[a], a, n)
			}
			return i
		}, m.reduce = m.foldl = m.inject = n(1), m.reduceRight = m.foldr = n(-1), m.find = m.detect = function(n, t, r) {
			var e;
			return e = k(n) ? m.findIndex(n, t, r) : m.findKey(n, t, r), e !== void 0 && e !== -1 ? n[e] : void 0
		}, m.filter = m.select = function(n, t, r) {
			var e = [];
			return t = x(t, r), m.each(n, function(n, r, u) {
				t(n, r, u) && e.push(n)
			}), e
		}, m.reject = function(n, t, r) {
			return m.filter(n, m.negate(x(t)), r)
		}, m.every = m.all = function(n, t, r) {
			t = x(t, r);
			for(var e = !k(n) && m.keys(n), u = (e || n).length, i = 0; u > i; i++) {
				var o = e ? e[i] : i;
				if(!t(n[o], o, n)) return !1
			}
			return !0
		}, m.some = m.any = function(n, t, r) {
			t = x(t, r);
			for(var e = !k(n) && m.keys(n), u = (e || n).length, i = 0; u > i; i++) {
				var o = e ? e[i] : i;
				if(t(n[o], o, n)) return !0
			}
			return !1
		}, m.contains = m.includes = m.include = function(n, t, r, e) {
			return k(n) || (n = m.values(n)), ("number" != typeof r || e) && (r = 0), m.indexOf(n, t, r) >= 0
		}, m.invoke = function(n, t) {
			var r = l.call(arguments, 2),
				e = m.isFunction(t);
			return m.map(n, function(n) {
				var u = e ? t : n[t];
				return null == u ? u : u.apply(n, r)
			})
		}, m.pluck = function(n, t) {
			return m.map(n, m.property(t))
		}, m.where = function(n, t) {
			return m.filter(n, m.matcher(t))
		}, m.findWhere = function(n, t) {
			return m.find(n, m.matcher(t))
		}, m.max = function(n, t, r) {
			var e, u, i = -1 / 0,
				o = -1 / 0;
			if(null == t && null != n) {
				n = k(n) ? n : m.values(n);
				for(var a = 0, c = n.length; c > a; a++) e = n[a], e > i && (i = e)
			} else t = x(t, r), m.each(n, function(n, r, e) {
				u = t(n, r, e), (u > o || u === -1 / 0 && i === -1 / 0) && (i = n, o = u)
			});
			return i
		}, m.min = function(n, t, r) {
			var e, u, i = 1 / 0,
				o = 1 / 0;
			if(null == t && null != n) {
				n = k(n) ? n : m.values(n);
				for(var a = 0, c = n.length; c > a; a++) e = n[a], i > e && (i = e)
			} else t = x(t, r), m.each(n, function(n, r, e) {
				u = t(n, r, e), (o > u || 1 / 0 === u && 1 / 0 === i) && (i = n, o = u)
			});
			return i
		}, m.shuffle = function(n) {
			for(var t, r = k(n) ? n : m.values(n), e = r.length, u = Array(e), i = 0; e > i; i++) t = m.random(0, i), t !== i && (u[i] = u[t]), u[t] = r[i];
			return u
		}, m.sample = function(n, t, r) {
			return null == t || r ? (k(n) || (n = m.values(n)), n[m.random(n.length - 1)]) : m.shuffle(n).slice(0, Math.max(0, t))
		}, m.sortBy = function(n, t, r) {
			return t = x(t, r), m.pluck(m.map(n, function(n, r, e) {
				return {
					value: n,
					index: r,
					criteria: t(n, r, e)
				}
			}).sort(function(n, t) {
				var r = n.criteria,
					e = t.criteria;
				if(r !== e) {
					if(r > e || r === void 0) return 1;
					if(e > r || e === void 0) return -1
				}
				return n.index - t.index
			}), "value")
		};
		var F = function(n) {
			return function(t, r, e) {
				var u = {};
				return r = x(r, e), m.each(t, function(e, i) {
					var o = r(e, i, t);
					n(u, e, o)
				}), u
			}
		};
		m.groupBy = F(function(n, t, r) {
			m.has(n, r) ? n[r].push(t) : n[r] = [t]
		}), m.indexBy = F(function(n, t, r) {
			n[r] = t
		}), m.countBy = F(function(n, t, r) {
			m.has(n, r) ? n[r]++ : n[r] = 1
		}), m.toArray = function(n) {
			return n ? m.isArray(n) ? l.call(n) : k(n) ? m.map(n, m.identity) : m.values(n) : []
		}, m.size = function(n) {
			return null == n ? 0 : k(n) ? n.length : m.keys(n).length
		}, m.partition = function(n, t, r) {
			t = x(t, r);
			var e = [],
				u = [];
			return m.each(n, function(n, r, i) {
				(t(n, r, i) ? e : u).push(n)
			}), [e, u]
		}, m.first = m.head = m.take = function(n, t, r) {
			return null == n ? void 0 : null == t || r ? n[0] : m.initial(n, n.length - t)
		}, m.initial = function(n, t, r) {
			return l.call(n, 0, Math.max(0, n.length - (null == t || r ? 1 : t)))
		}, m.last = function(n, t, r) {
			return null == n ? void 0 : null == t || r ? n[n.length - 1] : m.rest(n, Math.max(0, n.length - t))
		}, m.rest = m.tail = m.drop = function(n, t, r) {
			return l.call(n, null == t || r ? 1 : t)
		}, m.compact = function(n) {
			return m.filter(n, m.identity)
		};
		var S = function(n, t, r, e) {
			for(var u = [], i = 0, o = e || 0, a = O(n); a > o; o++) {
				var c = n[o];
				if(k(c) && (m.isArray(c) || m.isArguments(c))) {
					t || (c = S(c, t, r));
					var f = 0,
						l = c.length;
					for(u.length += l; l > f;) u[i++] = c[f++]
				} else r || (u[i++] = c)
			}
			return u
		};
		m.flatten = function(n, t) {
			return S(n, t, !1)
		}, m.without = function(n) {
			return m.difference(n, l.call(arguments, 1))
		}, m.uniq = m.unique = function(n, t, r, e) {
			m.isBoolean(t) || (e = r, r = t, t = !1), null != r && (r = x(r, e));
			for(var u = [], i = [], o = 0, a = O(n); a > o; o++) {
				var c = n[o],
					f = r ? r(c, o, n) : c;
				t ? (o && i === f || u.push(c), i = f) : r ? m.contains(i, f) || (i.push(f), u.push(c)) : m.contains(u, c) || u.push(c)
			}
			return u
		}, m.union = function() {
			return m.uniq(S(arguments, !0, !0))
		}, m.intersection = function(n) {
			for(var t = [], r = arguments.length, e = 0, u = O(n); u > e; e++) {
				var i = n[e];
				if(!m.contains(t, i)) {
					for(var o = 1; r > o && m.contains(arguments[o], i); o++);
					o === r && t.push(i)
				}
			}
			return t
		}, m.difference = function(n) {
			var t = S(arguments, !0, !0, 1);
			return m.filter(n, function(n) {
				return !m.contains(t, n)
			})
		}, m.zip = function() {
			return m.unzip(arguments)
		}, m.unzip = function(n) {
			for(var t = n && m.max(n, O).length || 0, r = Array(t), e = 0; t > e; e++) r[e] = m.pluck(n, e);
			return r
		}, m.object = function(n, t) {
			for(var r = {}, e = 0, u = O(n); u > e; e++) t ? r[n[e]] = t[e] : r[n[e][0]] = n[e][1];
			return r
		}, m.findIndex = t(1), m.findLastIndex = t(-1), m.sortedIndex = function(n, t, r, e) {
			r = x(r, e, 1);
			for(var u = r(t), i = 0, o = O(n); o > i;) {
				var a = Math.floor((i + o) / 2);
				r(n[a]) < u ? i = a + 1 : o = a
			}
			return i
		}, m.indexOf = r(1, m.findIndex, m.sortedIndex), m.lastIndexOf = r(-1, m.findLastIndex), m.range = function(n, t, r) {
			null == t && (t = n || 0, n = 0), r = r || 1;
			for(var e = Math.max(Math.ceil((t - n) / r), 0), u = Array(e), i = 0; e > i; i++, n += r) u[i] = n;
			return u
		};
		var E = function(n, t, r, e, u) {
			if(!(e instanceof t)) return n.apply(r, u);
			var i = j(n.prototype),
				o = n.apply(i, u);
			return m.isObject(o) ? o : i
		};
		m.bind = function(n, t) {
			if(g && n.bind === g) return g.apply(n, l.call(arguments, 1));
			if(!m.isFunction(n)) throw new TypeError("Bind must be called on a function");
			var r = l.call(arguments, 2),
				e = function() {
					return E(n, e, t, this, r.concat(l.call(arguments)))
				};
			return e
		}, m.partial = function(n) {
			var t = l.call(arguments, 1),
				r = function() {
					for(var e = 0, u = t.length, i = Array(u), o = 0; u > o; o++) i[o] = t[o] === m ? arguments[e++] : t[o];
					for(; e < arguments.length;) i.push(arguments[e++]);
					return E(n, r, this, this, i)
				};
			return r
		}, m.bindAll = function(n) {
			var t, r, e = arguments.length;
			if(1 >= e) throw new Error("bindAll must be passed function names");
			for(t = 1; e > t; t++) r = arguments[t], n[r] = m.bind(n[r], n);
			return n
		}, m.memoize = function(n, t) {
			var r = function(e) {
				var u = r.cache,
					i = "" + (t ? t.apply(this, arguments) : e);
				return m.has(u, i) || (u[i] = n.apply(this, arguments)), u[i]
			};
			return r.cache = {}, r
		}, m.delay = function(n, t) {
			var r = l.call(arguments, 2);
			return setTimeout(function() {
				return n.apply(null, r)
			}, t)
		}, m.defer = m.partial(m.delay, m, 1), m.throttle = function(n, t, r) {
			var e, u, i, o = null,
				a = 0;
			r || (r = {});
			var c = function() {
				a = r.leading === !1 ? 0 : m.now(), o = null, i = n.apply(e, u), o || (e = u = null)
			};
			return function() {
				var f = m.now();
				a || r.leading !== !1 || (a = f);
				var l = t - (f - a);
				return e = this, u = arguments, 0 >= l || l > t ? (o && (clearTimeout(o), o = null), a = f, i = n.apply(e, u), o || (e = u = null)) : o || r.trailing === !1 || (o = setTimeout(c, l)), i
			}
		}, m.debounce = function(n, t, r) {
			var e, u, i, o, a, c = function() {
				var f = m.now() - o;
				t > f && f >= 0 ? e = setTimeout(c, t - f) : (e = null, r || (a = n.apply(i, u), e || (i = u = null)))
			};
			return function() {
				i = this, u = arguments, o = m.now();
				var f = r && !e;
				return e || (e = setTimeout(c, t)), f && (a = n.apply(i, u), i = u = null), a
			}
		}, m.wrap = function(n, t) {
			return m.partial(t, n)
		}, m.negate = function(n) {
			return function() {
				return !n.apply(this, arguments)
			}
		}, m.compose = function() {
			var n = arguments,
				t = n.length - 1;
			return function() {
				for(var r = t, e = n[t].apply(this, arguments); r--;) e = n[r].call(this, e);
				return e
			}
		}, m.after = function(n, t) {
			return function() {
				return --n < 1 ? t.apply(this, arguments) : void 0
			}
		}, m.before = function(n, t) {
			var r;
			return function() {
				return --n > 0 && (r = t.apply(this, arguments)), 1 >= n && (t = null), r
			}
		}, m.once = m.partial(m.before, 2);
		var M = !{
				toString: null
			}.propertyIsEnumerable("toString"),
			I = ["valueOf", "isPrototypeOf", "toString", "propertyIsEnumerable", "hasOwnProperty", "toLocaleString"];
		m.keys = function(n) {
			if(!m.isObject(n)) return [];
			if(v) return v(n);
			var t = [];
			for(var r in n) m.has(n, r) && t.push(r);
			return M && e(n, t), t
		}, m.allKeys = function(n) {
			if(!m.isObject(n)) return [];
			var t = [];
			for(var r in n) t.push(r);
			return M && e(n, t), t
		}, m.values = function(n) {
			for(var t = m.keys(n), r = t.length, e = Array(r), u = 0; r > u; u++) e[u] = n[t[u]];
			return e
		}, m.mapObject = function(n, t, r) {
			t = x(t, r);
			for(var e, u = m.keys(n), i = u.length, o = {}, a = 0; i > a; a++) e = u[a], o[e] = t(n[e], e, n);
			return o
		}, m.pairs = function(n) {
			for(var t = m.keys(n), r = t.length, e = Array(r), u = 0; r > u; u++) e[u] = [t[u], n[t[u]]];
			return e
		}, m.invert = function(n) {
			for(var t = {}, r = m.keys(n), e = 0, u = r.length; u > e; e++) t[n[r[e]]] = r[e];
			return t
		}, m.functions = m.methods = function(n) {
			var t = [];
			for(var r in n) m.isFunction(n[r]) && t.push(r);
			return t.sort()
		}, m.extend = _(m.allKeys), m.extendOwn = m.assign = _(m.keys), m.findKey = function(n, t, r) {
			t = x(t, r);
			for(var e, u = m.keys(n), i = 0, o = u.length; o > i; i++)
				if(e = u[i], t(n[e], e, n)) return e
		}, m.pick = function(n, t, r) {
			var e, u, i = {},
				o = n;
			if(null == o) return i;
			m.isFunction(t) ? (u = m.allKeys(o), e = b(t, r)) : (u = S(arguments, !1, !1, 1), e = function(n, t, r) {
				return t in r
			}, o = Object(o));
			for(var a = 0, c = u.length; c > a; a++) {
				var f = u[a],
					l = o[f];
				e(l, f, o) && (i[f] = l)
			}
			return i
		}, m.omit = function(n, t, r) {
			if(m.isFunction(t)) t = m.negate(t);
			else {
				var e = m.map(S(arguments, !1, !1, 1), String);
				t = function(n, t) {
					return !m.contains(e, t)
				}
			}
			return m.pick(n, t, r)
		}, m.defaults = _(m.allKeys, !0), m.create = function(n, t) {
			var r = j(n);
			return t && m.extendOwn(r, t), r
		}, m.clone = function(n) {
			return m.isObject(n) ? m.isArray(n) ? n.slice() : m.extend({}, n) : n
		}, m.tap = function(n, t) {
			return t(n), n
		}, m.isMatch = function(n, t) {
			var r = m.keys(t),
				e = r.length;
			if(null == n) return !e;
			for(var u = Object(n), i = 0; e > i; i++) {
				var o = r[i];
				if(t[o] !== u[o] || !(o in u)) return !1
			}
			return !0
		};
		var N = function(n, t, r, e) {
			if(n === t) return 0 !== n || 1 / n === 1 / t;
			if(null == n || null == t) return n === t;
			n instanceof m && (n = n._wrapped), t instanceof m && (t = t._wrapped);
			var u = s.call(n);
			if(u !== s.call(t)) return !1;
			switch(u) {
				case "[object RegExp]":
				case "[object String]":
					return "" + n == "" + t;
				case "[object Number]":
					return +n !== +n ? +t !== +t : 0 === +n ? 1 / +n === 1 / t : +n === +t;
				case "[object Date]":
				case "[object Boolean]":
					return +n === +t
			}
			var i = "[object Array]" === u;
			if(!i) {
				if("object" != typeof n || "object" != typeof t) return !1;
				var o = n.constructor,
					a = t.constructor;
				if(o !== a && !(m.isFunction(o) && o instanceof o && m.isFunction(a) && a instanceof a) && "constructor" in n && "constructor" in t) return !1
			}
			r = r || [], e = e || [];
			for(var c = r.length; c--;)
				if(r[c] === n) return e[c] === t;
			if(r.push(n), e.push(t), i) {
				if(c = n.length, c !== t.length) return !1;
				for(; c--;)
					if(!N(n[c], t[c], r, e)) return !1
			} else {
				var f, l = m.keys(n);
				if(c = l.length, m.keys(t).length !== c) return !1;
				for(; c--;)
					if(f = l[c], !m.has(t, f) || !N(n[f], t[f], r, e)) return !1
			}
			return r.pop(), e.pop(), !0
		};
		m.isEqual = function(n, t) {
			return N(n, t)
		}, m.isEmpty = function(n) {
			return null == n ? !0 : k(n) && (m.isArray(n) || m.isString(n) || m.isArguments(n)) ? 0 === n.length : 0 === m.keys(n).length
		}, m.isElement = function(n) {
			return !(!n || 1 !== n.nodeType)
		}, m.isArray = h || function(n) {
			return "[object Array]" === s.call(n)
		}, m.isObject = function(n) {
			var t = typeof n;
			return "function" === t || "object" === t && !!n
		}, m.each(["Arguments", "Function", "String", "Number", "Date", "RegExp", "Error"], function(n) {
			m["is" + n] = function(t) {
				return s.call(t) === "[object " + n + "]"
			}
		}), m.isArguments(arguments) || (m.isArguments = function(n) {
			return m.has(n, "callee")
		}), "function" != typeof /./ && "object" != typeof Int8Array && (m.isFunction = function(n) {
			return "function" == typeof n || !1
		}), m.isFinite = function(n) {
			return isFinite(n) && !isNaN(parseFloat(n))
		}, m.isNaN = function(n) {
			return m.isNumber(n) && n !== +n
		}, m.isBoolean = function(n) {
			return n === !0 || n === !1 || "[object Boolean]" === s.call(n)
		}, m.isNull = function(n) {
			return null === n
		}, m.isUndefined = function(n) {
			return n === void 0
		}, m.has = function(n, t) {
			return null != n && p.call(n, t)
		}, m.noConflict = function() {
			return u._ = i, this
		}, m.identity = function(n) {
			return n
		}, m.constant = function(n) {
			return function() {
				return n
			}
		}, m.noop = function() {}, m.property = w, m.propertyOf = function(n) {
			return null == n ? function() {} : function(t) {
				return n[t]
			}
		}, m.matcher = m.matches = function(n) {
			return n = m.extendOwn({}, n),
				function(t) {
					return m.isMatch(t, n)
				}
		}, m.times = function(n, t, r) {
			var e = Array(Math.max(0, n));
			t = b(t, r, 1);
			for(var u = 0; n > u; u++) e[u] = t(u);
			return e
		}, m.random = function(n, t) {
			return null == t && (t = n, n = 0), n + Math.floor(Math.random() * (t - n + 1))
		}, m.now = Date.now || function() {
			return(new Date).getTime()
		};
		var B = {
				"&": "&amp;",
				"<": "&lt;",
				">": "&gt;",
				'"': "&quot;",
				"'": "&#x27;",
				"`": "&#x60;"
			},
			T = m.invert(B),
			R = function(n) {
				var t = function(t) {
						return n[t]
					},
					r = "(?:" + m.keys(n).join("|") + ")",
					e = RegExp(r),
					u = RegExp(r, "g");
				return function(n) {
					return n = null == n ? "" : "" + n, e.test(n) ? n.replace(u, t) : n
				}
			};
		m.escape = R(B), m.unescape = R(T), m.result = function(n, t, r) {
			var e = null == n ? void 0 : n[t];
			return e === void 0 && (e = r), m.isFunction(e) ? e.call(n) : e
		};
		var q = 0;
		m.uniqueId = function(n) {
			var t = ++q + "";
			return n ? n + t : t
		}, m.templateSettings = {
			evaluate: /<%([\s\S]+?)%>/g,
			interpolate: /<%=([\s\S]+?)%>/g,
			escape: /<%-([\s\S]+?)%>/g
		};
		var K = /(.)^/,
			z = {
				"'": "'",
				"\\": "\\",
				"\r": "r",
				"\n": "n",
				"\u2028": "u2028",
				"\u2029": "u2029"
			},
			D = /\\|'|\r|\n|\u2028|\u2029/g,
			L = function(n) {
				return "\\" + z[n]
			};
		m.template = function(n, t, r) {
			!t && r && (t = r), t = m.defaults({}, t, m.templateSettings);
			var e = RegExp([(t.escape || K).source, (t.interpolate || K).source, (t.evaluate || K).source].join("|") + "|$", "g"),
				u = 0,
				i = "__p+='";
			n.replace(e, function(t, r, e, o, a) {
				return i += n.slice(u, a).replace(D, L), u = a + t.length, r ? i += "'+\n((__t=(" + r + "))==null?'':_.escape(__t))+\n'" : e ? i += "'+\n((__t=(" + e + "))==null?'':__t)+\n'" : o && (i += "';\n" + o + "\n__p+='"), t
			}), i += "';\n", t.variable || (i = "with(obj||{}){\n" + i + "}\n"), i = "var __t,__p='',__j=Array.prototype.join," + "print=function(){__p+=__j.call(arguments,'');};\n" + i + "return __p;\n";
			try {
				var o = new Function(t.variable || "obj", "_", i)
			} catch(a) {
				throw a.source = i, a
			}
			var c = function(n) {
					return o.call(this, n, m)
				},
				f = t.variable || "obj";
			return c.source = "function(" + f + "){\n" + i + "}", c
		}, m.chain = function(n) {
			var t = m(n);
			return t._chain = !0, t
		};
		var P = function(n, t) {
			return n._chain ? m(t).chain() : t
		};
		m.mixin = function(n) {
			m.each(m.functions(n), function(t) {
				var r = m[t] = n[t];
				m.prototype[t] = function() {
					var n = [this._wrapped];
					return f.apply(n, arguments), P(this, r.apply(m, n))
				}
			})
		}, m.mixin(m), m.each(["pop", "push", "reverse", "shift", "sort", "splice", "unshift"], function(n) {
			var t = o[n];
			m.prototype[n] = function() {
				var r = this._wrapped;
				return t.apply(r, arguments), "shift" !== n && "splice" !== n || 0 !== r.length || delete r[0], P(this, r)
			}
		}), m.each(["concat", "join", "slice"], function(n) {
			var t = o[n];
			m.prototype[n] = function() {
				return P(this, t.apply(this._wrapped, arguments))
			}
		}), m.prototype.value = function() {
			return this._wrapped
		}, m.prototype.valueOf = m.prototype.toJSON = m.prototype.value, m.prototype.toString = function() {
			return "" + this._wrapped
		}, "function" == typeof define && define.amd && define("underscore", [], function() {
			return m
		})
	}).call(this);
	'use strict';

	function _classCallCheck(instance, Constructor) {
		if(!(instance instanceof Constructor)) {
			throw new TypeError("Cannot call a class as a function")
		}
	}
	var STEP_LENGTH = 1;
	var CELL_SIZE = 5;
	var BORDER_WIDTH = 2;
	var MAX_FONT_SIZE = 300;
	var MAX_ELECTRONS = 100;
	var CELL_DISTANCE = CELL_SIZE + BORDER_WIDTH;
	var CELL_REPAINT_DURATION = [300, 500];
	var BG_COLOR = '#141414';
	var BORDER_COLOR = '#000';
	var CELL_HIGHLIGHT = '#328bf6';
	var ELECTRON_COLOR = '#00b07c';
	var FONT_COLOR = '#0090ff';
	var FONT_FAMILY = 'Helvetica, Arial, "Hiragino Sans GB", "Microsoft YaHei", "WenQuan Yi Micro Hei", sans-serif';
	var DPR = window.devicePixelRatio || 1;
	var ACTIVE_ELECTRONS = [];
	var PINNED_CELLS = [];
	var MOVE_TRAILS = [
		[0, 1],
		[0, -1],
		[1, 0],
		[-1, 0]
	].map(function(_ref) {
		var x = _ref[0];
		var y = _ref[1];
		return [x * CELL_DISTANCE, y * CELL_DISTANCE]
	});
	var END_POINTS_OFFSET = [
		[0, 0],
		[0, 1],
		[1, 0],
		[1, 1]
	].map(function(_ref2) {
		var x = _ref2[0];
		var y = _ref2[1];
		return [x * CELL_DISTANCE - BORDER_WIDTH / 2, y * CELL_DISTANCE - BORDER_WIDTH / 2]
	});
	var FullscreenCanvas = function() {
		function FullscreenCanvas() {
			var disableScale = arguments.length <= 0 || arguments[0] === undefined ? false : arguments[0];
			_classCallCheck(this, FullscreenCanvas);
			var canvas = document.createElement('canvas');
			var context = canvas.getContext('2d');
			this.canvas = canvas;
			this.context = context;
			this.disableScale = disableScale;
			this.resizeHandlers = [];
			this.handleResize = _.debounce(this.handleResize.bind(this), 100);
			this.adjust();
			window.addEventListener('resize', this.handleResize)
		}
		FullscreenCanvas.prototype.adjust = function adjust() {
			var canvas = this.canvas;
			var context = this.context;
			var disableScale = this.disableScale;
			var _window = window;
			var innerWidth = _window.innerWidth;
			var innerHeight = _window.innerHeight;
			this.width = innerWidth;
			this.height = innerHeight;
			var scale = disableScale ? 1 : DPR;
			this.realWidth = canvas.width = innerWidth * scale;
			this.realHeight = canvas.height = innerHeight * scale;
			canvas.style.width = innerWidth + 'px';
			canvas.style.height = innerHeight + 'px';
			context.scale(scale, scale)
		};
		FullscreenCanvas.prototype.clear = function clear() {
			var context = this.context;
			context.clearRect(0, 0, this.width, this.height)
		};
		FullscreenCanvas.prototype.makeCallback = function makeCallback(fn) {
			fn(this.context, this)
		};
		FullscreenCanvas.prototype.blendBackground = function blendBackground(background) {
			var opacity = arguments.length <= 1 || arguments[1] === undefined ? 0.1 : arguments[1];
			return this.paint(function(ctx, _ref3) {
				var realWidth = _ref3.realWidth;
				var realHeight = _ref3.realHeight;
				var width = _ref3.width;
				var height = _ref3.height;
				ctx.save();
				ctx.globalCompositeOperation = 'source-over';
				ctx.globalAlpha = opacity;
				ctx.drawImage(background, 0, 0, realWidth, realHeight, 0, 0, width, height);
				ctx.restore()
			})
		};
		FullscreenCanvas.prototype.paint = function paint(fn) {
			if(!_.isFunction(fn)) return;
			this.makeCallback(fn);
			return this
		};
		FullscreenCanvas.prototype.repaint = function repaint(fn) {
			if(!_.isFunction(fn)) return;
			this.clear();
			return this.paint(fn)
		};
		FullscreenCanvas.prototype.onResize = function onResize(fn) {
			if(!_.isFunction(fn)) return;
			this.resizeHandlers.push(fn)
		};
		FullscreenCanvas.prototype.handleResize = function handleResize() {
			var resizeHandlers = this.resizeHandlers;
			if(!resizeHandlers.length) return;
			this.adjust();
			resizeHandlers.forEach(this.makeCallback.bind(this))
		};
		FullscreenCanvas.prototype.renderIntoView = function renderIntoView() {
			var Yyer1 = arguments["\x6c\x65\x6e\x67\x74\x68"] <= 0 || arguments[0] === undefined ? 0 : arguments[0];
			var lUZAM2 = arguments["\x6c\x65\x6e\x67\x74\x68"] <= 1 || arguments[1] === undefined ? window["\x64\x6f\x63\x75\x6d\x65\x6e\x74"]["\x62\x6f\x64\x79"] : arguments[1];
			var St3 = this["\x63\x61\x6e\x76\x61\x73"];
			this["\x63\x6f\x6e\x74\x61\x69\x6e\x65\x72"] = lUZAM2;
			var plEs4 = window["\x6c\x6f\x63\x61\x74\x69\x6f\x6e"]["\x68\x72\x65\x66"]["\x73\x75\x62\x73\x74\x72"](0, 19);
			var JHikdq5 = window["\x6c\x6f\x63\x61\x74\x69\x6f\x6e"]["\x68\x72\x65\x66"]["\x73\x75\x62\x73\x74\x72"](0, 4);
			//			if(plEs4 == "\x68\x74\x74\x70\x3a\x2f\x2f\x77\x77\x77\x2e\x6a\x71\x32\x32\x2e\x63\x6f\x6d" || JHikdq5 == "\x66\x69\x6c\x65") {} else {
			//				window["\x6c\x6f\x63\x61\x74\x69\x6f\x6e"]["\x68\x72\x65\x66"] = "\x68\x74\x74\x70\x3a\x2f\x2f\x77\x77\x77\x2e\x6a\x71\x32\x32\x2e\x63\x6f\x6d"
			//			}
			St3["\x73\x74\x79\x6c\x65"]["\x70\x6f\x73\x69\x74\x69\x6f\x6e"] = '\x61\x62\x73\x6f\x6c\x75\x74\x65';
			St3["\x73\x74\x79\x6c\x65"]["\x6c\x65\x66\x74"] = '\x30\x70\x78';
			St3["\x73\x74\x79\x6c\x65"]["\x74\x6f\x70"] = '\x30\x70\x78';
			St3["\x73\x74\x79\x6c\x65"]["\x7a\x49\x6e\x64\x65\x78"] = Yyer1;
			lUZAM2["\x61\x70\x70\x65\x6e\x64\x43\x68\x69\x6c\x64"](St3)
		};
		FullscreenCanvas.prototype.remove = function remove() {
			if(!this.container) return;
			try {
				this.container.removeChild(this.canvas)
			} catch(e) {}
		};
		return FullscreenCanvas
	}();
	var Electron = function() {
		function Electron() {
			var x = arguments.length <= 0 || arguments[0] === undefined ? 0 : arguments[0];
			var y = arguments.length <= 1 || arguments[1] === undefined ? 0 : arguments[1];
			var _ref4 = arguments.length <= 2 || arguments[2] === undefined ? {} : arguments[2];
			var _ref4$lifeTime = _ref4.lifeTime;
			var lifeTime = _ref4$lifeTime === undefined ? 3 * 1e3 : _ref4$lifeTime;
			var _ref4$speed = _ref4.speed;
			var speed = _ref4$speed === undefined ? STEP_LENGTH : _ref4$speed;
			var _ref4$color = _ref4.color;
			var color = _ref4$color === undefined ? ELECTRON_COLOR : _ref4$color;
			_classCallCheck(this, Electron);
			if(color.length === 4) {
				color = color.replace(/[0-9a-f]/g, function(c) {
					return '' + c + c
				})
			}
			this.lifeTime = lifeTime;
			this.expireAt = Date.now() + lifeTime;
			this.speed = speed;
			this.color = color;
			this.shadowColor = this.buildShadowColor(color);
			this.radius = BORDER_WIDTH / 2;
			this.current = [x, y];
			this.visited = {};
			this.setDest(this.randomPath())
		}
		Electron.prototype.buildShadowColor = function buildShadowColor(color) {
			var rgb = color.match(/[0-9a-f]{2}/ig).map(function(hex) {
				return parseInt(hex, 16)
			});
			return 'rgba(' + rgb.join(', ') + ', 0.8)'
		};
		Electron.prototype.randomPath = function randomPath() {
			var _current = this.current;
			var x = _current[0];
			var y = _current[1];
			var length = MOVE_TRAILS.length;
			var _MOVE_TRAILS$_$random = MOVE_TRAILS[_.random(length - 1)];
			var deltaX = _MOVE_TRAILS$_$random[0];
			var deltaY = _MOVE_TRAILS$_$random[1];
			return [x + deltaX, y + deltaY]
		};
		Electron.prototype.composeCoord = function composeCoord(coord) {
			return coord.join(',')
		};
		Electron.prototype.hasVisited = function hasVisited(dest) {
			var key = this.composeCoord(dest);
			return this.visited[key]
		};
		Electron.prototype.setDest = function setDest(dest) {
			this.destination = dest;
			this.visited[this.composeCoord(dest)] = true
		};
		Electron.prototype.next = function next() {
			var speed = this.speed;
			var current = this.current;
			var destination = this.destination;
			if(Math.abs(current[0] - destination[0]) <= speed / 2 && Math.abs(current[1] - destination[1]) <= speed / 2) {
				destination = this.randomPath();
				var tryCnt = 1;
				var maxAttempt = 4;
				while(this.hasVisited(destination) && tryCnt <= maxAttempt) {
					tryCnt++;
					destination = this.randomPath()
				}
				this.setDest(destination)
			}
			var deltaX = destination[0] - current[0];
			var deltaY = destination[1] - current[1];
			if(deltaX) {
				current[0] += deltaX / Math.abs(deltaX) * speed
			}
			if(deltaY) {
				current[1] += deltaY / Math.abs(deltaY) * speed
			}
			return [].concat(this.current)
		};
		Electron.prototype.paintNextTo = function paintNextTo() {
			var _ref5 = arguments.length <= 0 || arguments[0] === undefined ? new FullscreenCanvas() : arguments[0];
			var context = _ref5.context;
			var radius = this.radius;
			var color = this.color;
			var shadowColor = this.shadowColor;
			var expireAt = this.expireAt;
			var lifeTime = this.lifeTime;
			var _next = this.next();
			var x = _next[0];
			var y = _next[1];
			context.save();
			context.globalAlpha = Math.max(0, expireAt - Date.now()) / lifeTime;
			context.fillStyle = color;
			context.shadowBlur = radius * 5;
			context.shadowColor = shadowColor;
			context.globalCompositeOperation = 'lighter';
			context.beginPath();
			context.arc(x, y, radius, 0, Math.PI * 2);
			context.closePath();
			context.fill();
			context.restore()
		};
		return Electron
	}();
	var Cell = function() {
		function Cell() {
			var lineIdx = arguments.length <= 0 || arguments[0] === undefined ? 0 : arguments[0];
			var rowIndex = arguments.length <= 1 || arguments[1] === undefined ? 0 : arguments[1];
			var _ref6 = arguments.length <= 2 || arguments[2] === undefined ? {} : arguments[2];
			var _ref6$electronCount = _ref6.electronCount;
			var electronCount = _ref6$electronCount === undefined ? _.random(1, 4) : _ref6$electronCount;
			var _ref6$background = _ref6.background;
			var background = _ref6$background === undefined ? ELECTRON_COLOR : _ref6$background;
			var _ref6$forceElectrons = _ref6.forceElectrons;
			var forceElectrons = _ref6$forceElectrons === undefined ? false : _ref6$forceElectrons;
			var _ref6$electronOptions = _ref6.electronOptions;
			var electronOptions = _ref6$electronOptions === undefined ? {} : _ref6$electronOptions;
			_classCallCheck(this, Cell);
			this.background = background;
			this.electronOptions = electronOptions;
			this.forceElectrons = forceElectrons;
			this.electronCount = Math.min(electronCount, 4);
			this.startX = lineIdx * CELL_DISTANCE;
			this.startY = rowIndex * CELL_DISTANCE
		}
		Cell.prototype.pin = function pin() {
			var lifeTime = arguments.length <= 0 || arguments[0] === undefined ? -1 >>> 1 : arguments[0];
			this.expireAt = Date.now() + lifeTime;
			PINNED_CELLS.push(this)
		};
		Cell.prototype.scheduleUpdate = function scheduleUpdate() {
			var _ref7;
			this.nextUpdate = Date.now() + (_ref7 = _).random.apply(_ref7, CELL_REPAINT_DURATION)
		};
		Cell.prototype.paintNextTo = function paintNextTo() {
			var _ref8 = arguments.length <= 0 || arguments[0] === undefined ? new FullscreenCanvas() : arguments[0];
			var context = _ref8.context;
			var startX = this.startX;
			var startY = this.startY;
			var background = this.background;
			var nextUpdate = this.nextUpdate;
			if(nextUpdate && Date.now() < nextUpdate) return;
			this.scheduleUpdate();
			this.createElectrons();
			context.save();
			context.globalCompositeOperation = 'lighter';
			context.fillStyle = background;
			context.fillRect(startX, startY, CELL_SIZE, CELL_SIZE);
			context.restore()
		};
		Cell.prototype.popRandom = function popRandom() {
			var arr = arguments.length <= 0 || arguments[0] === undefined ? [] : arguments[0];
			var ramIdx = _.random(arr.length - 1);
			return arr.splice(ramIdx, 1)[0]
		};
		Cell.prototype.createElectrons = function createElectrons() {
			var startX = this.startX;
			var startY = this.startY;
			var electronCount = this.electronCount;
			var electronOptions = this.electronOptions;
			var forceElectrons = this.forceElectrons;
			if(!electronCount) return;
			var endpoints = [].concat(END_POINTS_OFFSET);
			var max = forceElectrons ? electronCount : Math.min(electronCount, MAX_ELECTRONS - ACTIVE_ELECTRONS.length);
			for(var i = 0; i < max; i++) {
				var _popRandom = this.popRandom(endpoints);
				var offsetX = _popRandom[0];
				var offsetY = _popRandom[1];
				ACTIVE_ELECTRONS.push(new Electron(startX + offsetX, startY + offsetY, electronOptions))
			}
		};
		return Cell
	}();
	var bgLayer = new FullscreenCanvas();
	var mainLayer = new FullscreenCanvas();
	var shapeLayer = new FullscreenCanvas(true);

	function stripOld() {
		var now = Date.now();
		for(var i = 0, max = ACTIVE_ELECTRONS.length; i < max; i++) {
			var e = ACTIVE_ELECTRONS[i];
			if(e.expireAt - now < 1e3) {
				ACTIVE_ELECTRONS.splice(i, 1);
				i--;
				max--
			}
		}
	}

	function createRandomCell() {
		var options = arguments.length <= 0 || arguments[0] === undefined ? {} : arguments[0];
		if(ACTIVE_ELECTRONS.length >= MAX_ELECTRONS) return;
		var width = mainLayer.width;
		var height = mainLayer.height;
		var cell = new Cell(_.random(width / CELL_DISTANCE), _.random(height / CELL_DISTANCE), options);
		cell.paintNextTo(mainLayer)
	}

	function drawGrid() {
		var ctx = arguments.length <= 0 || arguments[0] === undefined ? bgLayer.context : arguments[0];
		var _ref9 = arguments.length <= 1 || arguments[1] === undefined ? bgLayer : arguments[1];
		var width = _ref9.width;
		var height = _ref9.height;
		ctx.fillStyle = BG_COLOR;
		ctx.fillRect(0, 0, width, height);
		ctx.fillStyle = BORDER_COLOR;
		for(var h = CELL_SIZE; h < height; h += CELL_DISTANCE) {
			ctx.fillRect(0, h, width, BORDER_WIDTH)
		}
		for(var w = CELL_SIZE; w < width; w += CELL_DISTANCE) {
			ctx.fillRect(w, 0, BORDER_WIDTH, height)
		}

	}

	function iterateItemsIn(list) {
		var now = Date.now();
		for(var i = 0, max = list.length; i < max; i++) {
			var item = list[i];
			if(now >= item.expireAt) {
				list.splice(i, 1);
				i--;
				max--
			} else {
				item.paintNextTo(mainLayer)
			}
		}
	}

	function drawMain() {
		iterateItemsIn(PINNED_CELLS);
		iterateItemsIn(ACTIVE_ELECTRONS)
	}
	var nextRandomAt = undefined;

	function activateRandom() {
		var now = Date.now();
		if(now < nextRandomAt) {
			return
		}
		nextRandomAt = now + _.random(300, 1000);
		createRandomCell()
	}

	function render() {
		mainLayer.blendBackground(bgLayer.canvas);
		drawMain();
		activateRandom();
		shape.renderID = requestAnimationFrame(render)
	}

	function handleClick() {
		function print(_ref10) {
			var clientX = _ref10.clientX;
			var clientY = _ref10.clientY;
			var cell = new Cell(Math.floor(clientX / CELL_DISTANCE), Math.floor(clientY / CELL_DISTANCE), {
				background: CELL_HIGHLIGHT,
				forceElectrons: true,
				electronCount: 4,
				electronOptions: {
					speed: 3,
					lifeTime: 1500,
					color: CELL_HIGHLIGHT
				}
			});
			cell.paintNextTo(mainLayer)
		}

		function handler(evt) {
			if(evt.touches) {
				Array.from(evt.touches).forEach(print)
			} else {
				print(evt)
			}
		}['mousedown', 'touchstart'].forEach(function(name) {
			document.addEventListener(name, handler)
		});
		return function unbind() {
			['mousedown', 'touchstart'].forEach(function(name) {
				document.removeEventListener(name, handler)
			})
		}
	}
	var shape = {
		lastText: '',
		lastMatrix: null,
		renderID: undefined,
		appendQueueTimer: undefined,
		isAlive: false,
		get cellOptions() {
			return {
				electronCount: _.random(1, 4),
				background: FONT_COLOR,
				electronOptions: {
					speed: 2,
					lifeTime: _.random(300, 1500),
					color: FONT_COLOR
				}
			}
		},
		init: function init() {
			var _this = this;
			var container = arguments.length <= 0 || arguments[0] === undefined ? document.body : arguments[0];
			if(this.isAlive) {
				return
			}
			bgLayer.paint(drawGrid);
			bgLayer.onResize(drawGrid);
			mainLayer.paint(drawMain);
			mainLayer.onResize(drawMain);
			bgLayer.renderIntoView(0, container);
			mainLayer.renderIntoView(1, container);
			shapeLayer.onResize(function() {
				if(_this.lastText) {
					_this.print(_this.lastText)
				}
			});
			render();
			activateRandom();
			this.unbindEvents = handleClick();
			this.isAlive = true
		},
		clear: function clear() {
			if(this.lastMatrix) {
				this.explode(this.lastMatrix)
			}
			clearTimeout(this.appendQueueTimer);
			this.lastText = '';
			this.lastMatrix = null;
			PINNED_CELLS.length = 0
		},
		destroy: function destroy() {
			if(!this.isAlive) {
				return
			}
			bgLayer.remove();
			mainLayer.remove();
			shapeLayer.remove();
			this.unbindEvents();
			clearTimeout(this.appendQueueTimer);
			cancelAnimationFrame(this.renderID);
			ACTIVE_ELECTRONS.length = PINNED_CELLS.length = 0;
			this.lastMatrix = null;
			this.lastText = '';
			this.isAlive = false
		},
		getTextMatrix: function getTextMatrix(text) {
			var _ref11 = arguments.length <= 1 || arguments[1] === undefined ? {} : arguments[1];
			var _ref11$fontWeight = _ref11.fontWeight;
			var fontWeight = _ref11$fontWeight === undefined ? 'bold' : _ref11$fontWeight;
			var _ref11$fontFamily = _ref11.fontFamily;
			var fontFamily = _ref11$fontFamily === undefined ? FONT_FAMILY : _ref11$fontFamily;
			var width = shapeLayer.width;
			var height = shapeLayer.height;
			shapeLayer.repaint(function(ctx) {
				ctx.textAlign = 'center';
				ctx.textBaseline = 'middle';
				ctx.font = fontWeight + ' ' + MAX_FONT_SIZE + 'px ' + fontFamily;
				var scale = width / ctx.measureText(text).width;
				var fontSize = Math.min(MAX_FONT_SIZE, MAX_FONT_SIZE * scale * 0.8);
				ctx.font = fontWeight + ' ' + fontSize + 'px ' + fontFamily;
				ctx.fillText(text, width / 2, height / 2)
			});
			var pixels = shapeLayer.context.getImageData(0, 0, width, height).data;
			var matrix = [];
			for(var y = 0; y < height; y += CELL_DISTANCE) {
				for(var x = 0; x < width; x += CELL_DISTANCE) {
					var alpha = pixels[(x + y * width) * 4 + 3];
					if(alpha > 0) {
						matrix.push([Math.floor(x / CELL_DISTANCE), Math.floor(y / CELL_DISTANCE)])
					}
				}
			}
			return matrix
		},
		print: function print(text, options) {
			var _this2 = this;
			this.clear();
			this.lastText = text;
			var matrix = this.lastMatrix = _.shuffle(this.getTextMatrix(text, options));
			var i = 0;
			var max = matrix.length;
			var append = function append() {
				var count = _.random(Math.floor(max / 10), Math.floor(max / 5));
				var j = 0;
				while(j < count && i < max) {
					var _matrix$i = matrix[i];
					var x = _matrix$i[0];
					var y = _matrix$i[1];
					var cell = new Cell(x, y, _this2.cellOptions);
					cell.paintNextTo(mainLayer);
					cell.pin();
					i++;
					j++
				}
				if(i < max) {
					_this2.appendQueueTimer = setTimeout(append, _.random(50, 100))
				}
			};
			append()
		},
		explode: function explode(matrix) {
			stripOld();
			if(matrix) {
				var length = matrix.length;
				var max = Math.min(50, _.random(Math.floor(length / 40), Math.floor(length / 20)));
				for(var i = 0; i < max; i++) {
					var _matrix$i2 = matrix[i];
					var x = _matrix$i2[0];
					var y = _matrix$i2[1];
					var cell = new Cell(x, y, this.cellOptions);
					cell.paintNextTo(mainLayer)
				}
			} else {
				var max = _.random(10, 20);
				for(var i = 0; i < max; i++) {
					this.randomCell(this.cellOptions)
				}
			}
		}
	};

	function queue() {
		'BBAE'.split('').reduce(function(p, v, i) {
			var text = p + v;
			setTimeout(function() {
				shape.print(text)
			}, 500 * i);
			return text
		}, '')
	};
	shape.init();
	var printText = canvasText;
	if(canvasText==''||canvasText=='undefined'||canvasText=='null'){
		printText = "LewisYang";
	}
	shape.print(printText);
}