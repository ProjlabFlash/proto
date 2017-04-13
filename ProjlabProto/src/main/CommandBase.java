package main;

public abstract class CommandBase {
	
	public final String cmdName;
	
	public CommandBase (String cmdName) {
		this.cmdName = cmdName;
	}
	
	public abstract void execute(String[] params);
	
	public static class CmdAddRail extends CommandBase{

		public CmdAddRail() {
			super("add rail");
		}

		@Override
		public void execute(String[] params) {
			System.out.println("yey");
			
		}
	}
}