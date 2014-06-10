/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.footoo.Razor_Jeff.exception.RazorException;
import org.footoo.Razor_Jeff.kernel.RazorCompress;

/**
 * 
 * @author jeff
 * @version $Id: CompressTest.java, v 0.1 2014年6月10日 下午6:25:56 jeff Exp $
 */
public class CompressTest {

    /**
     * 
     * @param args
     * @throws RazorException 
     */
    public static void main(String[] args) throws RazorException {
        MyCompress compress = new MyCompress();
        long begin = System.currentTimeMillis();
        List<File> files = new ArrayList<>();
        files.add(new File("test/servlet"));
        files.add(new File("test/spring"));
        //files.add(new File("test/why"));
        compress.compress(files, "test/target.je");
        compress.uncompress("test/target.je", "test/target/");
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

}

class MyCompress extends RazorCompress {

    @Override
    public void processChanged(int process) {
        System.out.println("process:" + process);
    }

    @Override
    public void stateChanged(int state) {
        System.out.println("State changed:" + state);
    }

}
