package JUC.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    public static void main(String[] args) {
        System.out.printf("main=>%s\n",Thread.currentThread().getName());
        CompletableFuture.supplyAsync(()->{
            System.out.printf("supplyAsync=>%s\n",Thread.currentThread().getName());
            return "Hello";
        }).thenApply(s->{
            System.out.printf("thenApply=>%s\n",Thread.currentThread().getName());
            return s+" world";
        }).thenAccept(s->{
            System.out.printf("thenAccept=>%s\n",Thread.currentThread().getName());
            System.out.println(s);
        });
        System.out.println("----------------------------------------------------------");
        CompletableFuture.supplyAsync(()->{
            System.out.printf("supplyAsync=>%s\n",Thread.currentThread().getName());
            return "Hello";
        }).thenApplyAsync(s->{
            System.out.printf("thenApplyAsync=>%s\n",Thread.currentThread().getName());
            return s+" world";
        }).thenAcceptAsync(s->{
            System.out.printf("thenAcceptAsync=>%s\n",Thread.currentThread().getName());
            System.out.println(s);
        });
        CompletableFuture.allOf();
    }
}
