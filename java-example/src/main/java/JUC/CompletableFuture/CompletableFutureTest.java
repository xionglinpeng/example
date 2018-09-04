package JUC.CompletableFuture;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class CompletableFutureTest {

    private static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**然后应用*/
    public static void thenApply(){
        CompletableFuture<String> A = CompletableFuture.completedFuture("Hello A!");
        CompletableFuture<String> B = A.thenApply(a->{
            System.out.printf("%s (daemon:%s) : 我是B，我应用了A。\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
            return "Hello B!";
        });
    }

    /**然后运行*/
    public static void thenRun(){
        CompletableFuture<Void> A = CompletableFuture.runAsync(()->{
            sleep(1000);
            System.out.printf("%s (daemon:%s) : 我是A，我执行完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });
        A.thenRun(()->{
            System.out.printf("%s (daemon:%s) : 我是B，在A之后我运行了。\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });
        System.out.println("你好，我是main。");
        sleep(5000);
    }

    /**运行后*/
    public static void runAfterBoth(){

        CompletableFuture<Void> A = CompletableFuture.runAsync(()->{
            sleep(1000);
            System.out.printf("%s (daemon:%s) : 我是A，我执行完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });

        CompletableFuture<Void> B = CompletableFuture.runAsync(()->{
            sleep(2000);
            System.out.printf("%s (daemon:%s) : 我是B，我执行完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });

        Runnable R = ()->{
            System.out.printf("%s (daemon:%s) : 我是R，到我执行了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        };

        A.runAfterBoth(B,R);

        System.out.println("你好，我是main。");
        sleep(5000);
    }

    /**运行后*/
    public static void runAfterEither(){

        CompletableFuture<Void> A = CompletableFuture.runAsync(()->{
            sleep(1000);
            System.out.printf("%s (daemon:%s) : 我是A，我执行完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });

        CompletableFuture<Void> B = CompletableFuture.runAsync(()->{
            sleep(2000);
            System.out.printf("%s (daemon:%s) : 我是B，我执行完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });

        Runnable R = ()->{
            System.out.printf("%s (daemon:%s) : 我是R，到我执行了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        };

        A.runAfterEither(B,R);

        System.out.println("你好，我是main。");
        sleep(5000);
    }

    /**然后接受*/
    public static void thenAccept(){
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->"Hello World!");
        A.thenAccept(s->{
            System.out.printf("消费值：%s\n",s);
            System.out.printf("%s (daemon:%s) : 我消费完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });
    }

    /**然后接受*/
    public static void thenAcceptBoth(){
        CompletableFuture<String> A = CompletableFuture.completedFuture("Hello A!");
        CompletableFuture<String> B = CompletableFuture.completedFuture("Hello B!");

        A.thenAcceptBothAsync(B,(a,b)->{
            System.out.printf("消费值：%s , %s\n",a,b);
            System.out.printf("%s (daemon:%s) : 我消费完了\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });
        System.out.println("你好，我是main。");
        sleep(2000);
    }

    /**当完成*/
    public static void whenComplete(){
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
//            throw new RuntimeException("出错了");
            return "Hello World!";
        });
        A.whenComplete((value,throwable)->{
            System.out.printf("结果值：a = %s\n结果值：b = %s\n",value,throwable);
            System.out.printf("%s (daemon:%s) : 当完成。\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        });
    }

    /**处理*/
    public static void handle(){
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
            throw new RuntimeException("出错了");
//            return "Hello World!";
        });
        A.handle((value,throwable)->{
            System.out.printf("结果值：a = %s\n结果值：b = %s\n",value,throwable);
            System.out.printf("%s (daemon:%s) : 处理。\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
            if (Objects.nonNull(throwable)) {
                return "Hello World!";
            }
            return value;
        });
    }

    /**要么接受*/
    public static void acceptEither(){
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
            sleep(2000);
            System.out.println("我是A...");
            return "A";
        });
        CompletableFuture<String> B = CompletableFuture.supplyAsync(()->{
            sleep(1000);
            System.out.println("我是B...");
            return "B";
        });
        A.acceptEither(B,s -> {
            System.out.printf("%s运行的更快。",s);
        }).join();
    }

    /**既适用于*/
    public static void applyToEither(){
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
            sleep(2000);
            System.out.println("我是A...");
            return "A";
        });
        CompletableFuture<String> B = CompletableFuture.supplyAsync(()->{
            sleep(1000);
            System.out.println("我是B...");
            return "B";
        });
        String result = A.applyToEither(B,s -> {
            System.out.printf("%s运行的更快。\n",s);
            return s+" == "+s;
        }).join();

        System.out.println("result = " + result);
    }


    public static void thenCombine() {
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
            sleep(2000);
            System.out.println("我是A...");
            return "A";
        });
        CompletableFuture<String> B = CompletableFuture.supplyAsync(()->{
            sleep(1000);
            System.out.println("我是B...");
            return "B";
        });

        String result = A.thenCombine(B,(a,b)->{
            System.out.println("消费A和B组合");
            return a+b;
        }).join();

        System.out.println("result = " + result);
    }

    public static void thenCompose() {
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
            sleep(2000);
            System.out.println("我是A...");
            return "A";
        });

        CompletableFuture<String> B = A.thenCompose(v ->{
            System.out.println(v);
            return CompletableFuture.supplyAsync(()->{
                sleep(1000);
                System.out.println("我是B...");
                return v+"+B";
            });
        });

        System.out.println("result = " + B.join());
    }

    public static void allOf(){
        CompletableFuture.allOf(CompletableFuture.runAsync(()->{
            sleep(3000);
            System.out.printf("%s[daemon:%s]我是A...\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        }),CompletableFuture.runAsync(()->{
            sleep(2000);
            System.out.printf("%s[daemon:%s]我是B...\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        }),CompletableFuture.runAsync(()->{
            sleep(1000);
            System.out.printf("%s[daemon:%s]我是C...\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        })).join();
    }

    public static void anyOf(){
        CompletableFuture.anyOf(CompletableFuture.runAsync(()->{
            sleep(3000);
            System.out.printf("%s[daemon:%s]我是A...\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        }),CompletableFuture.runAsync(()->{
            sleep(2000);
            System.out.printf("%s[daemon:%s]我是B...\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        }),CompletableFuture.runAsync(()->{
            sleep(1000);
            System.out.printf("%s[daemon:%s]我是C...\n",Thread.currentThread().getName(),Thread.currentThread().isDaemon());
        })).join();
    }

    public static void err(){
        throw new RuntimeException("2");
    }

    public static void exceptionally(boolean i) {
        String result = CompletableFuture.supplyAsync(()->{
            if (i) {
                return "hello world!";
            } else {
                throw new RuntimeException("errors : i = "+i);
            }
        }).exceptionally(e->{
            System.out.println(e);
            return "hello error!";
        }).join();
        System.out.println(result);
    }

    public static void get(boolean i) throws ExecutionException, InterruptedException {
//        String result = null;
//        try {
//            result = CompletableFuture.supplyAsync(()->{
//                if (i) {
//                    return "hello world!";
//                } else {
//                    throw new RuntimeException("errors : i = "+i);
//                }
//            }).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.out.println(result);

//        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->"Hello World!").toCompletableFuture();
//        System.out.println(A.toString());//java.util.concurrent.CompletableFuture@7699a589[Not completed]
//        A.join();
//        System.out.println(A.toString());//java.util.concurrent.CompletableFuture@7699a589[Completed normally]



    }

    public static void obtrudeValue() {

        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{

            sleep(1000);

//            return "Hello World!";
            throw new RuntimeException("111");
        });


        if (A.isCompletedExceptionally()) {
            A.obtrudeValue("MM");
        }


        try {
            System.out.println(A.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void obtrudeException(){
        CompletableFuture<String> A = CompletableFuture.supplyAsync(()->{
            sleep(2000);
            throw new RuntimeException("111");
        });



        A.obtrudeException(new Exception("ccc"));

        try {
            System.out.println(A.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void cancel(){
        CompletableFuture A = CompletableFuture.runAsync(()->{
            System.out.println("start running");
            sleep(1000);
            System.out.println("completed.");
        });
        sleep(100);
        A.cancel(true);
        System.out.println(A.isCancelled());
        System.out.println(A.isCompletedExceptionally());
        System.out.println(A.isDone());
        sleep(2000);
    }

    public static void main(String[] args) throws Exception {
        Thread thread1 = new Thread(()->{exceptionally(true);});thread1.start();
        Thread thread2 = new Thread(()->{sleep(600000);});thread2.start();

        sleep(4000);
        System.out.println("sssss"+thread1.getState());
//        exceptionally(true);
//        long t = System.currentTimeMillis();
//        System.out.println(TimeUnit.SECONDS.convert(System.currentTimeMillis()-t,TimeUnit.MILLISECONDS));
    }
}
