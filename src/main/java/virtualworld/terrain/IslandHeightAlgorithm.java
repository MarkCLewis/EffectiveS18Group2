package virtualworld.terrain;


//Assumes that the center is (0,0)

public class IslandHeightAlgorithm extends TerrainHeightAlgorithm {
	
	double size;
	Point center;
	private final TerrainHeightAlgorithm ta;
	
	IslandHeightAlgorithm(Point c, double length, TerrainHeightAlgorithm t) {
		size = length;
		center = c;
		ta = t;
		noise = Perlin.getInstance();
	}
	
	@Override
	public double generateHeight(double x, double z) {
		double e = ta.generateSubValue(ta.heightSeed, ta.frequency, x, z);
		for (int i = 1; i < ta.numOctaves; i++) {
			e += ta.generateSubValue(ta.heightSeed / (ta.octaveScale*i), ta.frequency * ((ta.octaveScale*i)/2), x, z);
		}
		double d = euclideanDistance((x+.5), (z+.5));
		//System.out.println(d);
		return ((e + .3) * (1 - (.85*Math.pow(d, .95)))) * ta.baseHeight;
	}
	
	private double manhattanDistance(double x, double z) {
		return (2*(Math.max(Math.abs(x), Math.abs(z))));
		
	}
	
	private double euclideanDistance(double x, double z) {
		return (2*(Math.sqrt(x * x + z*z)));
	}
	

}
