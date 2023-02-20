public class Cell {
    private boolean isAlive;

    public Cell() {
        this.isAlive = false;
    }

    public Cell(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public String toString() {
        if (isAlive) {
            return "o";
        } else {
            return ".";
        }
    }
}
