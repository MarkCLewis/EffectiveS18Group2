package virtualworld;

public class Game {
	/* 
	 * This class is a singleton
	 * No public constructors are provided
	 * 
	 */

	public static final String title = "VirtualWorld";
	
	private static class GameLoader {
		private static final Game INSTANCE;
		static {
			try {
				INSTANCE = new Game();
			} catch (Exception e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}
	
	private Game() {
		if(GameLoader.INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}
		else {
			this.init();
		}
	}
	
	public static Game getInstance() {
		return GameLoader.INSTANCE;
	}

    private void init() {
    	/* Initialize the RenderEngine implicitly */
        RenderEngine.getInstance();
    }
    
    public void run() {
        RenderEngine.getInstance().run();
    }

}
