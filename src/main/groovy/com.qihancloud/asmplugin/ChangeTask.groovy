package com.qihancloud.asmplugin

import jdk.internal.org.objectweb.asm.Opcodes;
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.qihancloud.asmplugin.asm.AopClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import  org.objectweb.asm.Opcodes;

public class ChangeTask extends DefaultTask {
    String packageType = ''

    // 任务名
    static final String NAME = "Reject"

    public ChangeTask() {
    }

    @TaskAction
    def start() {
        def ext = project.buildDir.toString() + "/intermediates/classes/" + packageType
        println ext
        checkAllDir(ext)
    }

    public void setPackageType(String type){
        packageType = type
    }


    public void checkAllDir(String path) {
        File classFile = new File(path)
        File[] files = classFile.listFiles()
        if (files != null) {
        for (int i = 0; i < files.length; i ++) {
            File childFile = files[i]
            if(childFile.isDirectory()){
                checkAllDir(childFile.getAbsolutePath())
            }else {
                String absolutPath = childFile.absolutePath;
                if(absolutPath.endsWith(".class") && !absolutPath.contains('$') && !absolutPath.contains('android/support')) {
                    InputStream is;
                    try {

                        is = new FileInputStream(childFile);

                        byte[] tBytes = doAspect('', is);
                        is.close();
                       // def res = path + '/' + new Date().getTime() + '.class'
                       // println res
                        FileOutputStream fout = new FileOutputStream(childFile);
                        fout.write(tBytes)
                        fout.flush()
                        fout.close();

                    } catch (Exception e) {
                        if(e instanceof InterruptedException)
                            throw e
                        else
                        println e.getMessage()
                    }
                }
            }
        }
        }
    }

/**
 * 调用aop
 *
 * @param targetPackageName 目标包名，要被修改的类的包名
 * @param classInputStream  类的输入流
 * @return
 * @throws Exception
 */
    public  byte[] doAspect(String targetPackageName, InputStream classInputStream) throws Exception {
        return doAspect(targetPackageName, new ClassReader(classInputStream));
    }

    /**
     * 调用aop
     *
     * @param targetPackageName 目标包名，要被修改的类的包名
     * @param classBytes        类的二进制数据
     * @return
     * @throws Exception
     */
    public  byte[] doAspect(String targetPackageName, byte[] classBytes) throws Exception {
        return doAspect(targetPackageName, new ClassReader(classBytes));
    }


    /**
     * 调用aop
     *
     * @param targetPackageName 目标包名，要被修改的类的包名
     * @param classFile         类的二进制文件
     * @return
     */
    public  byte[] doAspect(String targetPackageName, File classFile) {
        InputStream is = null;
        byte[] tBytes = null;
        try {
            is = new FileInputStream(classFile);
            tBytes = doAspect(targetPackageName, is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
        return tBytes;
    }

    public  byte[] doAspect(String targetPackageName, ClassReader classReader) {
        ClassWriter tClassWrite = new ClassWriter(COMPUTE_MAXS);
        AopClassVisitor tAopClassVisitor = new AopClassVisitor(Opcodes.ASM5, tClassWrite);
        classReader.accept(tAopClassVisitor, EXPAND_FRAMES);
        byte[] tAspectedClassByte = tClassWrite.toByteArray();
        return tAspectedClassByte;
    }
}