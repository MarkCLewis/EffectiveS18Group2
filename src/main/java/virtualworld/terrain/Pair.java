package virtualworld.terrain;

public class Pair<L,R> {
	
  private final L left;
  private final R right;

  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public L getLeft() { return left; }
  public R getRight() { return right; }


  public int hashCode() { return left.hashCode() ^ right.hashCode(); }


  public boolean equals(Pair<?,?> o) {
    return this.left.equals(o.getLeft()) &&
           this.right.equals(o.getRight());
  }

}
