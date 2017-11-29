package com.qihancloud.asmplugin.asm

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import com.qihancloud.asmplugin.asm.AopMethodVisitor.OnCheckResultListener;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * @author Viyu	
 * @desc AOP class visitor，主要做的就是调用了AopMethodVisitor
 */
public class AopClassVisitor extends ClassVisitor {

    /**
     * 目标包名，就是要被修改的类的包名
     */
    private final String mTargetPackageName;
    Set<String> methodSet = new HashSet<>();

    /**
     * 实际的类全名
     */
    private String mActualClassFullName;

    /**
     * 是否需要修改
     */
    private int mNeedModifyMethod = -1;

    public AopClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mActualClassFullName = name.replaceAll("/", ".");
        if(interfaces.length > 0){
            for(String interfaceChild : interfaces){
                if(interfaceChild.equals('com/qihancloud/qh_ros_user/ForegroundTag')) {
                    if(name.toLowerCase().contains('service') && !name.toLowerCase().contains('activity'))
                        throw new InterruptedException("The class ${name} may be a servcie,You should use the BackgroundTag interface.")
                    else {
                        mNeedModifyMethod = 1
                        return
                    }
                }else   if(interfaceChild.equals('com/qihancloud/qh_ros_user/BackgroundTag')) {
                    if(name.toLowerCase().contains('activity'))
                        throw new InterruptedException("The class ${name} may be a activity,You should use the ForgroundTag interface.")
                    else {
                        mNeedModifyMethod = 2
                        return
                    }
                }
            }
        }
        //checkSuperName(superName)
    }



    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mNeedModifyMethod != -1) {
            AopMethodVisitor visitor =  new AopMethodVisitor(mNeedModifyMethod,api, mv, access, desc, mActualClassFullName, name);
            visitor.setOnCheckResultListener(new OnCheckResultListener(){
                @Override
                void onResultListener(String methodName){
                    methodSet.add(methodName)
                }
            })
            return visitor
        }

        return mv;
    }

    @Override
    public void visitEnd() {
        if(mNeedModifyMethod == 1){
            if(!methodSet.contains("onResume")){
                MethodVisitor mv = visitMethod(Opcodes.ACC_PUBLIC, "onResume", "()V", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "onResume", "()V", false);
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            if(!methodSet.contains("onPause")){
                MethodVisitor mv = visitMethod(Opcodes.ACC_PUBLIC, "onPause", "()V", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "onPause", "()V", false);
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
        }else if(mNeedModifyMethod == 2){
            if(!methodSet.contains("onCreate")){
                MethodVisitor mv = visitMethod(Opcodes.ACC_PUBLIC, "onCreate", "()V", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "onCreate", "()V", false);
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            if(!methodSet.contains("onDestroy")){
                MethodVisitor mv = visitMethod(Opcodes.ACC_PUBLIC, "onDestroy", "()V", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "onDestroy", "()V", false);
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
        }
        super.visitEnd();
        mNeedModifyMethod = -1;
    }
}
