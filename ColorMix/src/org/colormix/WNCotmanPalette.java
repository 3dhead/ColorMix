package org.colormix;

import java.awt.Color;

public enum WNCotmanPalette {
	LEMON_YELLOW("Lemon Yellow", new Color(255, 227, 0)),
	CADMIUM_YELLOW_PALE("Cadmium Yellow Pale", new Color(255,186,0)),
	CADMIUM_YELLOW("Cadmium Yellow", new Color(255,161,0)),
	CADMIUM_ORANGE("Cadmium Orange", new Color(253,111,42)),
	CADMIUM_RED_PALE("Cadmium Red Pale", new Color(227,40,16)),
	CADMIUM_RED_DEEP("Cadmium Red Deep", new Color(190,37,37)),
	ALIZARIN_CRIMSON("Alizarin Crimson", new Color(154,44,67)),
	PURPLE_LAKE("Purple Lake", new Color(68,34,85)),
	ULTRAMARINE("Ultramarine", new Color(65, 102, 245)),
	COBALT_BLUE("Cobalt Blue", new Color(33,104,201)),
	CERULEAN_BLUE("Cerulean Blue", new Color(16,117,167)),
	PHTHALO_BLUE("Phthalo Blue", new Color(0, 15, 137)),
	VIRIDIAN("Viridian", new Color(64,130,109)),
	EMERALD("Emerald", new Color(80,200,120)),
	HOOKERS_GREEN_DARK("Hookers Green Dark", new Color(73,121,107)),
	SAP_GREEN("Sap Green", new Color(80,125,42)),
	YELLOW_OCHRE("Yellow Ochre", new Color(204,119,34)),
	RAW_UMBER("Raw Umber", new Color(143,110,93)),
	BURNT_SIENNA("Burnt Sienna", new Color(145, 76, 54)),
	INDIAN_RED("Indian Red", new Color(205,92,92)),
	BURNT_UMBER("Burnt Umber", new Color(104,72,61)),
	PAYNES_GRAY("Paynes Gray", new Color(64, 64, 79)),
//	LAMP_BLACK("Lamp Black", new Color(42,41,45)),
//	CHINESE_WHITE("Chinese White", new Color(239,232,224))
	;
	
	private String name;

	private Color color;

	private WNCotmanPalette(String name, Color col) {
		this.name = name;
		this.color = col;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	
	
}
