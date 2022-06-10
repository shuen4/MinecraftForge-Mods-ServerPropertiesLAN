function initializeCoreMod() {
    return {
        'splan1': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.client.server.IntegratedServer'
            },
            'transformer': function (classNode) {
                var namelist = ["m_7079_","isNetherEnabled","m_6993_","isCommandBlockEnabled","m_6373_","repliesToStatus","m_142205_","isResourcePackRequired"];
                var desc = "()Z";
                
                var asmHandler = "com/shuen/splan/splan";
                var asmMethodName = ["isNetherEnabled","isCommandBlockEnabled","repliesToStatus","isResourcePackRequired"];
                var asmMethodDesc = "()Z";
                
                var Opcodes = Java.type("org.objectweb.asm.Opcodes");
                var MethodNode = Java.type("org.objectweb.asm.tree.MethodNode");
                var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
                var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");

                var methods = classNode.methods;
                var tmp=[];
                
                for (m in methods)
                    for (n in namelist)
                        if (methods[m].name===namelist[n])
                            tmp.push(m);
                for (m in tmp)
                    methods.remove(tmp[tmp.length-m-1]);
                for (n in namelist) {
                    name=namelist[n];
                    var method=new MethodNode(Opcodes.ACC_PUBLIC,name,desc,null,null);
                    var instructions=new InsnList();
                    instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,asmHandler,asmMethodName[parseInt(n/2)],asmMethodDesc,false));
                    instructions.add(new InsnNode(Opcodes.IRETURN));
                    method.instructions.add(instructions);
                    methods.add(method);
                }
                return classNode;
            }
        }
    }
}