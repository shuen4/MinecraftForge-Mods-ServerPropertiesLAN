function initializeCoreMod() {
    return {
        'splan': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.util.HttpUtil'
            },
            'transformer': function (classNode) {
                var SRG_Name = "m_13939_";
                var name = "getAvailablePort";
                var desc = "(I)V";
                
                var asmHandler = "com/shuen/splan/splan";
                var asmMethodName = "getPort";
                var asmMethodDesc = "()I";
                
                var Opcodes = Java.type("org.objectweb.asm.Opcodes");
                var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                var AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");

                var methods = classNode.methods;
                for (m in methods) 
                {
                    var method = methods[m];
                    if (method.name === SRG_Name || method.name === name) 
                    {
                        var code = method.instructions;
                        var instr = code.toArray();
                        for (t in instr) 
                        {
                            var instruction = instr[t];
                            if (instruction.getType() === AbstractInsnNode.METHOD_INSN && instruction.getOpcode() === Opcodes.INVOKESPECIAL && instruction.desc === desc)
                            {
                                var instruction2 = instruction.getPrevious();
                                if (instruction2.getOpcode() === Opcodes.ICONST_0)
                                {
                                    code.remove(instruction2);
                                    code.insertBefore(instruction,new MethodInsnNode(Opcodes.INVOKESTATIC, asmHandler, asmMethodName, asmMethodDesc, false));
                                }
                                break;
                            }
                        }
                        break;
                    }
                }

                return classNode;
            }
        }
    }
}