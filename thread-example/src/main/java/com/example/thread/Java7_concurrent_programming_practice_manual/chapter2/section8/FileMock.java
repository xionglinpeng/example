package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section8;

public class FileMock {

    private String[] content;
    private int index;

    public FileMock(int size,int length) {
        this.content = new String[size];
        for (int i = 0; i < 10; i++) {
            StringBuilder buffer = new StringBuilder(length);
            for (int j = 0; j < length; j++) {
                int indice = (int)Math.random()*255;
                buffer.append((char)indice);
            }
            this.content[i] = buffer.toString();
        }
        this.index = 0;
    }

    /**
     * 如果文件有可以处理的数据行，则返回true；如果已经到达模拟文件的结尾则返回false。
     * @return
     */
    public boolean hasMoreLines() {
        return this.index < this.content.length;
    }

    /**
     * 返回属性index指定的行的内容。并且将index自动增加1。
     * @return
     */
    public String getLine() {
        if (this.hasMoreLines()) {
            System.out.println("Mock: "+(content.length - index));
            return content[index++];
        }
        return null;
    }

}
