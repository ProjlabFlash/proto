package main;

public enum Color {
	BLUE("BLUE"), RED("RED"), GREEN("GREEN"), YELLOW("YELLOW"),
	TEAL("TEAL"), PURPLE("PURPLE"), GREY("GREY"), ORANGE("ORANGE");
	
	private String name;
	
	Color(String text) {
	    this.name = text;
	  }
	
	public static Color fromString(String text) {
	    for (Color c : Color.values()) {
	      if (c.name.equalsIgnoreCase(text)) {
	        return c;
	      }
	    }
	    return null;
	  }
}
