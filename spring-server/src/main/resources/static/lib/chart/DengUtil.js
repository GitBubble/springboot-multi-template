
function getYPosOfAxis(condition){
	var index =[12,2,8];// FYI: index cache in the  <table class='table table-striped'> of rank.html
	var groupRank = [4]
/*   0: 排名        employNum
     1: 姓名        name
     2: 合入MR      mergedMrCount
     3: 提他ISSUE   resposbilityIssueCountBySelf
     4: 评论别人MR  noteTotalOtherCount
	 5: 评论自己MR  noteTotalCount	 
     6: MR评论数    mrCommonReplyCount
     7: 处理MR数    handleMRCount
     8: 有效ISSUE   effectiveIssueCount
     9: 全部ISSUE   issueTotalCount
     10:自提ISSUE   effectiveIssueCountBySelf
     11:闭环ISSUE   closeIssueCount
	 12:流控计算分  personalScore  */
	 
	if(condition === 'personalScore') return index[0];
	if(condition === 'mergedMrCount') return index[1];
	if(condition === 'effectiveIssueCount') return index[2];
	if(!condition) return groupRank[0];
	return index[0];
}
function getRandomIntInclusive(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min; //The maximum is inclusive and the minimum is inclusive 
}
function drawChart(ctx,src,condition)
{
    //var ctx = document.getElementById(ctx);
    var rank = src;
    var length = rank.children().length;
	
	if(length == 0) {
		console.log(" the "+$(src)[0].toString() + "has no values...");
		return;
	}
	
    var rankScore = new Array(length);
    var rankName = new Array(length);
	var nameOffset = 1 ;// fix to be offset 1; see table design in function getYPosOfAxis	
    for(var i=0 ; i < length; i++)
    {
       rankName[i] = $($(rank.children()[i]).children()[nameOffset]).text();// coloum 1 is the name./pls do not change it. /deng
	   rankScore[i] = $($(rank.children()[i]).children()[getYPosOfAxis(condition)]).text(); // y axis data. offset decided by outside call
    }
    
    var backgroundColor = [].fill.call(new Array(length),'rgba(75, 192, 192, 0.2)');
    var borderColor = [].fill.call(new Array(length),'rgba(75, 192, 192, 0.2)');
    
    //fill top five
    var top5bg = ['rgba(255, 99, 132, 0.2)','rgba(255, 159, 64, 0.2)','rgba(255, 205, 86, 0.2)','rgba(75, 192, 192, 0.2)'];
    var top5bw = ['rgb(255, 99, 132)','rgb(255, 159, 64)','rgb(255, 205, 86)','rgb(75, 192, 192)','rgb(54, 162, 235)'];
    for(var i=0;i<top5bg.length;i++){backgroundColor[i] = top5bg[i];}
    for(var i=0;i<top5bw.length;i++){borderColor[i] = top5bg[i];}
    new Chart(ctx,{
    'type':'horizontalBar',
    'data':{
    'labels':rankName,
    'datasets':[{'label':'本周有效分',
                'data':rankScore,
    			'fill':false,
    			'backgroundColor':backgroundColor,
    			'borderColor':borderColor,
    			'borderWidth':1
    			}
    			]
    			},
    
    'options':{'scales':{'xAxes':[{'ticks':{'beginAtZero':true}}]}}});
}


//add new feature,shake the element.2017年7月29日18:24:10 ---by dengwenbin
function shakeBaby() {
		function createLinkElement() {
			var e = document.createElement('link');
			e.setAttribute('type', 'text/css');
			e.setAttribute('rel', 'stylesheet');
			e.setAttribute('href', f);
			e.setAttribute('class', l);
			document.body.appendChild(e)
		}
		
		function removeElement() {
			var e = document.getElementsByClassName(l);
			for (var t = 0; t < e.length; t++) {
				document.body.removeChild(e[t])
			}
		}
		
		function createDivElement() {
			var e = document.createElement('div');
			e.setAttribute('class', a);
			document.body.appendChild(e);
			setTimeout(function() {
				document.body.removeChild(e)
			}, 100)
		}
		
		function getOffset(e) {
			return {
				height: e.offsetHeight,
				width: e.offsetWidth
			}
		}
		
		function comparePara(i) {
			var s = getOffset(i);
			return s.height > minHeight && s.height < maxHeight && s.width > minWidth && s.width < maxWidth
		}
		
		function changeOffset(e) {
			var t = e;
			var n = 0;
			while (!!t) {
				n += t.offsetTop;
				t = t.offsetParent
			}
			return n
		}
		
		function getInnerBrowserHeight() {
			var e = document.documentElement;
			if (!!window.innerWidth) {
				return window.innerHeight
			} else if (e && !isNaN(e.clientHeight)) {
				return e.clientHeight
			}
			return 0
		}
		
		function getBodyScrollPixel() {
			if (window.pageYOffset) {
				return window.pageYOffset
			}
			return Math.max(document.documentElement.scrollTop, document.body.scrollTop)
		}
		
		function shakeOffset(e) {
			var t = changeOffset(e);
			return t >= w && t <= b + w
		}
		
		function playDrumAudio() {
			var e = document.createElement('audio');
			e.setAttribute('class', l);
			e.src = audio;
			e.loop = false;
			e.addEventListener('canplay', function() {
				setTimeout(function() {
					addclass(k)
				}, 500);
				setTimeout(function() {
					replaceClassToShake();
					createDivElement();
					for (var e = 0; e < O.length; e++) {
						pushClassTo(O[e])
					}
				}, 15500)
			}, true);
			e.addEventListener('ended', function() {
				replaceClassToShake();
				removeElement()
			}, true);
			e.innerHTML = ' <p>If you are reading this, it is because your browser does not support the audio element. We recommend that you get a new browser.</p> <p>';
			document.body.appendChild(e);
			e.play()
		}
		
		function addclass(e) {
			e.className += ' ' + shakeClass + ' ' + o
		}
		
		function pushClassTo(e) {
			e.className += ' ' + shakeClass + ' ' + u[Math.floor(Math.random() * u.length)]
		}
		
		function replaceClassToShake() {
			var e = document.getElementsByClassName(shakeClass);
			var t = new RegExp('\\b' + shakeClass + '\\b');
			for (var n = 0; n < e.length;) {
				e[n].className = e[n].className.replace(t, '')
			}
		}
		
		var minHeight = 19;
		var minWidth = 30;
		var maxHeight = 420;
		var maxWidth = 420;
		var audio = '//10.61.16.223:8088/static/lib/harlem-shake.mp3';
		var shakeClass = 'mw-harlem_shake_me';
		var o = 'im_first';
		var u = ['im_drunk', 'im_baked', 'im_trippin', 'im_blown'];
		var a = 'mw-strobe_light';
		var f = '//10.61.16.223:8088/static/css/harlem-shake-style.css';
		var l = 'mw_added_css';
		var b = getInnerBrowserHeight();
		var w = getBodyScrollPixel();
		var allElem = document.getElementsByTagName('*');
		allElem = (allElem == undefined) ? (document.all) : (allElem);
		var k = null;
		for (var L = 0; L < allElem.length; L++) {
			var A = allElem[L];
			if (comparePara(A)) {
				if (shakeOffset(A)) {
					k = A;
					break
				}
			}
		}
		if (A === null) {
			console.warn('Could not find a node of the right size. Please try a different page.');
			return
		}
		createLinkElement();
		playDrumAudio();
		var O = [];
		for (var L = 0; L < allElem.length; L++) {
			var A = allElem[L];
			if (comparePara(A)) {
				O.push(A)
			}
		}
}









