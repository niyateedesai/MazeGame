public class Maze{
	String[][] grid;

	public Maze( String[][] grid){
		this.grid = grid;
	}

	public boolean isWallAt(int r, int c){
		if(grid[r][c].equals("*")){
			return true;
		}
		return false;
	}
	
	public boolean isWallAhead(Explorer niyatee){
		if(niyatee.getDirection() == 0 || niyatee.getDirection() == 360){
			if(niyatee.getLoc().getRow()+1 != -1){
				if(!grid[niyatee.getLoc().getRow()-1][niyatee.getLoc().getCol()].equals("*")){
					return false;
				}
			}
		}
		else if(niyatee.getDirection() == 90){
			if(niyatee.getLoc().getCol()-1 != grid[0].length){
				if(!grid[niyatee.getLoc().getRow()][niyatee.getLoc().getCol()+1].equals("*")){
					return false;
				}
			}
		}
		else if(niyatee.getDirection() == 180){
			if(niyatee.getLoc().getRow()+1 != grid.length){
				if(!grid[niyatee.getLoc().getRow()+1][niyatee.getLoc().getCol()].equals("*")){
					return false;
				}
			}
		}
		else if(niyatee.getDirection() == 270){
			if(niyatee.getLoc().getCol()-1 != -1){
				if(!grid[niyatee.getLoc().getRow()][niyatee.getLoc().getCol()-1].equals("*")){
					return false;
				}
			}
		}
		return true;
	}

}