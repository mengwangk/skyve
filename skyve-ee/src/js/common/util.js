// SKYVE name space definition
SKYVE = {};

SKYVE.Util = function() {
	var context = window.location + '';
	context = context.substring(0, context.lastIndexOf("/") + 1)
	
	var wkt = null;

	// public methods
	return {
		customer: null,
		v: null,
		googleMapsV3ApiKey: null,
		ckEditorConfigFileUrl: null,
		CONTEXT_URL: context,
		
		loadJS: function(scriptPath, callback) {
		    var scriptNode = document.createElement('SCRIPT');
		    scriptNode.type = 'text/javascript';
		    scriptNode.src = scriptPath;

		    if (callback != null) {
			    if (scriptNode.readyState) { // IE, incl. IE9
			    	scriptNode.onreadystatechange = function() {
			    		if (scriptNode.readyState == "loaded" || scriptNode.readyState == "complete") {
			    			scriptNode.onreadystatechange = null;
			    			callback();
			    		}
			    	};
			    } 
			    else { // Other browsers
			    	scriptNode.onload = callback;
			    }
		    }
		    
		    var headNode = document.getElementsByTagName('HEAD');
		    if (headNode[0] != null) {
		        headNode[0].appendChild(scriptNode);
		    }
		},
		
		scatterGMap: function(display, // the display object that holds the map and other state variables
								data, // the response from the map servlet to scatter
								fit, // fit bounds
								auto) { // auto-update, only remove or update if changed
			// instantiate WKT if it hasn't been already (at this point Wkt script is loaded)
			if (! wkt) {
				wkt = new Wkt.Wkt()
			}
			
			// ensure that only 1 refresh at a time occurs
			// NB switch this off first thing in case there is an error in the code below
			display._refreshing = false;

			var items = data.items;
			
			if (auto) {
				// remove overlays not present in the data
				for (var bizId in display._objects) {
					if (! items.containsProperty('bizId', bizId)) { // this is SC API
						var deletedObject = display._objects[bizId];
						for (var i = 0, l = deletedObject.overlays.length; i < l; i++) {
							deletedObject.overlays[i].setMap(null);
							deletedObject.overlays[i] = null;
						}
						delete deletedObject['overlays'];
						delete display._objects[bizId];
					}
				}
			}
			else {
				// remove all overlays
				for (var bizId in display._objects) {
					var deletedObject = display._objects[bizId];
					for (var i = 0, l = deletedObject.overlays.length; i < l; i++) {
						deletedObject.overlays[i].setMap(null);
						deletedObject.overlays[i] = null;
					}
					delete deletedObject['overlays'];
					delete display._objects[bizId];
				}
			}

			// add/update overlays from the data
			for (var i = 0, l = items.length; i < l; i++) {
				var item = items[i];

				var object = display._objects[item.bizId];
				if (object) {
					// if the wkts have changed delete the overlay and recreate it
					var same = (object.overlays.length == item.features.length);
					if (same) {
						for (var j = 0, m = object.overlays.length; j < m; j++) {
							if (object.overlays[j].geometry !== item.features[j].geometry) {
								same = false;
								break;
							}
						}
					}
					if (! same) {
						for (var j = 0, m = object.overlays.length; j < m; j++) {
							object.overlays[j].setMap(null);
							object.overlays[j] = null;
						}
						delete object['overlays'];
						delete display._objects[bizId];
						object = null;
					}
				}
				if (object) {} else {
					object = {overlays: []};
					for (var j = 0, m = item.features.length; j < m; j++) {
						var feature = item.features[j];

						try { // Catch any malformed WKT strings
				        	wkt.read(feature.geometry);
				        }
				        catch (e) {
				            if (e.name === 'WKTError') {
				                alert(feature.geometry + ' is invalid WKT.');
				                continue;
				            }
				        }
				        var props = {editable: feature.editable};
				        if (feature.strokeColour) {
				        	props.strokeColor = feature.strokeColour;
				        }
				        if (feature.fillColour) {
				        	props.fillColor = feature.fillColour;
				        }
				        if (feature.fillOpacity) {
				        	props.fillOpacity = feature.fillOpacity;
				        }
				        if (feature.iconDynamicImageName) {
				        	props.icon = {url: 'image?_n=' + feature.iconDynamicImageName};
				        	if (feature.iconAnchorX && feature.iconAnchorY) {
				        		props.icon.anchor = new google.maps.Point(feature.iconAnchorX, feature.iconAnchorY);
				        		props.icon.origin = new google.maps.Point(0,0);
				        	}
				        }
				        
				        var overlay = wkt.toObject(props);
				        object.overlays.push(overlay);
	                	overlay.setMap(display.gmap);

	                	if (feature.zoomable) { // can show the info window for zooming
		                	overlay.bizId = item.bizId;
		                	overlay.geometry = feature.geometry;
				            overlay.fromTimestamp = item.fromTimestamp;
				            overlay.toTimestamp = item.toTimestamp;
				            overlay.mod = item.moduleName;
				            overlay.doc = item.documentName;
				            overlay.infoMarkup = item.infoMarkup;
					        google.maps.event.addListener(overlay, 'click', function(event) {
					        	display.click(this, event);
					        });
				        }
					}
			        
//			        if (Wkt.isArray(overlay)) { // Distinguish multigeometries (Arrays) from objects
//			        	for (i in obj) {
//			                if (obj.hasOwnProperty(i) && ! Wkt.isArray(obj[i])) {
//			                	obj[i].bizId = datum.bizId;
//			                	obj[i].setMap(display.gmap);
//								display._objects[obj[i].bizId] = obj[i];
//			                }
//			            }
//			        }
//			        else {
//			            obj.setMap(display.gmap); // Add it to the map
//			            this._objects.push(obj);
//			        }
//
//					overlay = new google.maps.Marker({
//						bizId: datum.bizId,
//						position: latlng,
//			            map: display.gmap
//			        });
					display._objects[item.bizId] = object;
				}
			}

			if (fit) {
				var bounds = new google.maps.LatLngBounds();
				var someOverlays = false;
				for (var id in display._objects) {
					someOverlays = true;
					var object = display._objects[id];
					var overlays = object.overlays;
					for (var i = 0, l = overlays.length; i < l; i++) {
						var overlay = overlays[i];
			            if (overlay.getPath) {
				            // For Polygons and Polylines - fit the bounds to the vertices
							var path = overlay.getPath();
							for (var j = 0, m = path.getLength(); j < m; j++) {
								bounds.extend(path.getAt(j));
							}
			            }
			            else if (overlay.getPosition) {
			            	bounds.extend(overlay.getPosition());
			            }
					}
				}

				if (someOverlays) {
					// Don't zoom in too far on only one marker
				    if (bounds.getNorthEast().equals(bounds.getSouthWest())) {
				       var extendPoint1 = new google.maps.LatLng(bounds.getNorthEast().lat() + 0.01, bounds.getNorthEast().lng() + 0.01);
				       var extendPoint2 = new google.maps.LatLng(bounds.getNorthEast().lat() - 0.01, bounds.getNorthEast().lng() - 0.01);
				       bounds.extend(extendPoint1);
				       bounds.extend(extendPoint2);
				    }

					display.gmap.fitBounds(bounds);
				}
			}
		}
	}
}();