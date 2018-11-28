package com.xlp.example.banner;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class SpringBootBuddhaBanner implements Banner {

    private static final String BANNER =
                    "                   _ooOoo_"+"\n"+
                    "                  o8888888o"+"\n"+
                    "                  88\" . \"88"+"\n"+
                    "                  (| -_- |)"+"\n"+
                    "                  O\\  =  /O"+"\n"+
                    "               ____/`---'\\____"+"\n"+
                    "             .'  \\\\|     |//  `."+"\n"+
                    "            /  \\\\|||  :  |||//  \\"+"\n"+
                    "           /  _||||| -:- |||||-  \\"+"\n"+
                    "           |   | \\\\\\  -  /// |   |"+"\n"+
                    "           | \\_|  ''\\---/''  |   |"+"\n"+
                    "           \\  .-\\__  `-`  ___/-. /"+"\n"+
                    "         ___`. .'  /--.--\\  `. . __"+"\n"+
                    "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\"."+"\n"+
                    "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |"+"\n"+
                    "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /"+"\n"+
                    "======`-.____`-.___\\_____/___.-`____.-'======"+"\n"+
                    "                   `=---='"+"\n"+
                    "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+"\n"+
                    "          佛祖保佑       永无BUG";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {

        out.println(BANNER);
        out.println();
    }
}
