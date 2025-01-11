package com.projet.model;

public class Forest extends Tile{
	private int productionRessources =3;

    /**
     * @name Constructor with all parameters
     * 
     * @param x : position x of the forest
     * @param y : position y of the forest
     * @param productionResources : production of ressource of the forest
     */
    public Forest(int x, int y,int productionRessources) {
    	super(x,y,TileType.FOREST,"forest.png");
        this.productionRessources = productionRessources;
    }
    /**
     * 
     * @name Constructor with position as parameter and default value for attribute productionRessources
     * 
     * @param x : position x of the forest
     * @param y : position y of the forest
     * @default productionRessources  3 : production of ressource of the forest 
     */
    public Forest(int x, int y) {
    	super(x,y,TileType.FOREST,"forest.png");
    }


    /*
     *  Getter
     */
    public int getProductionRessources() {
        return productionRessources;
    }

    /*
     *  Setter
     */
    public void setProductionResources(int productionRessources) {
        this.productionRessources = productionRessources;
    }
    
    /*
     * methods
     */
    
    /**
     * collect ressources from the forest (remove them)
     * 
     * @return the amount of ressources collected
     */
    public int collectRessources() {
    	int ret = productionRessources;
    	productionRessources=0;
    	setImage("plain.png");
    	return ret;
    }
    
}
