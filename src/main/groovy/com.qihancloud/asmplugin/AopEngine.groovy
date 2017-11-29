package com.qihancloud.asmplugin;



import org.gradle.api.*;
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.AppExtension


/**
 * @author viyu
 * @desc AOP执行的调用入口
 */
public class AopEngine implements Plugin<Project>{
    static final String EXTENSION_NAME = 'AsmOption'

    void apply(Project project) {
        // 创建清理任务
        project.afterEvaluate {
            Task changeTask = project.tasks.create(ChangeTask.NAME, ChangeTask)
            project.android.applicationVariants.each { variant ->
                //println("variant:" + variant.name)

               /* def names = project.tasks.findAll{
                    task -> task.name.contains('compileLint')
                }

                if(!names.isEmpty())
                // names.first().dependsOn changeTask
                    changeTask.finalizedBy {names.first()}
                else
                    println 'task 不存在'*/
                def dexTaskName = "transformClassesWithDexFor${variant.name.capitalize()}"
                println dexTaskName
                def dexTask = project.tasks.findByName(dexTaskName)
                if (dexTask) {
                    changeTask.setPackageType(variant.name.capitalize().toLowerCase())
                    dexTask.dependsOn changeTask
                }
            }
        }
       /* if (project.plugins.hasPlugin(AppPlugin)) {
            AppExtension android = project.extensions.getByType(AppExtension)
            android.registerTransform(new MyTransform(project))
        }*/
    }


}
