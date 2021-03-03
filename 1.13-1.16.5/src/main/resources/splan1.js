function initializeCoreMod() {
    return {
        'coremodone': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.util.HttpUtil'
            },
            'transformer': function (classNode) {
            	var asmHandler = "com/shuen/splan/splan";
            	
                var Opcodes = Java.type("org.objectweb.asm.Opcodes");

                var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
				var AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");
                var api = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                var methods = classNode.methods;

                for (m in methods) 
                {
                    var method = methods[m];
                    if (method.name === "func_76181_a" || method.name === "getSuitableLanPort") 
                    {
                        var code = method.instructions;
                        var instr = code.toArray();
                        for (t in instr) 
                        {
                            var instruction = instr[t];
                            if (instruction.getType() === AbstractInsnNode.METHOD_INSN && instruction.getOpcode() === Opcodes.INVOKESPECIAL && instruction.desc === "(I)V")
                            {
								var instruction2 = instruction.getPrevious();
								if (instruction2.getOpcode() === Opcodes.ICONST_0)
								{
									code.remove(instruction2);
									code.insertBefore(instruction,new MethodInsnNode(Opcodes.INVOKESTATIC, asmHandler, "getPort", "()I", false));
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