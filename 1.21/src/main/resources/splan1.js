function initializeCoreMod() {
    return {
        'splan1': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.client.server.IntegratedServer'
            },
            'transformer': function (classNode) {
                var namelist = ["m_7079_","isLevelEnabled","m_6993_","isCommandBlockEnabled","m_6373_","repliesToStatus","m_183306_","hidesOnlinePlayers","m_214042_","getServerResourcePack"];
                var desc = ["(Lnet/minecraft/world/level/Level;)Z", "()Z","()Ljava/util/Optional;"];
                
                var asmHandler = "com/shuen/splan/splan";
                var asmMethodName = ["isLevelEnabled","isCommandBlockEnabled","repliesToStatus","hidesOnlinePlayers","getServerResourcePack"];
                var asmMethodDesc = ["(Lnet/minecraft/world/level/Level;)Z", "()Z","()Ljava/util/Optional;"];
                
                var Opcodes = Java.type("org.objectweb.asm.Opcodes");
                var MethodNode = Java.type("org.objectweb.asm.tree.MethodNode");
                var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
                var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
                var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");

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
                    var method=new MethodNode(Opcodes.ACC_PUBLIC,name,desc[n>2?(n<8?1:2):0],null,null);
                    var instructions=new InsnList();
					if (n<3)
						instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,asmHandler,asmMethodName[parseInt(n/2)],asmMethodDesc[n>2?(n<8?1:2):0],false));
                    instructions.add(new InsnNode(n<8?Opcodes.IRETURN:Opcodes.ARETURN));
                    method.instructions.add(instructions);
                    methods.add(method);
                }
                return classNode;
            }
        }
    }
}