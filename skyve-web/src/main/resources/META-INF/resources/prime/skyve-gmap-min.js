SKYVE.BizMap=function(){var a={};var c=new Wkt.Wkt();var b=function(g,d,h,f){if(g._refreshing){return}g._refreshing=true;var e="";if(f){c.fromObject(f.getNorthEast());e="&_ne="+c.write();c.fromObject(f.getSouthWest());e+="&_sw="+c.write()}$.get(g.url+e,function(i){try{SKYVE.GMap.scatter(g,i,d,h)}finally{g._refreshing=false}})};return{create:function(f){var e={zoom:1,center:new google.maps.LatLng(0,0),mapTypeId:google.maps.MapTypeId.ROADMAP};var g=a[f.elementId];if(g){if(g.webmap){e.zoom=g.webmap.getZoom();e.center=g.webmap.getCenter();e.mapTypeId=g.webmap.getMapTypeId()}}else{g={_objects:{},_overlays:[],_refreshing:false,click:function(h,i){SKYVE.BizMap.click(this,h,i)}};a[f.elementId]=g}g.infoWindow=new google.maps.InfoWindow({content:""});g.webmap=new google.maps.Map(SKYVE.PF.getByIdEndsWith(f.elementId)[0],e);if(f.loading==="lazy"){google.maps.event.addListener(g.webmap,"zoom_changed",function(){if(!g._refreshing){b(g,false,false,this.getBounds())}});google.maps.event.addListener(g.webmap,"dragend",function(){b(g,false,false,this.getBounds())})}var d=SKYVE.Util.CONTEXT_URL+"map?";if(f.modelName){d+="_c="+f._c+"&_m="+f.modelName}else{if(f.queryName){d+="_mod="+f.moduleName+"&_q="+f.queryName+"&_geo="+f.geometryBinding}}g.url=d;b(g,true,false);return g},get:function(d){return a[d]},click:function(l,j,e){var g=j.infoMarkup;g+='<br/><br/><input type="button" value="Zoom" onclick="window.location=\''+SKYVE.Util.CONTEXT_URL;g+="?m="+j.mod+"&d="+j.doc+"&i="+j.bizId+"'\"/>";if(j.getPosition){l.infoWindow.open(this.webmap,j);l.infoWindow.setContent(g)}else{if(j.getPath){var d=new google.maps.LatLngBounds();var o=j.getPath();for(var i=0,f=o.getLength();i<f;i++){d.extend(o.getAt(i))}var h=d.getNorthEast();var m=d.getSouthWest();l.infoWindow.setPosition(e.latLng);l.infoWindow.open(l.webmap);l.infoWindow.setContent(g)}}}}}();SKYVE.BizMapPicker=function(){var a={};var b=new Wkt.Wkt();return{create:function(e){var d={zoom:SKYVE.Util.mapZoom,center:SKYVE.GMap.centre(),mapTypeId:google.maps.MapTypeId.ROADMAP,mapTypeControlOptions:{style:google.maps.MapTypeControlStyle.DROPDOWN_MENU}};var f=a[e.elementId];if(f){if(f.webmap){d.zoom=f.webmap.getZoom();d.center=f.webmap.getCenter();d.mapTypeId=f.webmap.getMapTypeId()}}else{f={_objects:{},_overlays:[],setFieldValue:function(g){SKYVE.PF.setTextValue(c+"_value",g)}};a[e.elementId]=f}var c=SKYVE.PF.getByIdEndsWith(e.elementId).attr("id");f.webmap=new google.maps.Map(document.getElementById(c),d);if(!e.disabled){SKYVE.GMap.drawingTools(f);SKYVE.GMap.geoLocator(f)}SKYVE.GMap.clear(f);SKYVE.GMap.scatterValue(f,SKYVE.PF.getTextValue(c+"_value"))}}}();