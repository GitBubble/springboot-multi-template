function customDropDown(elem){
  //change click value 
   this.section = elem;
   this.display = elem.find('#forceId');
   this.value = '';
   this.index = -1;   
   this.options = elem.find('ul.dropdown-menu>li');
   this.initEvents();
   this.initDefaultValue();
}

customDropDown.prototype = {
   initEvents:function(){
         var obj = this;
         obj.section.on('click',function(event){$(this).toggleClass('active');});
         
         obj.options.on('click',function(){
             var opt = $(this);
	         var text = opt.find('a').text();
	         obj.display.text(text);
	         obj.display.val = opt.attr('value');
	         obj.value = text;
	         obj.display.index = opt.index();
	         });
	         
         obj.display.on('mouseover',function(){
              var pannel = $(this);
              if(pannel.text() != pannel.attr('title'))
              {
                 //button.curText = button.innerText;
  	            pannel.text(pannel.attr('title'));
              }
         	});
	      
         obj.display.on('mouseout',function(){
              var pannel = $(this);
              if (obj.getCurrentText())
	      	   pannel.text(obj.getCurrentText());
	      	else
	      	   pannel.text(pannel.attr('title'));
	      
         });
   },
   initDefaultValue:function(){
	   
   },
   //get
   getCurrentText:function(){
         return this.value;
   },
   //get
   getPostPara:function(){
         if(typeof this.display.val == 'function'){ 
		     this.display.val = 0;
			 if( this.display.attr('title') === '>>选秀方式<<')
				 this.display.val = 'personalScore';
		 }	 		 
		 return this.display.val;	    
    }
};

//$(document).ready(function(){
  var forcedropdown = new customDropDown($('#forceSection'));
  var divdropdown = new customDropDown($('#DivisionSection'));
  var scopedropdown = new customDropDown($('#ScopeSection'));
  var rankropdown = new customDropDown($('#RankSection')); 
//)

  
   var module = {
      
	  main: 'oooops..crashed',
	  topRange: 5,
	  slogan:["运动全能","加班最少","帅哥最多","颜值最高","代码最好","MR速度最快","ISSUE质量最高"],
	  shuffleSlogan:function(){
		 return module.slogan[getRandomIntInclusive(0,module.slogan.length-1)];  
	  },
	  init: function(){
	       main = $('#loadingPart').html();
		   
		   $('#Run').on('click',function(content){
		         if(!module.checkPara()) return;
                 //loading waiting circles
				 $('#loadingPart').html("<p>数据加载中...</p><p><img id='loader-img' alt='loading....' src='../static/img/Ripple.gif'/></p>");
				 module.clearBattleField();				 
				 module.getModuleInfo(); // re-generate all group rank data
				 module.setTimeSpan();   // refresh time span of all statistics...
				   
          	  });
		  
		  this.initDraw();

	  },
	  clearBattleField:function(){
		  $('.section').remove(); // remove all group ranks
		  var chart = $(".col-xs-6");
		  for(var i=1;i<chart.length;i++){
			 $(chart[i]).remove();
		  }
	  },
	  setTimeSpan:function(){
		  var scopeDate = scopedropdown.getPostPara();
		  var days = [7,14,30,60,180,365];//Arthur.deng: roughly calculated days,not consideration of calendar details.
		  //exeception
		  if(scopeDate > days.size) {console.log('illegal days,make sure value of <li> in the range of days[]')}
		  var cur = new Date();
		  var last = new Date();
		  last.setDate(last.getDate()-days[scopeDate-1]);
		  $('#dateSpan').text(cur.toLocaleDateString() + '~' + last.toLocaleDateString());
	  },
	  checkPara: function(){
	  	if( !forcedropdown.getPostPara() || !divdropdown.getPostPara()
		     || !scopedropdown.getPostPara() || !rankropdown.getPostPara())
		   {
		      alert('小伙子，小姑娘们。\n 动动手勾勾左边的小框框~');
			  return false;
		   }
		   else
		   {
		      return true;
		   }
	  },
	  getPara:function(){    	
		return '?id='+forcedropdown.getPostPara() +'&'+'dept='+divdropdown.getPostPara()+'&'+'cycle='+scopedropdown.getPostPara()
				    +'&'+'sort='+rankropdown.getPostPara();
	  },
	  getAppendTr:function(response){
	    var topRange = [];
		topRange.push('<tr>');
		topRange.push('<td>' + response.employNum + '</td>');
        topRange.push('<td>' + response.name + '</td>');
        topRange.push('<td>' + response.mergedMrCount + '</td>');
        topRange.push('<td>' + response.resposbilityIssueCountBySelf + '</td>');
        topRange.push('<td>' + response.noteTotalOtherCount + '</td>');
        topRange.push('<td>' + response.mrCommonReplyCount + '</td>');
        topRange.push('<td>' + response.handleMRCount + '</td>');
        topRange.push('<td>' + response.effectiveIssueCount + '</td>');
        topRange.push('<td>' + response.issueTotalCount + '</td>');
        topRange.push('<td>' + response.effectiveIssueCountBySelf + '</td>');
        topRange.push('<td>' + response.closeIssueCount + '</td>');
        topRange.push('<td>' + response.noteTotalCount + '</td>');
		 topRange.push('<td>' + response.personalScore + '</td>');
		topRange.push('</tr>');	       
		return topRange.join('');
	  },
	  
	  getTopRange: function(response){
	        var top = '';
	     	for(var i=0;i<module.topRange;i++){
				var rows = this.getAppendTr(response[i]);
				top += rows.replace('<tr>',"<tr class='top5'>").replace(response[i].employNum,i+1);	
			}
            return 	top;	
	  },
	  
	  getGroupRanks:function(response){
	        var groupRankAll = new Map();
	        for(var i=0;i<response.length;i++){
			     if(!response[i].group){
				    continue;
				 }
				 else{
				    if( undefined === groupRankAll.get(response[i].group)){
					      groupRankAll.set(response[i].group,'');    
					}
					
					var rows = groupRankAll.get(response[i].group) + this.getAppendTr(response[i]);
					groupRankAll.set(response[i].group,rows);
				 }
			}
			return groupRankAll;
	  },
	  
	  getModuleInfo: function(born){
	    	var initReq = "http://10.61.16.223:8088/query?id=%25E8%25B7%25AF%25E7%2594%25B1%25E5%2599%25A8%25E4%25B8%258E%25E7%2594%25B5%25E4%25BF%25A1%25E4%25BB%25A5%25E5%25A4%25AA%25E5%25BC%2580%25E5%258F%2591%25E7%25AE%25A1%25E7%2590%2586%25E9%2583%25A8-%25E6%25B5%2581%25E6%258E%25A7%25E8%25BD%25AF%25E4%25BB%25B6%25E5%25BC%2580%25E5%258F%2591%25E9%2583%25A8-%25E6%25A8%25A1%25E5%259E%258B%25E7%25AE%2597%25E6%25B3%2595%25E5%25BC%2580%25E5%258F%2591%25E9%2583%25A8-%25E6%25B7%25B1%25E5%259C%25B3&dept=1&cycle=1&sort=personalScore";
			
			if(born != undefined){
				reqUrl = initReq;
			}else{
				reqUrl = encodeURI(encodeURI('http://10.61.16.223:8088/query' + module.getPara()));
			}
			
			
	        $.ajax({
              type: 'GET',
              dataType: 'json',
              url: reqUrl,
              //data: { id: id, name: name ,
              success: function(response) {
			      //console.log(response);
                  $('#loadingPart').html(main);
				  module.clearBattleField();			
                  var content = module.getGroupRanks(response);
				  var groupNum = response.length;
				  //var id = 0;
				  var top = module.getTopRange(response);
				  //for(var i=0;i<groupNum;++i){
				  //	  module.createSection();
				  //}
				  //var title = $('.section').find('.sub-header');
				  //var groupData = $('.section').find('.rank');
				  /*
                  $.each(content, function(key,value) {
                      title[id].text(key);
                     groupData[id].append(top +value);
                    }					 
                  );*/

				  for(var [key,value] of content.entries()){
					 var title = key+'  + 作战范围前' + module.topRange + '位';
					 module.createSection(key,title);
					 $("tbody#"+key).append(top +value);
					 module.createChart(key,module.shuffleSlogan());
					 module.draw($("canvas#"+key),$("tbody#"+key));
					 //id++;
				  }
				  // project draw runs after each group data was drawed in canvas
				  //module.updateProjectTable();
				  module.drawProject();
              },
              error: function(response) {
			      $('#loadingPart').html(main);
				  module.initDraw();               
              }
            });
	  },
	  
	  draw:function(ctx,src){
		    var condition = rankropdown.getPostPara();
            drawChart(ctx,src,condition);
	  },
	  
	  updateProjectTable:function(){
		  //clear project Table
		  //$("")
		  var groups = $(".rank").first().siblings();
		  //update table
		    //sum statistics
			//append
	  },
	  drawProject:function(){
		   drawChart($('#project'),$('#prjRank'),null);
	  },
	  //only for initial page 
	  initDraw:function(){
		   module.draw($("#qos"),$("#qosRank"));
		   module.draw($("#cxb"),$("#cxbRank"));
		   module.draw($("#frm"),$("#frmRank"));
		   
		   module.drawProject();
	  },
	  createSection:function(key,title){
		  var section ="<div class='section'> <h2 class='sub-header'>"+title+"</h2><div class='table-responsive'><table class='table table-striped'><thead><tr><th>排名</th><th>姓名</th><th>合入MR</th><th>提他ISSUE</th><th>评论别人MR</th><th>MR评论数</th><th>处理MR数</th><th>有效ISSUE</th><th>全部ISSUE</th><th>自提ISSUE</th><th>闭环ISSUE</th><th>评论自己MR</th><th>流控计算分</th></tr></thead><tbody id='"+key+"' class='rank'></tbody></table></div></div>";
		  $("#loadingPart").append(section);
	  },
	  
	  createChart: function(id,slogan){
	  var chart ="<div class='col-xs-6 col-sm-3 placeholder'> <canvas id='"+id+"' width='200' height='200'></canvas> <h4>"+id+"</h4><span class='text-muted' >"+slogan+"</span></div>";
	  $("#chart").append(chart);
	  },
	  
	  loadingDefault:function(){
		   this.init();
		   $('#loadingPart').html("<p>数据加载中...</p><p><img id='loader-img' alt='loading....' src='../static/img/Ripple.gif'/></p>");
		   this.getModuleInfo(true);
		   this.initDraw();
	  }
   };
  
   module.loadingDefault();
