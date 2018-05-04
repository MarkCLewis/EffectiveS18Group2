package shapes;

public class RenderMaterial {
	private RenderColor specular;
	private RenderColor diffuse;
	private RenderColor ambient;
	private float shininess;
	
	/**
	 * If true, then the material does per-vertex lighting.
	 * If false, then lighting is calculated per-pixel.
	 * Per-vertex lighting is more efficient.
	 */
	private boolean useVertexLighting;
	
	/**
	 * If true, then the material uses the alpha channels
	 * of its textures/colors.
	 * If false, the material is opaque.
	 * Default is false.
	 */
	private boolean useTransparency;
	
	private boolean useTexture;
	private String texDiffusePath;
	private String texNormalPath;
	
	/**
	 * Material holds all the important color and texture 
	 * information for the shape it is attached to.
	 * Note: textures are yet to be implemented
	 */
	public RenderMaterial() {
		// defaults
		this.specular = RenderColor.VeryDarkGrey;
		this.diffuse = RenderColor.VeryDarkGrey;
		this.ambient = RenderColor.Black;
		this.shininess = 0.01f;
		this.useVertexLighting = true;
		this.useTransparency = false;
		this.useTexture = false;
		this.texDiffusePath = "";
		this.texNormalPath = "";
	}
	
	/**
	 * Material holds all the important color and texture 
	 * information for the shape it is attached to.
	 * Note: textures are yet to be implemented
	 * @param specular the color of bright highlights due to light sources
	 * @param diffuse the color of smooth highlights due to light sources
	 * @param ambient the color of parts of the object that are not in the light
	 */
	public RenderMaterial(RenderColor specular, RenderColor diffuse, RenderColor ambient) {
		this.specular = specular;
		this.diffuse = diffuse;
		this.ambient = ambient;
		// defaults
		this.shininess = 0.01f;
		this.useVertexLighting = true;
		this.useTransparency = false;
		this.useTexture = false;
		this.texDiffusePath = "";
		this.texNormalPath = "";
	}
	
	/**
	 * Material holds all the important color and texture 
	 * information for the shape it is attached to.
	 * Note: textures are yet to be implemented
	 * @param specular the color of bright highlights due to light sources
	 * @param diffuse the color of smooth highlights due to light sources
	 * @param ambient the color of parts of the object that are not in the light
	 * @param vertexColor if true, material is rendered using per-vertex colors
	 * @param vertexLighting if true, material is rendered using per-vertex lighting
	 * @param transparency if true, material uses the alpha channel of its colors as its transparency.
	 * If false, the material is opaque.
	 */
	public RenderMaterial(
			RenderColor specular, 
			RenderColor diffuse, 
			RenderColor ambient,
			float shininess,
			boolean vertexColor,
			boolean vertexLighting,
			boolean transparency) {
		if(shininess < 0f || shininess > 128f) {
			throw new IllegalArgumentException();
		}
		this.specular = specular;
		this.diffuse = diffuse;
		this.ambient = ambient;
		this.shininess = shininess;
		this.useVertexLighting = vertexLighting;
		this.useTransparency = transparency;
		this.useTexture = false;
		this.texDiffusePath = "";
		this.texNormalPath = "";
	}
	
	public RenderMaterial(RenderMaterial that) {
		this.specular = that.getSpecularColor();
		this.diffuse = that.getDiffuseColor();
		this.ambient = that.getAmbientColor();
		this.shininess = that.getShininess();
		this.useVertexLighting = that.isUsingVertexLighting();
		this.useTransparency = that.isUsingTransparency();
		this.useTexture = that.useTexture;
		this.texDiffusePath = that.texDiffusePath;
		this.texNormalPath = that.texNormalPath;
	}
	
	public RenderColor getAmbientColor() {
		return new RenderColor(this.ambient);
	}
	
	public RenderColor getDiffuseColor() {
		return new RenderColor(this.diffuse);
	}
	
	public RenderColor getSpecularColor() {
		return new RenderColor(this.specular);
	}

	public float getShininess() {
		return this.shininess;
	}
	
	public String getTextureDiffusePath() {
		return this.texDiffusePath;
	}
	
	public String getTextureNormalPath() {
		return this.texNormalPath;
	}
	
	public void setAmbientColor(RenderColor ambient) {
		this.ambient = new RenderColor(ambient);
	}
	
	public void setDiffuseColor(RenderColor diffuse) {
		this.diffuse = new RenderColor(diffuse);
	}
	
	public void setSpecularColor(RenderColor specular) {
		this.specular = new RenderColor(specular);
	}
	
	public void setShininess(float shininess) {
		if(shininess < 0f || shininess > 128f) {
			throw new IllegalArgumentException();
		}
		this.shininess = shininess;
	}
	
	public void setTextureDiffusePath(String texDiffusePath) {
		this.texDiffusePath = texDiffusePath;
	}
	
	public void setTextureNormalPath(String texNormalPath) {
		this.texNormalPath = texNormalPath;
	}

	public boolean isUsingVertexLighting() {
		return useVertexLighting;
	}
	
	public boolean isUsingTransparency() {
		return useTransparency;
	}
	
	public boolean isUsingTexture() {
		return useTexture;
	}

	public void setUseVertexLighting(boolean useVertexLighting) {
		this.useVertexLighting = useVertexLighting;
	}
	
	public void setUseTransparency(boolean useTransparency) {
		this.useTransparency = useTransparency;
	}
	
	public void setUseTexture(boolean useTexture) {
		this.useTexture = useTexture;
	}
	
	public static RenderMaterial getRedMaterial() {
		RenderMaterial red = new RenderMaterial();
		red.setAmbientColor(RenderColor.Black);
		red.setDiffuseColor(RenderColor.Red);
		red.setSpecularColor(RenderColor.VeryDarkGrey);
		return red;
	}
	
	public static RenderMaterial getGreenMaterial() {
		RenderMaterial green = new RenderMaterial();
		green.setAmbientColor(RenderColor.Black);
		green.setDiffuseColor(RenderColor.Green);
		green.setSpecularColor(RenderColor.VeryDarkGrey);
		return green;
	}
	
	public static RenderMaterial getBlueMaterial() {
		RenderMaterial blue = new RenderMaterial();
		blue.setAmbientColor(RenderColor.Black);
		blue.setDiffuseColor(RenderColor.Blue);
		blue.setSpecularColor(RenderColor.VeryDarkGrey);
		return blue;
	}
	
	public static RenderMaterial getYellowMaterial() {
		RenderMaterial yellow = new RenderMaterial();
		yellow.setAmbientColor(RenderColor.Black);
		yellow.setDiffuseColor(RenderColor.Yellow);
		yellow.setSpecularColor(RenderColor.VeryDarkGrey);
		return yellow;
	}
}
