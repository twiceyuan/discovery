package cn.numeron.discovery.plugin

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class DiscoveryVisitor : ClassNode(Opcodes.ASM7) {

    val order: Int
        get() = invisibleAnnotations.first {
            it.desc == IMPLEMENTATION_ANNOTATION
        }.values?.let {
            it[it.indexOf("order") + 1].toString().toInt()
        } ?: 0

    fun hasAnnotation(annotation: String): Boolean {
        invisibleAnnotations ?: return false
        return invisibleAnnotations.any {
            it.desc == annotation
        }
    }

    fun isAbstract(): Boolean {
        return access and Opcodes.ACC_ABSTRACT != 0
    }

    fun getAllSuperTypeNames(): List<String> {
        // 所有 interface name
        val superTypeNames = interfaces.map(String::toClassName).toMutableSet()
        val superName = superName
        // 父类 name（如抽象类）
        if (superName != null && superName != "java/lang/Object") {
            superTypeNames.add(superName.replace("/", "."))
        }
        return superTypeNames.toList()
    }
}
