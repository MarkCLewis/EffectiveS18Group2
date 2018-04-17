package shapes;

public class Material {
	private Color specular;
	private Color diffuse;
	private Color ambient;
	
	/**
	 * If true, then material is low quality.
	 * If false, then material is high quality.
	 */
	private boolean lowQuality;
	
	/**
	 * If true, then material uses vertex-centered colors.
	 * If false, then colors are rendered per-pixel.
	 * Vertex colors are less expensive for renderer.
	 */
	private boolean useVertexColor;
	
	/**
	 * If true, then the material does per-vertex lighting.
	 * If false, then lighting is calculated per-pixel.
	 * Per-vertex lighting is more efficient.
	 */
	private boolean vertexLighting;
	
	/**
	 * Material holds all the important color and texture 
	 * information for the shape it is attached to.
	 * Note: textures are yet to be implemented
	 * @param specular the color of bright highlights due to light sources
	 * @param diffuse the color of smooth highlights due to light sources
	 * @param ambient the color of parts of the object that are not in the light
	 */
	public Material(Color specular, Color diffuse, Color ambient) {
		this.specular = specular;
		this.diffuse = diffuse;
		this.ambient = ambient;
	}
	
	public Color getAmbientColor() {
		return new Color(this.ambient);
	}
	
	public Color getDiffuseColor() {
		return new Color(this.diffuse);
	}
	
	public Color getSpecularColor() {
		return new Color(this.specular);
	}
	
	public void setAmbientColor(Color ambient) {
		this.ambient = new Color(ambient);
	}
	
	public void setDiffuseColor(Color diffuse) {
		this.diffuse = new Color(diffuse);
	}
	
	public void setSpecularColor(Color specular) {
		this.specular = new Color(specular);
	}
	
	public boolean isLowQuality() {
		return lowQuality;
	}
	
	public boolean isUsingVertexColor() {
		return useVertexColor;
	}
	
	public boolean isUsingVertexLighting() {
		return vertexLighting;
	}
	
	public void setLowQuality() {
		lowQuality = true;
	}
	
	public void setHighQuality() {
		lowQuality = false;
	}
	
	public void setUseVertexColor() {
		useVertexColor = true;
	}
	
	public void setUsePixelColor() {
		useVertexColor = false;
	}
	
	public void setUseVertexLighting() {
		vertexLighting = true;
	}
	
	public void setUsePixelLighting() {
		vertexLighting = false;
	}
}
