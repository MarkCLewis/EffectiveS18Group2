/*
 * 	public Road[] split() {
 * 		(north1,north2) = splitLine(tl,tr,North)
 * 		(south1,south2) = splitLine(bl,br,South)
 * 		(west1,west2) = splitLine(tl,bl,West)
 * 		(east1,east2) = splitLine(tr,br,East)
 * 		activeness = false
 *
 * 		return Road[] {
 * 			new Road() {
 * 				north = north1
 * 				west = west1
 * 				east = findNumber(west1,east1)
 * 				south = findNumber(south1,north1)
 * 				tl = tl
 * 				tr = average of tl and tr
 * 				bl = average of tl and bl
 * 				br = center
 * 				activeness = true
 * 			}
 * 				new Road() {
 * 				north = north1
 * 				west = west1
 * 				east = findNumber(west1,east1)
 * 				south = findNumber(south1,north1)
 * 				tl = tl
 * 				tr = average of tl and tr
 * 				bl = average of tl and bl
 * 				br = center
 * 			}
 * 			...
 *
 * 		}
 * 		
 * 	}
 * 	public static void buildRoads() {
 * 		**split down to roads of sides equal to 1 if close by**
 * 		while(closeby and not at size 1) {
 * 			roadarray = road.split()
 * 			for(Road r: roadarray) {
 * 				WorldManager.getInstance().addEntity(r);
 * 			}
 * 		}
 * 	}
 */


/*
 * 	public List<Shape> getShapes() {
 * 		List<Point> pointlist = new ArrayList<>();
 * 		if(north == 1) {
 * 			pointlist.add(findPoint(tl,tr))
 *		}
 *		if(south == 1) {
 *			pointlist.add(findPoint(bl,br))
 *		}
 *		...
 *		
 * 	}
 */