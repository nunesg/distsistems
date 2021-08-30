package multithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

public class Main {
  public static void main(String args[]) {
    int n = 30;
    StringBuilder str = new StringBuilder("CkIsdYCOHhDmFLaBvSyzogTKvDPRPNJwnKtHRSokFtNeFdigDEPAmcsOdCnnSZOGugWXamtcMaKkRnFE");
    CountDownLatch latch = new CountDownLatch(n);
    ExecutorService es = Executors.newFixedThreadPool(n);
    MessageHandler handlers[] = new MessageHandler[n];
    for (int i=0; i<n; i++) {
      handlers[i] = new MessageHandler(str, i);
      es.execute(new MessageHandlerRunnable(handlers[i], latch));
      if (i>0) {
        handlers[i-1].setNext(handlers[i]);
      }
    }
    handlers[n-1].setNext(handlers[0]);
    handlers[0].awake(); // start

    try{
      latch.await();
      es.shutdown();
    } catch (Exception e) {
      System.out.println("Error waiting threads. e: " + e.toString());
    }

    System.out.println("\nResulting message: " + str.toString());
  }
}