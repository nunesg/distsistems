package cloudnotes.server;

import java.math.BigInteger;

public class IdManager {
  private BigInteger nextId;

  IdManager() {
    nextId = new BigInteger("1");
  }

  public String create() {
    String id = nextId.toString();
    nextId = nextId.add(BigInteger.ONE);
    return id;
  }

  public boolean has(String id) {
    BigInteger big = new BigInteger(id);
    return big.compareTo(nextId) < 0 && big.compareTo(BigInteger.ZERO) > 0;
  }
}