package multithread;

import java.util.concurrent.CountDownLatch;

public class MessageHandlerRunnable implements Runnable {
  private MessageHandler msg;
  private CountDownLatch latch;

  public MessageHandlerRunnable(MessageHandler msg, CountDownLatch latch) {
    this.msg = msg;
    this.latch = latch;
  }

  public void run() {
    while(!msg.isFinished()) {
      if (msg.isAwake()) {
        msg.run();
      }
    }
    latch.countDown();
  }
}