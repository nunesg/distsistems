package multithread;

public class MessageHandler {
  private StringBuilder msg;
  private int id;
  private boolean awaken;
  private boolean finished;
  private MessageHandler next;

  public MessageHandler(StringBuilder msg, int id) {
    this.msg = msg;
    this.id = id;
    this.next = null;
    awaken = false;
    finished = false;
  }

  public void setNext(MessageHandler next) {
    this.next = next;
  }

  public synchronized void awake() {
    awaken = true;
  }

  public synchronized boolean isAwake() {
    return awaken;
  }

  public boolean isFinished() {
    return finished;
  }

  public synchronized void run() {
    if (checkIfFinished()) {
      finish();
      awaken = false;
      next.awake();
      return;
    }

    for (int i=0; i<msg.length(); i++) {
      if (Character.isLowerCase(msg.charAt(i))) {
        msg.setCharAt(i, Character.toUpperCase(msg.charAt(i)));
        printMessage();
        if (checkIfFinished()) {
          finish();
        }
        awaken = false;
        try{
          Thread.sleep(500);
        } catch (Exception e) {
          System.out.println("Error sleeping.");
        }
        next.awake();
        return;
      }
    }
  }

  private boolean checkIfFinished() {
    for (int i = 0; i < msg.length(); i++) {
      if (Character.isLowerCase(msg.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private void finish() {
    finished = true;
    System.out.println("Thread " + id + " finished editing message: " + msg.toString());
  }

  private void printMessage() {
    System.out.println("Thread " + id + " just edited message: " + msg.toString());
  }
}