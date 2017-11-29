package com.qihancloud.asmplugin.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author viyu
 * @desc AOP method visitor，主要做的就是在方法中插入了AopInvoker的代码
 */
class AopMethodVisitor extends AdviceAdapter {

    private int mInvokerVarIndex = 0;

    private final String mClassName;
    private final String mMethodName;
    OnCheckResultListener onCheckResultListener;
    int mType;

    public AopMethodVisitor(int type,int api, MethodVisitor originMV, int access, String desc, String className, String methodName) {
        super(api, originMV, access, methodName, desc);
        mClassName = className;
        mMethodName = methodName;
        mType = type;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        beginAspect();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        afterAspect();
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        println maxStack + "," + maxLocals
        super.visitMaxs(maxStack + 0, maxLocals + 0);
    }

    /**
     * 在方法开始插入AopInvoker.aspectBeforeInvoke()
     */
    private void beginAspect() {
        if (mv == null) {
            return;
        }
       /* println mClassName + ',' + mMethodName
        if(mType == 1) {
            if (mMethodName.equals('onResume')) {

                /mv.visitMethodInsn(INVOKESTATIC, "com/viyu/aopframework/AopInvoker", "newInvoker", "(Ljava/lang/String;Ljava/lang/String;)Lcom/viyu/aopframework/AopInvoker;", false);
                 mv.visitInsn(DUP);
                 mv.visitMethodInsn(INVOKEVIRTUAL, "com/viyu/aopframework/AopInvoker", "aspectBeforeInvoke", "()V", false);
                 mInvokerVarIndex = newLocal(Type.getType("Lcom/viyu/aopframework/AopInvoker;"));
                 mv.visitVarInsn(ASTORE, mInvokerVarIndex);
            }
        }*/
    }

    /**
     * 在方法结束插入AopInvoker.aspectAfterInvoke()
     */
    private void afterAspect() {
        if (mv == null) {
            return;
        }
       println mClassName + ',' + mMethodName + ',' + mType
        if(mType == 1) {
            if (mMethodName.equals('onResume')) {
                mv.visitLdcInsn(mClassName);
                mv.visitMethodInsn(INVOKESTATIC, "com/qihancloud/qh_ros_user/Utils/NavigationUtils", "connected",
                        "(Ljava/lang/String;)Lcom/qihancloud/qh_ros_user/Utils/NavigationUtils;", false);
              /*  mv.visitMethodInsn(INVOKESTATIC, "com/example/test/BaseActivity", "test",
                        "()V", false);*/
                if(onCheckResultListener != null)
                    onCheckResultListener.onResultListener(mMethodName)

            }else if(mMethodName.equals('onPause')){
                mv.visitLdcInsn(mClassName);
                mv.visitMethodInsn(INVOKESTATIC, "com/qihancloud/qh_ros_user/Utils/NavigationUtils", "disconnected",
                        "(Ljava/lang/String;)Lcom/qihancloud/qh_ros_user/Utils/NavigationUtils;", false);
                /*  mv.visitMethodInsn(INVOKESTATIC, "com/example/test/BaseActivity", "test",
                          "()V", false);*/
                if(onCheckResultListener != null)
                    onCheckResultListener.onResultListener(mMethodName)
            }
        }else if(mType == 2){
            if (mMethodName.equals('onCreate')) {
                mv.visitLdcInsn(mClassName);
                mv.visitMethodInsn(INVOKESTATIC, "com/qihancloud/qh_ros_user/Utils/NavigationUtils", "connected",
                        "(Ljava/lang/String;)Lcom/qihancloud/qh_ros_user/Utils/NavigationUtils;", false);
                /*  mv.visitMethodInsn(INVOKESTATIC, "com/example/test/BaseActivity", "test",
                          "()V", false);*/
                if(onCheckResultListener != null)
                    onCheckResultListener.onResultListener(mMethodName)

            }else if(mMethodName.equals('onDestroy')){
                mv.visitLdcInsn(mClassName);
                mv.visitMethodInsn(INVOKESTATIC, "com/qihancloud/qh_ros_user/Utils/NavigationUtils", "disconnected",
                        "(Ljava/lang/String;)Lcom/qihancloud/qh_ros_user/Utils/NavigationUtils;", false);
                /*  mv.visitMethodInsn(INVOKESTATIC, "com/example/test/BaseActivity", "test",
                          "()V", false);*/
                if(onCheckResultListener != null)
                    onCheckResultListener.onResultListener(mMethodName)
            }
        }
    }

    public void setOnCheckResultListener(OnCheckResultListener listener){
        onCheckResultListener = listener
    }

    public interface OnCheckResultListener{
        void onResultListener(String methodName);
    }
}
